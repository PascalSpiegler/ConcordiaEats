<!doctype html>
<%@page import="java.sql.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
          crossorigin="anonymous">
    <title>Update Discount | ConcordiaEats</title>
    <script type="text/javascript">
        function updateValues() {
            const discountPercentage = parseFloat(document.getElementById("discountPercentage").value);
            const discountedPrice = parseFloat(document.getElementById("discountedPrice").value);
            const output = discountedPrice/ (1 - discountPercentage / 100);
            if (!isNaN(output)) {
                document.getElementById("originalPrice").value = output.toFixed(2);
            } else {
                document.getElementById("originalPrice").value = "";
            }
        }
    </script>
</head>
<body>
<jsp:include page="./adminHeader.jsp" />

<div class="jumbotron container border border-info">
    <h3>Update Discount</h3>
    <form action="/admin/discountproducts/updateData" method="post">
        <div class="row">
            <div class="col-sm-5">

                <div class="form-group">
                    <label>Id</label>
                    <input type="number" readonly="readonly" class="form-control border border-success" name="id"  value="${product.getId()}">


                </div>
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" readonly="readonly" class="form-control border border-success" required name="name" value="${product.getName() }" placeholder="Enter name">
                </div>

                <div class="form-group">
                    <label for="restaurant">Restaurant ID</label>
                    <input type="text" readonly="readonly" class="form-control border border-success" required name="restaurant" value="${product.getRestaurantId() }" placeholder="Enter name">
                    </select>
                </div>

                <div class="form-group">
                    <label for="originalPrice">Original Price</label>
                    <input type="number" id="originalPrice" readonly="readonly" step=".01" class="form-control border border-success" required name="originalPrice" value="${ product.getPrice() / (1 - discounts.get(product.getId())/100) }" min="1">
                </div>

                <div class="form-group">
                    <label for="discountPercentage">Discount Percentage</label>
                    <input onchange="updateValues()" type="number" id="discountPercentage" step="1" class="form-control border border-success" required name="discountPercentage" value="${ discounts.get(product.getId())}" min="1" max = "99" placeholder="Price">
                </div>

                <div class="form-group">
                    <label for="discountedPrice">Discounted Price</label>
                    <input type="number" id = "discountedPrice" readonly="readonly" step=".01" class="form-control border border-success" required name="discountedPrice" value="${ product.getPrice() }" min="1" placeholder="Price">
                    <a href = "/admin/products/update?pid=${product.getId()}">Click here to update the discounted price</a>
                </div>
                <input type="hidden" name="imgName">
                <input type="submit" value="Update Details" class="btn btn-primary">

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