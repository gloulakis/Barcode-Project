/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Georgios.Loulakis
 * 
 */

public class DB_Connection {

    /**
     * 
     * @param args the command line arguments
     * 
     */
    
    public static void main(String[] args) {
        DB_Connection od = new DB_Connection();
        List<String> orderlist = od.Order();
        for (int i = 0; i < orderlist.size(); i++) {
            System.out.println(orderlist.get(i));
        }
    }
        public List<String> Order(){
            String order = null;
            String [] list = null;
            DB_Connection od = new DB_Connection();
            Connection con = od.getConnection();
            List<String> orderList = new ArrayList<String>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT Distinct (order_code)\n" +
                    "FROM V_TASK\n" +
                    "WHERE order_code is not null \n" +
                    "AND Type = 'Picking'\n" +
                    "AND Status ='Нова'\n" +
                    "AND depositorid = 215");
             while(rs.next()){
              orderList.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB_Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
            return orderList;
        }
        
        public Connection getConnection(){
            Connection connection = null;
            try{
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection("jdbc:sqlserver://192.168.21.3;databaseName=bers;user=sa;password=q2w3e4r%");
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB_Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DB_Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
            return connection;
        }
}
