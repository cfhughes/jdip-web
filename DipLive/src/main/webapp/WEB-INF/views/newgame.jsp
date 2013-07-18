<%@include file="head.jsp" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<form:form action="savegame" commandName="game">
<label for="name">Game Name:</label><form:input path="name" id="name" type="text"/><br/>
<label for="variant">Variant</label><select id="variant" name="variant">
<c:forEach items="${variants}" var="variant">
<option value="${variant.name}">${variant.name}
( <c:forEach items="${variant.powers}" var="power">
${power.name}
</c:forEach>)
</option>
</c:forEach>
</select><br/>
<button type="submit">Submit</button>
</form:form>
<%@include file="tail.jsp" %>