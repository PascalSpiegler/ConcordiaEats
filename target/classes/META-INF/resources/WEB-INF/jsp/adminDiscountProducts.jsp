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

    <title>Discounts | ConcordiaEats </title>
</head>
<body class="bg-light">
<jsp:include page="./adminHeader.jsp" />

<div class="container-fluid">

    <a style="margin: 20px 0" class="btn btn-primary"
       href="/admin/discountproducts/add">Add Discount</a>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Product ID</th>
            <th scope="col">Restaurant ID</th>
            <th scope="col">Product Name</th>
            <th scope="col">Preview</th>
            <th scope="col">Original Price</th>
            <th scope="col">Discount Percentage</th>
            <th scope="col">Discount Price</th>
            <th scope="col">Delete</th>
            <th scope="col">Update</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${discountProducts}" var="discountProduct">
            <tr>
                <td>
                        ${discountProduct.getId()}
                </td>
                <td>
                        ${discountProduct.getRestaurantId()}
                </td>
                <td>${discountProduct.getName()}</td>
                <td><img src="${discountProduct.getImage()}" height="100px" width="100px"></td>
                <td>$${discountProduct.getPrice() / (1/1-(discounts.get(discountProduct.getId())/100))}</td>
                <td>${discounts.get(discountProduct.getId())}%</td>
                <td>$${discountProduct.getPrice()}</td>
                <td>
                    <form action="discountproducts/delete" method="get">
                        <input type="hidden" name="id" value="${discountProduct.getId()}">
                        <input type="submit" value="Delete" class="btn btn-danger">
                    </form>
                </td>
                <td>
                    <form action="discountproducts/update" method="get">
                        <input type="hidden" name="pid" value="${discountProduct.getId()}">
                        <input type="submit" value="Update" class="btn btn-warning">
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
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
