<%@include file="head.jsp"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form class="form" action="updateuser" modelAttribute="user">
	<label for="username">User Name:</label>
	<form:input class="form-control" path="username" id="uname" type="text" />
	<br />
	<label for="pass">Password:</label>
	<input class="form-control" id="pass" name="pass" type="password" />
	<br />
	<label for="email">Email:</label>
	<form:input class="form-control" path="email" id="email" type="text" />
	<br />
	<button type="submit">Submit</button>
</form:form>
<%@include file="tail.jsp"%>