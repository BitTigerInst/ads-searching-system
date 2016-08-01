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


</head>
<body ng-app= "myApp">
<div ng-controller="myCtrl">
    <div class="container">
        <div class="header clearfix">
            <nav>
                <ul class="nav nav-pills pull-right">
                    <li role="presentation" ><a href="index.jsp">Home</a></li>
                    <li role="presentation" ><a href="about.jsp">About</a></li>
                    <li role="presentation" class="active"><a href="#">Contact</a></li>
                </ul>
            </nav>
            <h3 class="text-muted" align="left">OUR TEAM: Monkey-Year-Horse-Month</h3>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <img src = "resources/img/ljl.png" alt="Jack">
                <h3 class="panel-title"><strong>Jialiang(Jack) Li</strong></h3>
            </div>
            <div class="panel-body">
                <img src="resources/img/linkedin.png" alt="LinkedIn"> <a href="https://www.linkedin.com/in/lijialiang">https://www.linkedin.com/in/lijialiang</a><br><br>
                <img src="resources/img/github.ico" alt="Github"> <a href="https://github.com/lijljacklee">https://github.com/lijljacklee</a>
            </div>
        </div>


        <div class="panel panel-info">
            <div class="panel-heading">
                <img src = "resources/img/xt.png" alt="Tony">
                <h3 class="panel-title"><strong>Tao(Tony) Xu</strong></h3>
            </div>
            <div class="panel-body">
              <img src="resources/img/linkedin.png" alt="LinkedIn"> <a href="http://linkedin.com/in/tao-xu-ba469024">http://linkedin.com/in/tao-xu-ba469024</a><br><br>
              <img src="resources/img/github.ico" alt="Github"> <a href="https://github.com/dojiangv">https://github.com/dojiangv</a>
            </div>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <img src = "resources/img/jyh.png" alt="Jackie">
                <h3 class="panel-title"><strong>Jingyun(Jackie) Hu</strong></h3>
            </div>
            <div class="panel-body">
                <img src="resources/img/linkedin.png" alt="LinkedIn"> <a href="https://www.linkedin.com/in/jingyunhu">https://www.linkedin.com/in/jingyunhu</a><br><br>
                <img src="resources/img/github.ico" alt="Github"> <a href="https://github.com/sleephu">https://github.com/sleephu</a>
            </div>
        </div>

        <footer class="footer">
            <p>@2016 Monkey-Year-Horse-Month</p>
        </footer>

    </div> <!-- /container -->
</div>
</body>
</html>
