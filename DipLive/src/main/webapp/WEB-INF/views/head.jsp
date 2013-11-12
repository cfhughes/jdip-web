<%@ page language="java" contentType="application/xhtml+xml; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<title>Diplomacy</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="description" content="" />
<meta name="author" content="" />

<script src="//code.jquery.com/jquery-2.0.3.min.js"
	type="text/javascript"></script>
<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"
	rel="stylesheet" />
<style type="text/css">

.container {
padding: 30px;
}

.navbar {
margin-bottom: 30px;
}

</style>

</head>

<body>

	<div class="container"
		style="background-color: #E3F2E3; padding-left: 10px; padding-right: 10px;">



		<nav class="navbar navbar-default" role="navigation">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-ex1-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="<c:url value="/" />">Diplomacy</a>
			</div>
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav">
				<sec:authorize access="hasRole('PLAYER')">
					<li><a href="<c:url value="/forum" />">Forum</a></li>
					</sec:authorize>
					<li><a href="<c:url value="/gamelist" />">All Games</a></li>
					<sec:authorize access="hasRole('PLAYER')">
						<li><a href="<c:url value="/newgame" />">New Game</a></li>
					</sec:authorize>
					<li><a href="<c:url value="/help" />">Help</a></li>
				</ul>
				<div class="pull-right">
					<sec:authorize access="isAuthenticated()"> 
					<p class="navbar-text">
				User: <sec:authentication property="principal.username" /></p>
						<a href="<c:url value="/j_spring_security_logout" />"><button class="btn btn-default navbar-btn">Logout</button></a>
					</sec:authorize>

					<sec:authorize access="!isAuthenticated()">
						<a href="<c:url value="/login" />"><button class="btn btn-default navbar-btn">Login</button></a>
						<a href="<c:url value="/newuser" />"><button class="btn btn-default navbar-btn">Register</button></a>
					</sec:authorize>
				</div>
			</div>
		</nav>
		<!-- /.navbar -->

		<div class="row-fluid">