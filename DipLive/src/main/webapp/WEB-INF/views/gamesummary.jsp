<div class="well well-small">
<h4><a href="/dip/game/${game.id}">${game.name}</a> ${game.phase}</h4>
<p>${game.w.nonTurnData['_variant_info_'].variantName}</p>
<c:forEach items="${game.players}" var="player">
<p style = "color:blue">${player.user.username} ${player.power}</p>
</c:forEach>
</div>