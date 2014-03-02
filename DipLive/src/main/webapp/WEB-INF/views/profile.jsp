<%@include file="head.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h3>${user.username}</h3>
<p>Wins: ${user.wins}</p>
<p>Losses: ${user.losses}</p>
<p>Games Abandoned: ${user.retreats}</p>
<h4>Games:</h4>
<c:forEach items="${user.games}" var="game">
	<c:set var="game" value="${game.game}" />
	<%@include file="gamesummary.jsp"%>
</c:forEach>
<%@include file="tail.jsp"%>