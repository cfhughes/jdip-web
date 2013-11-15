<%@include file="head.jsp"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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