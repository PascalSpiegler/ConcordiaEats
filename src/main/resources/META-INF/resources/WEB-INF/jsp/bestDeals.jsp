<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">


    <title>Best Deals | ConcordiaEats </title>

    <style>
        .card-img-top {
            height: 200px;
            object-fit: cover;
        }

        .card-body {
            height: 250px;
        }

        .card-text {
            height: 70px;
        }


        .form-control.quantity{
            max-width: 50px;
        }

        .card-with-bottom-fixed {
            position: relative;
        }

        .card-with-bottom-fixed .fixed-bottom {
            position: absolute;
            padding: 20px 20px 20px;
        }



    </style>
</head>
<body style="background-color: #FFE8A3" class="p-16">

<jsp:include page="./header.jsp" />

<div class="container">
    <div style="background-color: #91263A; padding:10px" class="row">

        <c:forEach items="${deals}" var = "product">
            <c:set var="quantity" value="${basketService.getQuantityInBasketById(product.getId())}"/>
            <div class="col-xl-4 col-lg-6 col-md-6 col-sm-12">
                <div class="card mb-4 box-shadow card-with-bottom-fixed">
                    <img class="card-img-top" src=${product.getImage()} alt="Product">
                    <div class="card-body">
                        <h5 class="card-title">${product.getName()}
                            <c:if test = "${sales.containsKey(product.getId())}">
                            <span class="text-muted ml-2">${sales.get(product.getId())} sales</span>
                                </c:if>
                            <c:if test = "${!sales.containsKey(product.getId())}">
                                <span class="text-muted ml-2">0 sales</span>
                            </c:if>

                            <c:if test = "${bestSellers.contains(product.getId())}">
                                <br>
                                <span style = "font-size: 14px;" class="badge badge-success">Best Selling Deal!</span>
                            </c:if>
                            <c:if test = "${topCategory.getId() == product.getCategoryId()}">
                                <br>
                                <span style = "font-size: 14px;" class="badge badge-warning">Most Searched Category! (${topCategory.getName()})</span>
                            </c:if>

                        </h5>
                        <p class="card-text">${product.getDescription()}
                            <c:if test = "${discountIds.contains(product.getId())}">
                                <br>
                                <span class = "text-danger" style = "text-align: center" class="fw-bold me-2">${String.format("%.0f", discounts.get(product.getId()))}% off!</span>
                            </c:if>
                        </p>



                        <div class="d-flex justify-content-between align-items-center fixed-bottom">
                            <a href="#" class="btn btn-outline-secondary favorites-btn" id="fav-${product.getId()}">
                                <c:if test = "${favourites.contains(product.getId())}">
                                    <i class="bi bi-heart-fill text-danger"></i>
                                </c:if>
                                <c:if test = "${!favourites.contains(product.getId())}">
                                    <i class="bi bi-heart"></i>
                                </c:if>
                            </a>
                                <%-- Prevents redirect --%>
                            <iframe name="dummyframe" method="post" id="dummyframe" style="display: none;"></iframe>
                            <form class="add-to-cart-form">
                                <c:if test = "${quantity == 0}">
                                    <input type="hidden" name="productId" value="${product.getId()}">
                                    <input id = "${product.getId()}" class="btn btn-sm btn-outline-secondary add-to-cart-btn" type="button" value="Add to cart" />
                                    <div class="input-group quantity-input" style="display:none;">
                                        <span class="input-group-btn">
                                            <button id = "${product.getId()}" type="button" class="btn btn-outline-secondary decrement-btn">-</button>
                                        </span>
                                        <input type="number" class="form-control quantity" name="quantity" value="${quantity + 1}" min="1" max="10">
                                        <span class="input-group-btn">
                                            <button  id = "${product.getId()}" type="button" class="btn btn-outline-secondary increment-btn">+</button>
                                        </span>
                                    </div>
                                </c:if>
                                <c:if test = "${quantity != 0}">
                                    <input type="hidden" name="productId" value="${product.getId()}">
                                    <input id = "${product.getId()}" class="btn btn-sm btn-outline-secondary add-to-cart-btn" style="display:none;" type="button" value="Add to cart" />
                                    <div class="input-group quantity-input">
                                        <span class="input-group-btn">
                                            <button id = "${product.getId()}" type="button" class="btn btn-outline-secondary decrement-btn">-</button>
                                        </span>
                                        <input type="number" class="form-control quantity" name="quantity" value="${quantity}" min="1" max="10">
                                        <span class="input-group-btn">
                                            <button  id = "${product.getId()}" type="button" class="btn btn-outline-secondary increment-btn">+</button>
                                        </span>
                                    </div>

                                </c:if>

                            </form>
                            <c:if test = "${discountIds.contains(product.getId())}">
                                <div class="d-flex align-items-center">
                                    <del class="text-muted me-2 mr-2">$${String.format("%.2f", (product.getPrice()/(1-(discounts.get(product.getId())/100))))}</del>
                                    <span class="text-danger fw-bold">  $${product.getPrice()}</span>
                                </div>
                            </c:if>

                            <c:if test = "${!discountIds.contains(product.getId())}">
                                <div class="d-flex align-items-center">
                                    <span class="fw-bold">  $${product.getPrice()}</span>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="modal fade" id="remove-from-favorites-modal" tabindex="-1" role="dialog" aria-labelledby="remove-from-favorites-modal-title" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="remove-from-favorites-modal-title">Remove from Favorites</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    Are you sure you want to remove this product from your favorites?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" id="remove-from-favorites-confirm-btn">Remove</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
        src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script
        src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>



<%--HANDLING DYNAMIC ICNREMENT/DECREMENT FEATURE: --%>
<script>
    $(document).ready(function() {
        $('.add-to-cart-btn').click(function() {
            let productId = $(this).attr('id');
            console.log(${basketService.getQuantityInBasketById(product.getId()) != 0});
            $.ajax({
                type: 'GET',
                url: '/basket/addProduct/' + productId,
                success: function (data) {
                    console.log('AJAX response:', data);
                    $('.quantity-increment').show();
                    $('#${productId}').hide();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('AJAX error:', errorThrown);
                }
            });
            $(this).hide();
            $(this).siblings('.quantity-input').show();
        });

        $('.increment-btn').click(function() {
            let productId = $(this).attr('id');
            let quantityInput = $(this).closest('.quantity-input').find('.quantity');
            let currentQuantity = parseInt(quantityInput.val());
            if (currentQuantity < 10) {
                quantityInput.val(currentQuantity + 1);
                $.ajax({
                    type: 'GET',
                    url: '/basket/addProduct/' + productId,
                    success: function (data) {
                        console.log('AJAX response:', data);
                        $('.quantity-increment').show();
                        $('#${productId}').hide();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('AJAX error:', errorThrown);
                    }
                });

            }
        });

        $('.decrement-btn').click(function() {
            let productId = $(this).attr('id');
            let quantityInput = $(this).closest('.quantity-input').find('.quantity');
            let currentQuantity = parseInt(quantityInput.val());
            if (isNaN(currentQuantity)) {
                currentQuantity = 0;
            }
            $.ajax({
                type: 'GET',
                url: '/basket/removeProduct/' + productId
            });
            if (currentQuantity > 1){
                quantityInput.val(currentQuantity - 1);
                // Show quantity input only if quantity is greater than 1
                $(this).closest('.quantity-input').toggle(currentQuantity > 1);
            } else {
                // Show add to cart button if quantity is 1
                $(this).closest('.quantity-input').hide();
                $(this).closest('.card-body').find('.add-to-cart-btn').show();
            }
        });
    });
</script>

<script>
    $(document).ready(function() {
        // Favorites button click event
        $(".favorites-btn").click(function(e) {
            e.preventDefault();
            const productId = $(this).attr("id").split("-")[1];
            const isFavorite = $(this).find(".bi").hasClass("bi-heart-fill");
            if (isFavorite) {
                // Product is already in favorites, show modal popup
                $("#remove-from-favorites-modal").modal("show");
                $("#remove-from-favorites-confirm-btn").off("click").on("click", function() {
                    // User confirmed removal, proceed with post request
                    $.post('/favourites/add/' + productId, function(response) {
                        location.reload();
                    });
                });
            } else {
                // Product is not in favorites, proceed with post request
                $.post('/favourites/add/' + productId, function(response) {
                    location.reload();
                });
            }
        });
    });
</script>

</body>
</html>
