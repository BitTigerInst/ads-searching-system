<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="ISO-8859-1">
  <title>AJAX with Servlets using AngularJS</title>
  <script type="text/javascript" src="resources/angular-1.5.7/angular.js"></script>
  <script type="text/javascript" src="resources/angular-1.5.7/angular.min.js"></script>
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
          $scope.message = response.data;
        }, function(response) {
          //fail case
          console.log(response);
          $scope.message = response;
        });

      };
    } ]);
  </script>

</head>
<body>
<div ng-app= "myApp" ng-controller="myCtrl">
  <form ng-submit="inputQuery()">
    <input ng-model="query" />
    <button type="submit">Search</button>
  </form>
  <p> {{message}}</p>
</div>
</body>
</html>
