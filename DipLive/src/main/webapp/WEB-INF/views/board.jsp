<%@include file="head.jsp"%>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
${svg}
<c:if test="${member_of_game and started}">
<div id="order-type" class="btn-group">
	<button id="order-move" class="btn active">Move</button>
	<button id="order-hold" class="btn">Hold</button>
	<button id="order-shold" class="btn">Support Hold</button>
	<button id="order-smove" class="btn">Support Move</button>
	<button id="order-convoy" class="btn">Convoy</button>
</div>
<div id="bottom-bar"></div>
</c:if>
<div>
<c:forEach items="${players}" var="player">
<h3>${player.user.username}</h3><p>${player.power}</p> 
</c:forEach>
</div>
<c:if test="${member_of_game and started}">
<script>
		var from = 0;
		var order = {"type" : "order-move"};
		$("#MouseLayer > *").hover(
						function() {
							$("#bottom-bar").html("<p>Hovering Over " + $(this).attr("id") + " </p>");
						}, function() {
							$("#bottom-bar").html("");
						}
		);
		$("#order-type > *").click(function() {
			$("#order-type > *").removeClass("active");
			$(this).addClass("active");
			order={"type" : $(this).attr("id")};
			from = 0;
		});
		$("#MouseLayer > *").click(function() {
			var send = function(){
				$.ajax("${gid}/JSONorder", {
					data : JSON.stringify(order),
					contentType : 'application/json',
					type : 'POST',
					success : function(msg) {
						alert(msg["success"]);
					}
				});
			}
			if (!order.hasOwnProperty("loc")) {
				order["loc"] = $(this).attr("id");
				if(order["type"] == "order-hold"){
					send();
					order={"type" : order["type"]};
				}
			} else {
				if (!order.hasOwnProperty("loc1")){
					order["loc1"] = $(this).attr("id");
					if (order["type"] == "order-shold" || order["type"] == "order-move"){
						send();
						order={"type" : order["type"]};
					}
				}else{
					order["loc2"] = $(this).attr("id");
					send();
					order={"type" : order["type"]};
				}
			}
		});
	</script>
</c:if>
<%@include file="tail.jsp"%>