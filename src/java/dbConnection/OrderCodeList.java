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

public class OrderCodeList {

    /**
     * 
     * @param args the command line arguments
     * 
     */
        private ArrayList<String> myList;
        private String orderCode;
        
        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        
        
        
        
        public OrderCodeList(){
            myList = new  ArrayList<String>();
            int count=0;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String url = "jdbc:sqlserver://192.168.21.3;databaseName=bers;user=sa;password=q2w3e4r%";
                Connection con = DriverManager.getConnection(url);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT Distinct (order_code)\n" +
                                                    "FROM V_TASK\n" +
                                                    "WHERE order_code is not null\n" +
                                                    "AND depositorid = 215\n" +
                                                    "AND Status ='Нова'\n" +
                                                    "AND DATEPART(YEAR,createdon) = DATEPART(YEAR,GETDATE())\n" +
                                                    "AND DATEPART(MONTH,createdon) = DATEPART(MONTH,GETDATE())\n" +
                                                    "AND DATEPART(DAY,createdon) = DATEPART(DAY,GETDATE())");
                while(rs.next()){
                   myList.add(rs.getString(1));
                }
                    } catch (ClassNotFoundException ex) {
                    Logger.getLogger(OrderCodeList.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(OrderCodeList.class.getName()).log(Level.SEVERE, null, ex);
                    }
        }
        
        public ArrayList<String> getmyList(){
            return myList;
        }
        
        public static void main(String[] args){
            ArrayList<String> myList = myList();
            for (int i = 0; i < myList.size(); i++) {
                System.out.println(myList.get(i));
            }           
        }

         public static ArrayList<String> myList(){
         List<String> list = new ArrayList<>();
         int count=0;
         
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String url = "jdbc:sqlserver://192.168.21.3;databaseName=bers;user=sa;password=q2w3e4r%";

                Connection con = DriverManager.getConnection(url);
                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery("SELECT Distinct (order_code)\n" +
                                                    "FROM V_TASK\n" +
                                                    "WHERE order_code is not null\n" +
                                                    "AND depositorid = 215\n" +
                                                    "AND Status ='Нова'\n" +
                                                    "AND DATEPART(YEAR,createdon) = DATEPART(YEAR,GETDATE())\n" +
                                                    "AND DATEPART(MONTH,createdon) = DATEPART(MONTH,GETDATE())\n" +
                                                    "AND DATEPART(DAY,createdon) = DATEPART(DAY,GETDATE())");
                while(rs.next()){
                   list.add(rs.getString(1));
                }
                    } catch (ClassNotFoundException ex) {

                    Logger.getLogger(OrderCodeList.class.getName()).log(Level.SEVERE, null, ex);

                    } catch (SQLException ex) {

                        Logger.getLogger(OrderCodeList.class.getName()).log(Level.SEVERE, null, ex);

                    }
         return (ArrayList<String>) list;
       }
}

