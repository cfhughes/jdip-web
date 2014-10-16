<%@include file="head.jsp"%>
<p>You've joined a tournament game. You may have to wait for other players to join.</p>
<a href="<c:url value="/game/${id}" />"><button type="button" class="btn btn-primary">Enter The Game</button></a>
<%@include file="tail.jsp"%>