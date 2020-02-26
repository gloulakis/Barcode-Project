/*
 Copyright (C) 2005 MySQL AB

 This program is free software; you can redistribute it and/or modify
 it under the terms of version 2 of the GNU General Public License as 
 published by the Free Software Foundation.

 There are special exceptions to the terms and conditions of the GPL 
 as it is applied to this software. View the full text of the 
 exception in file EXCEPTIONS-CONNECTOR-J in the directory of this 
 software distribution.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/

package testsuite.simple;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.rmi.server.UID;
import java.sql.Connection;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlXid;

import testsuite.BaseTestCase;

/**
 * Unit tests for our XA implementation.
 *
 * @version $Id: $
 */
public class XATest extends BaseTestCase {
    MysqlXADataSource xaDs;
    
	public XATest(String name) {
		super(name);
		
		this.xaDs = new MysqlXADataSource();
		this.xaDs.setUrl(BaseTestCase.dbUrl);
		this.xaDs.setRollbackOnPooledClose(true);
	}

	/**
	 * Tests that simple distributed transaction processing
	 * works as expected.
	 * 
	 * @throws Exception if the test fails.
	 */
	public void testCoordination() throws Exception {
		if (!versionMeetsMinimum(5, 0)) {
			return;
		}
		
		createTable("testCoordination", "(field1 int) ENGINE=InnoDB");
		
		Connection conn1 = null;
		Connection conn2 = null;
		XAConnection xaConn1 = null;
		XAConnection xaConn2 = null;
		
		try {
			xaConn1 = getXAConnection();
			XAResource xaRes1 = xaConn1.getXAResource();
			conn1 = xaConn1.getConnection();
			
			xaConn2 = getXAConnection();
			XAResource xaRes2 = xaConn2.getXAResource();
			conn2 = xaConn2.getConnection();
			
			Xid xid1 = createXid();
			Xid xid2 = createXid(xid1);
			
			xaRes1.start(xid1, XAResource.TMNOFLAGS);
			xaRes2.start(xid2, XAResource.TMNOFLAGS);
			conn1.createStatement().executeUpdate("INSERT INTO testCoordination VALUES (1)");
			conn2.createStatement().executeUpdate("INSERT INTO testCoordination VALUES (2)");
			xaRes1.end(xid1, XAResource.TMSUCCESS);
			xaRes2.end(xid2, XAResource.TMSUCCESS);
			
			xaRes1.prepare(xid1);
			xaRes2.prepare(xid2);
			
			xaRes1.commit(xid1, false);
			xaRes2.commit(xid2, false);
			
			this.rs = this.stmt.executeQuery("SELECT field1 FROM testCoordination ORDER BY field1");
			
			assertTrue(this.rs.next());
			assertEquals(1, this.rs.getInt(1));
			
			assertTrue(this.rs.next());
			assertEquals(2, this.rs.getInt(1));
			
			this.stmt.executeUpdate("TRUNCATE TABLE testCoordination");
			
			//
			// Now test rollback
			//
			
			xid1 = createXid();
			xid2 = createXid(xid1);
			
			xaRes1.start(xid1, XAResource.TMNOFLAGS);
			xaRes2.start(xid2, XAResource.TMNOFLAGS);
			conn1.createStatement().executeUpdate("INSERT INTO testCoordination VALUES (1)");
			
			// ensure visibility
			assertEquals("1", getSingleIndexedValueWithQuery(conn1, 1, "SELECT field1 FROM testCoordination WHERE field1=1").toString());
			
			conn2.createStatement().executeUpdate("INSERT INTO testCoordination VALUES (2)");
			
			//		 ensure visibility
			assertEquals("2", getSingleIndexedValueWithQuery(conn2, 1, "SELECT field1 FROM testCoordination WHERE field1=2").toString());
			
			xaRes1.end(xid1, XAResource.TMSUCCESS);
			xaRes2.end(xid2, XAResource.TMSUCCESS);
			
			xaRes1.prepare(xid1);
			xaRes2.prepare(xid2);
			
			xaRes1.rollback(xid1);
			xaRes2.rollback(xid2);
			
			this.rs = this.stmt.executeQuery("SELECT field1 FROM testCoordination ORDER BY field1");
			
			assertTrue(!this.rs.next());
		} finally {
			if (conn1 != null) {
				conn1.close();
			}
			
			if (conn2 != null) {
				conn2.close();
			}
			
			if (xaConn1 != null) {
				xaConn1.close();
			}
			
			if (xaConn2 != null) {
				xaConn2.close();
			}
		}
	}
	
	protected XAConnection getXAConnection() throws Exception {
		return this.xaDs.getXAConnection();
	}
	
	/**
	 * Tests that XA RECOVER works as expected.
	 * 
	 * @throws Exception if test fails
	 */
	public void testRecover() throws Exception {
		if (!versionMeetsMinimum(5, 0)) {
			return;
		}

		XAConnection xaConn = null, recoverConn = null;
		
		try {			
			xaConn = getXAConnection();
			
			Connection c = xaConn.getConnection();
			Xid xid = createXid();
			
			XAResource xaRes = xaConn.getXAResource();
			xaRes.start(xid, XAResource.TMNOFLAGS);
			c.createStatement().executeQuery("SELECT 1");
			xaRes.end(xid, XAResource.TMSUCCESS);
			xaRes.prepare(xid);
			
			// Now try and recover
			recoverConn = getXAConnection();
			
			XAResource recoverRes = recoverConn.getXAResource();
			
			Xid[] recoveredXids = recoverRes.recover(XAResource.TMNOFLAGS);
			
			assertTrue(recoveredXids != null);
			assertTrue(recoveredXids.length > 0);
			
			boolean xidFound = false;
			
			for (int i = 0; i < recoveredXids.length; i++) {
				if (recoveredXids[i] != null &&
					recoveredXids[i].equals(xid)) {
					xidFound = true;
					
					break;
				}
			}
			
			assertTrue(xidFound);
		} finally {
			if (xaConn != null) {
				xaConn.close();
			}
			
			if (recoverConn != null) {
				recoverConn.close();
			}
		}
	}

	private Xid createXid() throws IOException {
		ByteArrayOutputStream gtridOut = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(gtridOut);
		new UID().write(dataOut);
		
		final byte[] gtrid = gtridOut.toByteArray();
		
		ByteArrayOutputStream bqualOut = new ByteArrayOutputStream();
		dataOut = new DataOutputStream(bqualOut);
		
		new UID().write(dataOut);
		
		final byte[] bqual = bqualOut.toByteArray();
		
		Xid xid = new MysqlXid(gtrid, bqual, 3306);
		return xid;
	}

	private Xid createXid(Xid xidToBranch) throws IOException {
		ByteArrayOutputStream bqualOut = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(bqualOut);
		
		new UID().write(dataOut);
		
		final byte[] bqual = bqualOut.toByteArray();
		
		Xid xid = new MysqlXid(xidToBranch.getGlobalTransactionId(), bqual, 3306);
		
		return xid;
	}
}
