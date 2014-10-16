<%@include file="head.jsp"%>

	<div class="col-md-4">
		<div>
			<h2>Join a Tournament Game</h2>
			<a href="<c:url value="/tournamentjoin" />"><button type="button"
					class="btn btn-primary">Join</button></a>
		</div>
	</div>
	<div class="col-md-4">
		<div>
			<h2>Your Level</h2>
			<h1>${user.level}</h1>
		</div>
	</div>

<%@include file="tail.jsp"%>