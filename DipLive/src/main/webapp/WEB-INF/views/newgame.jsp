<%@include file="head.jsp" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<form:form action="savegame" commandName="game">
<label for="name">Game Name:</label><form:input path="name" id="name" type="text"/><br>
<button type="submit">Submit</button>
</form:form>
<%@include file="tail.jsp" %>