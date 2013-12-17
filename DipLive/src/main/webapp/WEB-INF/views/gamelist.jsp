<%@include file="head.jsp"%>
<c:forEach items="${games}" var="game">
	<%@include file="gamesummary.jsp"%>
</c:forEach>
<p><a href="?p=${page+1}">Next Page</a></p>
<%@include file="tail.jsp"%>