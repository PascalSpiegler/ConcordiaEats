<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import ="java.io.FileOutputStream" %>
<%@page import=" java.io.ObjectOutputStream" %>
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

    <title>Document</title>
</head>
<body class="bg-light">
<jsp:include page="./adminHeader.jsp" />
<div class="container-fluid">

    <a style="margin: 20px 0" class="btn btn-primary"
       href="/admin/products/add">Add Product</a><br>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Product ID</th>
            <th scope="col">Restaurant ID</th>
            <th scope="col">Product Name</th>
            <th scope="col">Preview</th>
            <th scope="col">Price</th>
            <th scope="col">Description</th>
            <th scope="col">Category ID</th>
            <th scope="col">Sales</th>
            <th scope="col">Delete</th>
            <th scope="col">Update</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${products}" var="product">
            <tr>
                <td>
                        ${product[0]}
                        <c:if test="${mostSelling.contains(product[0])}">
                            <br>
                            <span class="badge badge-success">Most Selling!</span>
                        </c:if>

                        <c:if test="${leastSelling.contains(product[0])}">
                            <br>
                            <span class="badge badge-danger">Least Selling!</span>
                        </c:if>
                </td>
                <td>
                    ${product[1]}
                </td>
                <td>${product[2]}</td>
                <td><img src="${product[3]}" height="100px" width="100px"></td>
                <td>$${product[4]}</td>
                <td>${product[5]}</td>
                <td>${product[6]}</td>
                <td>
                    <c:if test="${sales.containsKey(product[0])}">
                        ${sales.get(product[0])}
                    </c:if>
                    <c:if test="${not sales.containsKey(product[0])}">
                        0
                    </c:if>
                </td>
                <td>
                    <form action="products/delete" method="get">
                        <input type="hidden" name="id" value="${product[0]}">
                        <input type="submit" value="Delete" class="btn btn-danger">
                    </form>
                </td>
                <td>
                    <form action="products/update" method="get">
                        <input type="hidden" name="pid" value="${product[0]}">
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