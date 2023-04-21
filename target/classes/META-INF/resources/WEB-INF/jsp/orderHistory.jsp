<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
          crossorigin="anonymous">

    <title>Order History | ConcordiaEats </title>

    <style>
        .card-img-top {
            height: 200px;
            object-fit: cover;
        }

        .card-body {
            height: 200px;
        }

        .card-text {
            height: 70px;
        }

    </style>
</head>
<body style="background-color: #FFE8A3" class="p-16">

<jsp:include page="./header.jsp" />
<div class="container-fluid" >
    <h1>Order History</h1>
</div>
<div class="container mt-5">
    <div style="background-color: #91263A; padding:10px" class="row">
        <div style="background-color: #ffffff; padding:10px; width: 100%;" class="">
            <div>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total Price</th>
                        <th>Timestamp</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${transactionList}" var="transaction">
                        <tr>
                            <td>${transaction.product_name}</td>
                            <td>${transaction.quantity}</td>
                            <td>${transaction.price}</td>
                            <td>${transaction.totalPrice}</td>
                            <td>${transaction.timestamp}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
        crossorigin="anonymous"></script>
<script
        src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script
        src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>
</body>
</html>
