<%@include file="head.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h3>Welcome to Diplomacy (Beta)</h3>
<c:forEach items="${games}" var="uge">
	<c:set var="game" value="${uge.game}" />
	<c:if test="${uge.unread}"><p>New Message</p></c:if>
	<c:if test="${not uge.ready}"><p>Orders Needed</p></c:if>
	<%@include file="gamesummary.jsp"%>
</c:forEach>
<img style="display: block; max-width: 100%;"
	src="resources/img/tank.jpg" />
<%@include file="tail.jsp"%>