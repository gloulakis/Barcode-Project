
<%@page import="dbConnection.OrderCodeList1Izpulnenie"%>
<%@page import="dbConnection.OrderCodeList"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="dbConnection.DB_Connection"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta charset=utf-8>
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Barcode Code Generator</title>
    </head>
    <body>
        <div id="col">
                    <div id="New">
                        <p style="color:#F7CAC9;">NOVA</p>
                    <%       
                             OrderCodeList orderCode;
                             orderCode = new OrderCodeList();
                             List<String> orderList = orderCode.getmyList();

                             for (int i = 0; i < orderList.size(); i++) {
                                      %>
                                    <img alt="my Image" src="Create_Bar_Code_With_Parameter?value=<%=orderList.get(i)%>"><br><br>
                                     <% 
                                 }                
                    %>
                  </div>        
                <div id="Ispulnenie">
                      <p style="color:whitesmoke;">IZPULNENIE</p>
                    <%       
                             OrderCodeList1Izpulnenie orderCode2;
                             orderCode2 = new OrderCodeList1Izpulnenie();
                             List<String> orderList2 = orderCode2.getmyList2();
                             for (int i = 0; i < orderList2.size(); i++) {
                                      %>
                                    <img alt="my Image" src="Create_Bar_Code_With_Parameter?value=<%=orderList2.get(i)%>"><br><br>
                                     <% 
                                 }                
                    %>
                </div>
        
                <div>
                        <%
                            response.setIntHeader("Refresh", 5);
                            Calendar calendar = new GregorianCalendar();
                            String am_pm;
                            int hour = calendar.get(Calendar.HOUR);
                            int minute = calendar.get(Calendar.MINUTE);
                            int second = calendar.get(Calendar.SECOND);
                            if(calendar.get(Calendar.AM_PM) == 0)
                               am_pm = "AM";
                            else
                               am_pm = "PM";
                            String CT = hour+":"+ minute +":"+ second +" "+ am_pm;
                            //out.println("Current Time: " + CT + "\n");
                        %>      
                </div>
       </div>
    </body>
</html>
