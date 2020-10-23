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
            ResultSet rs = st.executeQuery("SELECT ......");
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
                connection = DriverManager.getConnection("jdbc:sqlserver://;databaseName=;user=;password=");
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB_Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DB_Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
            return connection;
        }
}
