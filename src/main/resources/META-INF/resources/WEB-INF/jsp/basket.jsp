<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.jtspringproject.JtSpringProject.model.Product" %>

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

    <title>Basket | ConcordiaEats </title>

</head>

<style>
    .table td, .table th{
        border-top: 1px solid #000 !important;
    }

    .table thead th {
        border-bottom: 2px solid #000 !important;
    }
</style>

<body style="background-color: #FFE8A3" class="p-16">
<jsp:include page="./header.jsp" />


<div class="container">
    <div>
    <c:if test = "${basket.isEmpty()}">
        <h1>Basket is empty.</h1>
        <br>
        <a style="color: #91263A" href="/products">Add products to basket</a>
    </c:if>
    <c:if test = "${!basket.isEmpty()}">
        <h1>Basket</h1>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Product</th>
                <th scope="col">Quantity</th>
                <th scope="col">Price</th>
                <th scope="col">Subtotal</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${basket}" var="entry">
                <tr>
                    <td>${entry.getKey().getName()}</td>
                    <td>${entry.getValue()}</td>
                    <td>
                        <c:if test = "${discountIds.contains(entry.getKey().getId())}">
                            <div class="d-flex align-items-center">
                                <del class="text-muted me-2 mr-2">$${String.format("%.2f", (entry.getKey().getPrice()/(1-(discounts.get(entry.getKey().getId())/100))))}</del>
                                <span class="text-danger fw-bold">  $${entry.getKey().getPrice()}</span>
                            </div>
                        </c:if>
                        <c:if test = "${!discountIds.contains(entry.getKey().getId())}">
                            <div class="d-flex align-items-center">
                                <span class="fw-bold">  $${entry.getKey().getPrice()}</span>
                            </div>
                        </c:if>

                    </td>
                    <td>$${entry.getKey().getPrice() * entry.getValue()}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <table class="table" style="border-top: none; margin-top: 100px">
            <tbody style="border-top: none;">
            <tr style= "border-top: none;">
                <td colspan="3" class="text-right"><strong>Subtotal:</strong></td>
                <td>$${subtotal}</td>
            </tr>
            <tr style="border-top: none;">
                <td style="border-top: none;" colspan="3" class="text-right"><strong>QST (9.975%):</strong></td>
                <td>$${qst}</td>
            </tr>
            <tr style="border-top: none;">
                <td style="border-top: none;" colspan="3" class="text-right"><strong>GST (5%):</strong></td>
                <td>$${gst}</td>
            </tr>
            <tr style="border-top: none;">
                <td style="border-top: none;" colspan="3" class="text-right"><strong>Total:</strong></td>
                <td>$${total}</td>
            </tr>
            </tbody>
        </table>


        <a style="background-color: #91263A; border-color: #91263A" href="/checkout" class="btn btn-primary float-right">Proceed to Checkout</a>
    </c:if>
    </div>
</div>
</body>
</html>
