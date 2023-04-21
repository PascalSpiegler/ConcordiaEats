<!doctype html>

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

    <title> Add Discount | ConcordiaEats </title>
</head>
<body>
<jsp:include page="./adminHeader.jsp" />

<div class="jumbotron container border border-info">
    <h3>Add Discount</h3>
    <form action="sendData" method="post">
        <div class="row">
            <div class="col-sm-5">
                <div class="form-group">
                    <label for="id">Product</label>
                    <select class="form-control border border-success" name="id" id="id">
                        <option disabled selected>View products...</option>
                        <c:forEach items="${availableProducts}" var="availableProduct">
=                            <option value="${availableProduct.getId()}">ID ${availableProduct.getId()}: ${availableProduct.getName()}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="discountPercentage">Discount Percentage</label>
                    <input class="form-control border border-success" min = '1' max = '99' type="number" name="discountPercentage" id="discountPercentage">
                </div>

                <input type="hidden" name="imgName">
                <input type="submit" class="btn btn-primary">
            </div>
        </div>
    </form>
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