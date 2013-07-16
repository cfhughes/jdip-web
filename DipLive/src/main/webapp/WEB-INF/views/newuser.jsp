<%@include file="head.jsp" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<form:form action="saveuser" commandName="user">
<label for="uname">User Name:</label><form:input path="username" id="uname" type="text"/><br>
<label for="pword">Password:</label><form:input path="password" id="pword" type="text"/><br>
<button type="submit">Submit</button>
</form:form>
<%@include file="tail.jsp" %>