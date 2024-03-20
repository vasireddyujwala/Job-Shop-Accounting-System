<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customers</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f4f4f4;
        }

        .card {
            width: 70%;
            background-color: #fff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            padding: 20px;
        }

        h4 {
            margin: 0;
        }

        table {
            margin: auto;
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
    </style>
</head>
<body>
    <%@page import="jsp_azure.DataHandler"%>
    <%@page import="java.sql.ResultSet"%>
    
    <%
        // We instantiate the data handler here, and get all the customers from the database
        final DataHandler handler = new DataHandler();
        final ResultSet customers = handler.getAllCustomers();
    %>

    <div class="card">
        <h2>All Customers</h2>

        <!-- The table for displaying all the customer records -->
        <table>
            <tr> <!-- The table headers row -->
                <th align="center"><h4>Customer</h4></th>
                <th align="center"><h4>Address</h4></th>
                <th align="center"><h4>Category</h4></th>
            </tr>

            <%
                while (customers.next()) { // For each Customer record returned...
                    // Extract the attribute values for every row returned
                    final String cname = customers.getString("cname");
                    final String caddress = customers.getString("caddress");
                    final String category = customers.getString("category");

                    out.println("<tr>"); // Start printing out the new table row
                    out.println( // Print each attribute value
                            "<td align=\"center\">" + cname +
                            "</td><td align=\"center\"> " + caddress +
                            "</td><td align=\"center\"> " + category + "</td>");
                    out.println("</tr>");
                }
            %>
        </table>
    </div>
</body>
</html>
