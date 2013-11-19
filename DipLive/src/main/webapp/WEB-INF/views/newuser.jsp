<%@include file="head.jsp"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

	<form id="fb_signin" action="<c:url value="/signin/facebook"/>"
		method="POST">
		<div id="fb-root"></div>
		<input type="image" src="<c:url value="/resources/img/fb.gif"/>"
			name="submit" alt="Signin with Facebook" />
	</form>


	<form id="google_signin" action="<c:url value="/signin/google"/>"
		method="POST">
		<input type="image" src="<c:url value="/resources/img/gg.gif"/>"
			name="submit" alt="Signin with Google" />
	</form>

<form:form class="form" action="saveuser" commandName="user">
	<label for="uname">User Name:</label>
	<form:input class="form-control" path="username" id="uname" type="text" />
	<br />
	<label for="pword">Password:</label>
	<form:input class="form-control" path="password" id="pword"
		type="password" />
	<br />
	<label for="email">Email:</label>
	<form:input class="form-control" path="email" id="email" type="text" />
	<br />
	<button type="submit">Submit</button>
</form:form>
<%@include file="tail.jsp"%>