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
                String url = "jdbc:";
                Connection con = DriverManager.getConnection(url);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT ");
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
                String url = "jdbc:sqlserver://;databaseName=;user=;password=";

                Connection con = DriverManager.getConnection(url);
                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery("");
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

