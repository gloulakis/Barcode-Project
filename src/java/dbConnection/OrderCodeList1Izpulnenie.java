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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Georgios.Loulakis
 * 
 */

public class OrderCodeList1Izpulnenie {

    /**
     * 
     * @param args the command line arguments
     * 
     */
    
        private ArrayList<String> myList;
        private String orderCode;
   
        public OrderCodeList1Izpulnenie(){
            myList = new  ArrayList<String>();
            int count=0;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String url = "jdbc:sqlserver://192.168.21.3;databaseName=bers;user=sa;password=q2w3e4r%";
                Connection con = DriverManager.getConnection(url);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT DISTINCT(order_code)\n" +
                                                "FROM V_TASK\n" +
                                                "WHERE order_code is not null \n" +
                                                "AND Status ='В изпълнение'\n" +
                                                "AND depositorid = 215");
                while(rs.next()){
                   myList.add(rs.getString(1));
                }
                    } catch (ClassNotFoundException ex) {
                    Logger.getLogger(OrderCodeList1Izpulnenie.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(OrderCodeList1Izpulnenie.class.getName()).log(Level.SEVERE, null, ex);
                    }
        }
        public ArrayList<String> getmyList2(){
            return myList;
        }
 
}

