<h4><a href="/dip/game/${game.game.id}">${game.game.name}</a> ${game.game.phase}</h4>
<p>${game.game.w.nonTurnData['_variant_info_'].variantName}</p>
<c:forEach items="${game.game.players}" var="player">
<p style = "color:blue">${player.user.username} ${player.power}</p>
</c:forEach>