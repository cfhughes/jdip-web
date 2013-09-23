<%@include file="head.jsp" %>
<c:forEach items="${games}" var="game">
<p><a href="game/${game.id}">${game.name}</a></p>
</c:forEach>
<%@include file="tail.jsp" %>