<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
          crossorigin="anonymous">

    <title>Home | ConcordiaEats </title>

</head>
<body style="background-color: #FFE8A3" class="p-16">

<jsp:include page="./header.jsp" />
<div class="jumbotron text-center" style = "background-color: #ffffff;">
    <h1 class="display-2">Welcome, ${username}</h1><hr>
</div><br>
<div class="container-fluid" >
    <div class="row justify-content-center">
        <div class="col-sm-3 pt-4">
            <div class="card border border-info" style="background-color: white;">
                <div class="card-body text-center">
                    <h4 class="card-title">Favourites</h4>
                    <p>---------------------------------------------</p>
                    <p class="card-text">Access Favourite items here!</p>
                    <a href="/favourites" class="card-link btn btn-primary" style=" background-color: #91263A;">Favourite</a>

                </div>
            </div>
        </div><br>
        <div class="col-sm-3 pt-4">
            <div class="card" style="background-color: white;">
                <div class="card-body text-center">
                    <h4 class="card-title">Deals</h4>
                    <p>---------------------------------------------</p>
                    <p class="card-text">Best Deals</p>
                    <a href="/user/bestdeals" class="card-link btn btn-primary" style=" background-color: #91263A;">Get Deals</a>

                </div>
            </div>
        </div><br>
        <div class="col-sm-3 pt-4">
            <div class="card" style="background-color: white;">
                <div class="card-body text-center">
                    <h4 class="card-title">Order History</h4>
                    <p>---------------------------------------------</p>
                    <p class="card-text">View all orders here.</p>
                    <a href="/orderHistory" class="card-link btn btn-primary" style=" background-color: #91263A;">View History</a>

                </div>
            </div>
        </div><br>
    </div>
</div>
<div class="container mt-5">
    <div style="background-color: #91263A; padding:10px" class="row">
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
