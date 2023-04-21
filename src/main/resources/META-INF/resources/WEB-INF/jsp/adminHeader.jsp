<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<!-- jQuery UI library -->
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>

<!-- Bootstrap CSS -->
<link rel="stylesheet"
      href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
      integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
      crossorigin="anonymous">

<nav style="background-color: #F2CD5C; text-align: center" class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <div>
            <a class="navbar-brand" href="/adminhome" style="height: auto;"> <img
                    th:src="/images/ConcordiaEats_logo.svg" src="/images/ConcordiaEats_logo.svg"
                    width="auto" height="60" class="d-inline-block align-top" alt=""/>
            </a>
        </div>
        <button class="navbar-toggler" type="button" data-toggle="collapse"
                data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false"
                aria-label="Toggle navigation" style=" background-color: #91263A;">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent" style="justify-content: right;">
            <ul class="navbar-nav">

                <li class="nav-item active"><a class="nav-link" href="/logout" style="color: #91263A;">
                    <div><img
                            th:src="@{/images/logo.png}" src="../images/logout_icon.png"
                            width="auto" height="40" class="d-inline-block align-top" alt=""/></div>
                    <div><strong>Logout</strong></div>
                </a>
                </li>

            </ul>

        </div>
    </div>
</nav>
<br>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script
        src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>

<script>
    $(document).ready(function () {
        $("#searchInput").autocomplete({
            source: function (request, response) {
                // send an AJAX request to fetch the search results
                $.ajax({
                    url: "/search",
                    type: "GET",
                    data: {query: request.term},
                    dataType: "json",
                    success: function (data) {
                        console.log("Data: " + data)
                        response($.map(data, function (item) {
                            return {
                                label: item.label,
                                value: item.value
                            };
                        }));
                    }
                });
            },
            minLength: 3 // set the minimum number of characters before autocomplete starts
        });
    });
</script>
