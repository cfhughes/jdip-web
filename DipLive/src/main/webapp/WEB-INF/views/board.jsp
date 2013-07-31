<%@include file="head.jsp"%>
<style type="text/css">
.chat-input {
	padding: 5px 2px;
	margin: 5px;
	border: 1px solid #bbb;
	width: 335px;
	float: left
}

.chat-messages {
	max-height: 200px;
	overflow-y: auto
}
</style>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"
	type="text/javascript"></script>
${svg}
<br />
<c:if test="${member_of_game and started}">
	<div id="order-type" class="btn-group">
		<button id="order-move" class="btn active">Move</button>
		<button id="order-hold" class="btn">Hold</button>
		<button id="order-shold" class="btn">Support Hold</button>
		<button id="order-smove" class="btn">Support Move</button>
		<button id="order-convoy" class="btn">Convoy</button>
	</div>
	<div style="height: 20px" id="bottom-bar"></div>
	<div>
		<ul id="chat-tabs" class="nav nav-tabs">
			<c:forEach items="${players}" var="player">
				<li chatid="${player.id}"><a href="#${player.power}" data-toggle="tab">${player.user.username}(${player.power})</a></li>

			</c:forEach>
		</ul>
		<div class="tab-content">
			<c:forEach items="${players}" var="player">
				<div class="tab-pane" id="${player.power}">
					<textarea id="chat-${player.id}" name="${player.id}"
						class="chat-input" rows="3" cols=""></textarea>
					<button userid="${player.id}" class="dipchat-submit" type="button">Send</button>
					<br>
					<h3>${player.user.username}</h3>
					<p>${player.power}</p>
				</div>
			</c:forEach>
		</div>
	</div>
</c:if>
<c:if test="${member_of_game and started}">
<script type="text/javascript">
$(function() {
var loadchat = function(from,last){
	var request = {"fromid" : from, "gameid" : "${gid}","lastseen" : last};
	alert(JSON.stringify(request));
	$.ajax("JSONmessages", {
		data : JSON.stringify(request),
		contentType : 'application/json',
		type : 'POST',
		success : function(msg) {
			alert(msg);
		}
	});
};
$("#chat-tabs > li").click(function(){
	loadchat($(this).attr("chatid"),0);
});
loadchat($("#chat-tabs > li:first-child").attr("chatid"),0);
$(".dipchat-submit").click(function(){
	var chat = {"gameid" : "${gid}","to" : $(this).attr("userid"), "message" : $("#chat-"+$(this).attr("userid")).val()};
	$.ajax("JSONchat", {
		data : JSON.stringify(chat),
		contentType : 'application/json',
		type : 'POST',
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
});
</script>
</c:if>
<%@include file="tail.jsp"%>