<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Diplomacy</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<!-- Le styles -->
<link
	href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css"
	rel="stylesheet">
<style type="text/css">
body {
	padding-top: 20px;
	padding-bottom: 60px;
}

.row-fluid {
	max-width: 100%;
}

/* Custom container */
.container {
	margin: 0 auto;
	max-width: 1300px;
}

.container>hr {
	margin: 60px 0;
}

/* Main marketing message and sign up button */
.jumbotron {
	margin: 80px 0;
	text-align: center;
}

.jumbotron h1 {
	font-size: 100px;
	line-height: 1;
}

.jumbotron .lead {
	font-size: 24px;
	line-height: 1.25;
}

.jumbotron .btn {
	font-size: 21px;
	padding: 14px 24px;
}

/* Supporting marketing content */
.marketing {
	margin: 60px 0;
}

.marketing p+h4 {
	margin-top: 28px;
}

#MouseLayer {
	visibility: visible;
}

/* Customize the navbar links to be fill the entire space of the .navbar */
/*.navbar .navbar-inner { 
	padding: 0; 
} 

.navbar .nav {
	margin: 0;
	display: table;
	width: 100%;
}

.navbar .nav li {
	display: table-cell;
	width: 1%;
	float: none;
}

.navbar .nav li a {
	font-weight: bold;
	text-align: center;
	border-left: 1px solid rgba(255, 255, 255, .75);
	border-right: 1px solid rgba(0, 0, 0, .1);
}

.navbar .nav li:first-child a {
	border-left: 0;
	border-radius: 3px 0 0 3px;
}

.navbar .nav li:last-child a {
	border-right: 0;
	border-radius: 0 3px 3px 0;
}*/
</style>

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="../assets/js/html5shiv.js"></script>
    <![endif]-->

</head>

<body>

	<div class="container"
		style="background-color: #E3F2E3; min-height: 100%; padding-left: 10px; padding-right: 10px;">

		<div class="masthead">
			<p class="pull-right">
				<sec:authorize access="isAuthenticated()"> 
				User: <sec:authentication property="principal.username" />
					<a href="<c:url value="/j_spring_security_logout" context="/dip"/>">
						Logout</a>
				</sec:authorize>

				<sec:authorize access="!isAuthenticated()">
					<a href="<c:url value="/login" context="/dip"/>"><button>Login</button></a>
					<a href="<c:url value="/newuser" context="/dip"/>"><button>Register</button></a>
				</sec:authorize>
			</p>
			<h3 class="muted">Diplomacy</h3>
			<div class="navbar">
				<div class="navbar-inner">
					<div class="container">
						<button type="button" class="btn btn-navbar"
							data-toggle="collapse" data-target=".nav-collapse">
							<span class="icon-bar"></span> <span class="icon-bar"></span> <span
								class="icon-bar"></span>
						</button>
						<div class="nav-collapse collapse">
							<ul class="nav">
								<li><a href="<c:url value="/ " context="/dip"/>">Home</a></li>
								<li><a href="<c:url value="/gamelist" context="/dip"/>">All
										Games</a></li>
								<li><a href="<c:url value="/newgame" context="/dip"/>">New
										Game</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<!-- /.navbar -->
		</div>

		<div class="row-fluid">