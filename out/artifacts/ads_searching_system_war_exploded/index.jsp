<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="ISO-8859-1">
  <title>Ads Searching System</title>
  <script type="text/javascript" src="resources/angular-1.5.7/angular.js"></script>
  <script type="text/javascript" src="resources/angular-1.5.7/angular.min.js"></script>
  <link rel="stylesheet" type="text/css" href="resources/css/theme.css">
  <!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
  <!-- Optional theme -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
  <script>
    // send query
    var app = angular.module("myApp", []);

    app.controller("myCtrl", [ '$scope', '$http', function($scope, $http) {

      $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

      $scope.inputQuery = function() {
        $http({
          url : 'search',
          method : "POST",
          data : {
            'query' : $scope.query
          }
        }).then(function(response) {
          console.log(response.data);
 //         alert("success!~"+response.data);
          $scope.ads = response.data;
//          $scope.mainlineAds = response.data.mainline;
//          $scope.sidebarAds = response.data.bar;
        }, function(response) {
          //fail case
//          alert("fail!~"+response.data);
          console.log(response);
          $scope.message = response;
        })
      };
    } ]);

  </script>

</head>
<body ng-app= "myApp">
<div ng-controller="myCtrl">
<div class="container">
  <div class="header clearfix">
    <nav>
      <ul class="nav nav-pills pull-right">
        <li role="presentation" class="active"><a href="#">Home</a></li>
        <li role="presentation"><a href="#">About</a></li>
        <li role="presentation"><a href="#">Contact</a></li>
      </ul>
    </nav>
    <h3 class="text-muted" align="left">Ads Searching System</h3>
    <div class="row">
      <div class="col-sm-6 col-sm-offset-3">
        <div id="imaginary_container">
          <div class="input-group stylish-input-group" >
            <input type="text" class="form-control"  placeholder="Search" ng-model="query">
                    <span class="input-group-addon">
                        <button type="submit" ng-click="inputQuery()">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </span>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="alert alert-success" role="alert" ng-if="!message">{{ads}}</div>
  <div class="alert alert-warning" role="alert" ng-if="message">{{message}}</div>

  <%--<div class="row marketing">--%>
    <%--<div class="col-lg-6">--%>
      <%--&lt;%&ndash;ad in mainline&ndash;%&gt;--%>
      <%--<h4>Ad1</h4>--%>
      <%--<p>Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum.</p>--%>

      <%--<h4>Ad2</h4>--%>
      <%--<p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras mattis consectetur purus sit amet fermentum.</p>--%>

      <%--<h4>Ad3</h4>--%>
      <%--<p>Maecenas sed diam eget risus varius blandit sit amet non magna.</p>--%>
    <%--</div>--%>

    <%--<div class="col-lg-6">--%>
      <%--&lt;%&ndash;ad in sidebar&ndash;%&gt;--%>
      <%--<h4>Ad1</h4>--%>
      <%--<p>Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum.</p>--%>

      <%--<h4>Ad2</h4>--%>
      <%--<p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras mattis consectetur purus sit amet fermentum.</p>--%>

      <%--<h4>Ad3</h4>--%>
      <%--<p>Maecenas sed diam eget risus varius blandit sit amet non magna.</p>--%>
    <%--</div>--%>
  <%--</div>--%>

  <footer class="footer">
    <p>&copy; 2016 ads-searching, Inc.</p>
  </footer>

</div> <!-- /container -->
</div>
</body>
</html>
