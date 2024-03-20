<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Customer</title>
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

        h2 {
            text-align: center;
        }

        table {
            margin: auto;
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        input[type="text"] {
            width: calc(100% - 16px);
            padding: 8px;
            box-sizing: border-box;
        }

        input[type="reset"], input[type="submit"] {
            padding: 10px;
            width: calc(50% - 6px);
            box-sizing: border-box;
        }

        input[type="reset"] {
            background-color: #f44336;
            color: white;
            border: none;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
        }

        div {
            text-align: center;
        }

        .error {
            color: #f44336;
        }
    </style>
</head>
<body>
    <div class="card">
        <h2>Add Customer</h2>

        <!-- Form for collecting user input for the new customer record. -->
        <form action="add_new_customer.jsp" method="post">
            <!-- The form organized in an HTML table for better clarity. -->
            <table>
                <tr>
                    <th colspan="2">Enter the Customer Data:</th>
                </tr>
                <tr>
                    <td>Customer Name:</td>
                    <td>
                        <div>
                            <input type="text" name="cname" required>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Customer Address:</td>
                    <td>
                        <div>
                            <input type="text" name="address" required>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Category:</td>
                    <td>
                        <div>
                            <input type="text" name="category" required>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div>
                            <input type="reset" value="Clear">
                            <input type="submit" value="Insert">
                        </div>
                    </td>
                </tr>
            </table>
        </form>

        <!-- Display error messages, if any -->
        <%
            String errorMessage = (String)request.getAttribute("errorMessage");
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
                <div class="error"><%=errorMessage%></div>
        <%
            }
        %>
    </div>
</body>
</html>
