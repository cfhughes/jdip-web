<%@include file="head.jsp"%>
<style type="text/css">
.chat-input{padding:5px 2px;margin:5px;border:1px solid #bbb;width:335px;float:left}
.chat-messages{max-height:200px;overflow-y:auto}
</style>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
${svg}
<br/>
<c:if test="${member_of_game and started}">
<div id="order-type" class="btn-group">
	<button id="order-move" class="btn active">Move</button>
	<button id="order-hold" class="btn">Hold</button>
	<button id="order-shold" class="btn">Support Hold</button>
	<button id="order-smove" class="btn">Support Move</button>
	<button id="order-convoy" class="btn">Convoy</button>
</div>
<div style="height:20px" id="bottom-bar"></div>
</c:if>
<div>
<ul class="nav nav-tabs">
<c:forEach items="${players}" var="player">
<li><a href="#${player.power}" data-toggle="tab">${player.user.username}(${player.power})</a></li>

</c:forEach>
</ul>
<div class="tab-content">
<c:forEach items="${players}" var="player">
<div class="tab-pane" id="${player.power}">
<form>
<textarea class="chat-input" rows="3"></textarea><button class="dipchat-submit" type="submit">Send</button>
</form>
<h3>${player.user.username}</h3><p>${player.power}</p> 
</div>
</c:forEach>
</div>
</div>
<c:if test="${member_of_game and started}">
<script>
		$(".dipchat-submit").click(function(){
			$.ajax("JSONchat", {
				success : function(msg) {
					alert(msg);	
				}
			});
		});
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
						for (var layer in msg["orders"]){
							var $element = document.importNode(new DOMParser().parseFromString(msg["orders"][layer], "image/svg+xml").documentElement,true);
							$("#Layer1 > #"+layer).append($element);
						}
						console.log(msg);
					}
				});
			};
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