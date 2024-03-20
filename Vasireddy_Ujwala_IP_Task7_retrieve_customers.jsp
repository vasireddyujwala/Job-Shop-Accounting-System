<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customers</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        h4 {
            margin: 0;
        }

        table {
            margin: auto;
            border-collapse: collapse;
            width: 50%;
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
        // We instantiate the data handler here and get customers from the database
        final DataHandler handler = new DataHandler();

        // Get the attribute values passed from the input form.
        String lower_b = request.getParameter("lower_b");
        String upper_b = request.getParameter("upper_b");

        if (lower_b == null || upper_b == null || lower_b.trim().isEmpty() || upper_b.trim().isEmpty()) {
            response.sendRedirect("retrieve_customer_form.jsp");
        } else {
            try {
                int lower_b1 = Integer.parseInt(lower_b);
                int upper_b1 = Integer.parseInt(upper_b);

                // Now perform the query with the data from the form.
                final ResultSet customers = handler.retrieveCustomers(lower_b1, upper_b1);
    %>

                <!-- The table for displaying all the Customer records -->
                <table>
                    <tr> <!-- The table headers row -->
                        <th align="center"><h4>Customer</h4></th>
                        <th align="center"><h4>Category</h4></th>
                    </tr>

                    <%
                        while (customers.next()) { // For each Customer record returned...
                            // Extract the attribute values for every row returned
                            final String cname = customers.getString("name");
                            final String category = customers.getString("category");

                            out.println("<tr>"); // Start printing out the new table row
                            out.println( // Print each attribute value
                                    "<td align=\"center\">" + cname +
                                    "</td><td align=\"center\"> " + category + "</td>");
                            out.println("</tr>");
                        }
                    }
                 catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid integer
    %>
                    <h2>Error: Invalid input for category range. Please enter valid integers.</h2>
                    <a href="retrieve_customer_form.jsp">Go back to the form</a>
    <%
                }
            }
    %>
    </table>
</body>
</html>
