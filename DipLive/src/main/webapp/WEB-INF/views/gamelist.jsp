<%@include file="head.jsp" %>
<c:forEach items="${games}" var="game">
<%@include file="gamesummary.jsp" %>
</c:forEach>
<%@include file="tail.jsp" %>