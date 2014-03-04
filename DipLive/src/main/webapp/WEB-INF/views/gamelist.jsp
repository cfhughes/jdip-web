<%@include file="head.jsp"%>
<ul id="tabs" class="nav nav-tabs">
	<li <c:if test="${empty joinable}">class="active"</c:if>><a href="<c:url value="/gamelist" />">All</a></li>
	<li <c:if test="${joinable == 1}">class="active"</c:if>><a href="<c:url value="/gamelist?joinable=1" />">Need Players</a></li>
</ul>
<c:forEach items="${games}" var="game">
	<%@include file="gamesummary.jsp"%>
</c:forEach>
<p><a href="?p=${page+1}<c:if test="${joinable == 1}">&joinable=1</c:if>">Next Page</a></p>
<%@include file="tail.jsp"%>