<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Retrieve Customers Given Category Range</title>
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
            width: 50%;
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
            margin-top: 20px;
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
    </style>
</head>
<body>
    <div class="card">
        <h2>Retrieve Customers Given Category Range</h2>

        <!-- Form for collecting user input for retrieving customer records -->
        <form action="retrieve_customers.jsp" method="post">
            <!-- The form organized in an HTML table for better clarity. -->
            <table>
                <tr>
                    <th colspan="2">Enter the Category Range:</th>
                </tr>
                <tr>
                    <td>Lower Bound:</td>
                    <td>
                        <div>
                            <input type="text" name="lower_b" required>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Upper Bound:</td>
                    <td>
                        <div>
                            <input type="text" name="upper_b" required>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div>
                            <input type="reset" value="Clear">
                            <input type="submit" value="Retrieve">
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
