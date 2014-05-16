<div class="well well-sm">
	<h4>
		<a href='<c:url value="/game/${game.id}" />'>${game.name}</a>
		${game.phase}
		<c:if test="${unread}"><span class="label label-success">New Message!</span></c:if>
		<c:if test="${needorders}"><span class="label label-primary">Orders Needed</span></c:if>
	</h4>
	<p>${game.w.nonTurnData['_variant_info_'].variantName}</p>
	<c:forEach items="${game.players}" var="player">
		<p style="color: blue">
			<a href="<c:url value="/player/${player.user.id}" />">${player.user.username}</a>
			${player.power}
		</p>
	</c:forEach>
</div>