<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Welcome to Job-Shop Accounting</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 20px;
            padding: 20px;
            background-color: #f4f4f4;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }

        h1 {
            color: #333;
        }

        .card-container {
            display: flex;
            justify-content: space-around;
            margin-top: 20px;
        }

        .card {
            width: 400px;
            padding: 20px;
            background-color: #eee; /* Gray background color */
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            text-align: center;
        }

        h2 {
            color: #333;
        }

        a {
            display: block;
            margin: 10px 0;
            padding: 10px;
            background-color: #3498db;
            color: #fff;
            text-decoration: none;
            text-align: center;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        a:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
    <h1>Welcome to Job-Shop Accounting</h1>

    <div class="card-container">
        <!-- Add New Customer Card -->
        <div class="card">
            <h2>Add New Customer</h2>
            <a href="add_new_customer_form.jsp">Go to Add New Customer</a>
        </div>

        <!-- Retrieve Customers Card -->
        <div class="card">
            <h2>Retrieve Customers</h2>
            <a href="retrieve_customer_form.jsp">Go to Retrieve Customers</a>
        </div>
    </div>
</body>
</html>
