<!doctype html>
<%@page import="java.sql.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.7.0/css/all.css"
	integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ"
	crossorigin="anonymous">
<title>Update Product | ConcordiaEats </title>
</head>
<body>
<jsp:include page="./adminHeader.jsp" />
	<div class="jumbotron container border border-info">
		<h3>Update Existing Product</h3>
		<form action="/admin/products/updateData" method="post">
			<div class="row">
				<div class="col-sm-5">
					
					<div class="form-group">
						<label>Id</label>
						<input type="number" readonly="readonly" class="form-control border border-success" name="id"  value="${product.getId()}">
						

					</div>
					<div class="form-group">
						<label for="name">Name</label> 
						<input type="text" class="form-control border border-success" required name="name" value="${product.getName() }" placeholder="Enter name">
					</div>

					<div class="form-group">
						<label for="categoryId">Select a restaurant:</label>
						<select class="form-control border border-success" name="restaurantId" id="restaurantId">
							<c:forEach items="${restaurants}" var="restaurant">
								<option value="${restaurant.getId()}" <c:if test="${product.getRestaurantId() == restaurant.getId()}"> selected</c:if>>${restaurant.getId()}: ${restaurant.getName()}</option>
							</c:forEach>
						</select>
					</div>

					<div class="form-group">
						<label for="categoryId">Select a category:</label>
						<select class="form-control border border-success" name="categoryId" id="categoryId">
							<c:forEach items="${categories}" var="category">
								<option value="${category.getId()}" <c:if test="${product.getCategoryId() == category.getId()}"> selected</c:if>>${category.getId()}: ${category.getName()}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="price">Price</label> 
						<input type="number" step=".01" class="form-control border border-success" required name="price" value="${ product.getPrice() }" min="1" placeholder="Price">
					</div>
					
				</div>
				
				<div class="col-sm-5">
				<div class="form-group">
						<label for="description">Product Description</label>
						<input class="form-control border border-success" rows="4" name="description" required placeholder="Product Details" value= "${ product.getDescription() }">
					</div>

					<div class="form-group">
						<label for="image">Product Image URL</label>
						<input type="text" class="form-control border border-success" required name="image" value="${product.getImage()}" placeholder="Enter Image URL">
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