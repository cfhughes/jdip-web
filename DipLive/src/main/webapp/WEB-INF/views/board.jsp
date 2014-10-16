<%@include file="head.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript"
	src="<c:url value="/resources/jquery.localtime-0.8.0.min.js" />"></script>
<style type="text/css">
.chatcontainer {
	position: relative;
	padding: 10px;
	margin: 1em 0 3em;
	color: #000;
	background: #f3961c;
	/* default background for browsers without gradient support */
	/* css3 */
	background: -webkit-gradient(linear, 0 0, 0 100%, from(#f9d835),
		to(#f3961c));
	background: -moz-linear-gradient(#f9d835, #f3961c);
	background: -o-linear-gradient(#f9d835, #f3961c);
	background: linear-gradient(#f9d835, #f3961c);
	-webkit-border-radius: 10px;
	-moz-border-radius: 10px;
	border-radius: 10px;
	max-width: 500px;
}

/* Variant : for left/right positioned triangle */
.chatcontainer.chat-left {
	margin-left: 50px;
}

/* Variant : for right positioned triangle
------------------------------------------ */
.chatcontainer.chat-right {
	margin-right: 50px;
}

/* THE TRIANGLE
------------------------------------------------------------------------------------------------------------------------------- */

/* creates triangle */
.chatcontainer:after {
	content: "";
	position: absolute;
	bottom: -15px; /* value = - border-top-width - border-bottom-width */
	left: 50px; /* controls horizontal position */
	border-width: 15px 15px 0;
	/* vary these values to change the angle of the vertex */
	border-style: solid;
	border-color: #f3961c transparent;
	/* reduce the damage in FF3.0 */
	display: block;
	width: 0;
}

/* Variant : left
------------------------------------------ */
.chatcontainer.chat-left:after {
	top: 16px; /* controls vertical position */
	left: -50px; /* value = - border-left-width - border-right-width */
	bottom: auto;
	border-width: 10px 50px 10px 0;
	border-color: transparent #f3961c;
}

/* Variant : right
------------------------------------------ */
.chatcontainer.chat-right:after {
	top: 16px; /* controls vertical position */
	right: -50px; /* value = - border-left-width - border-right-width */
	bottom: auto;
	left: auto;
	border-width: 10px 0 10px 50px;
	border-color: transparent #f3961c;
}

.chat-me:after {
	border-color: #075698 transparent;
}

.chat-me {
	background: #075698;
	/* css3 */
	background: -webkit-gradient(linear, 0 0, 0 100%, from(#2e88c4),
		to(#075698));
	background: -moz-linear-gradient(#2e88c4, #075698);
	background: -o-linear-gradient(#2e88c4, #075698);
	background: linear-gradient(#2e88c4, #075698);
}

.chat-input {
	padding: 5px 2px;
	margin: 5px;
	border: 1px solid #bbb;
	width: 335px;
	float: left;
}

.chat-messages {
	height: 200px;
	overflow: auto;
	border: 1px solid #000;
}

use,symbol {
	overflow: visible;
}

.invisible {
	visibility: visible;
}

.intangible {
	visibility: hidden;
}

svg:not(:root ) {
	overflow: visible;
}

svg:FIRST-CHILD {
	overflow: hidden;
}
</style>

<c:if test="${!member_of_game and !started and loggedin and !tournament}">
	<form action="<c:url value="/joingame/${gid}" />">
		Join this game:
		<c:if test="${gameprivate}">
			<input type="text" name="secret" placeholder="Password">
		</c:if>
		<button type="submit" class="btn btn-default">Join</button>
	</form>
</c:if>
<div id="svg-map">${svg}</div>
<div id="jpeg-map">
	<img id="map-image" src="" style="display: none;"></img>
</div>
<c:if test="${member_of_game and playing}">
	<div style="height: 20px" id="bottom-bar"></div>
	<div id="order-type" class="btn-group">
		<c:choose>
			<c:when test="${phasetype == 'M'}">

				<button id="order-move" class="btn btn-default">Move</button>
				<button id="order-hold" class="btn btn-default">Hold</button>
				<button id="order-shold" class="btn btn-default">Support
					Hold</button>
				<button id="order-smove" class="btn btn-default">Support
					Move</button>
				<button id="order-convoy" class="btn btn-default">Convoy</button>

			</c:when>
			<c:when test="${phasetype == 'R'}">
				<button id="order-retreat" class="btn btn-default">Retreat</button>
				<button id="order-disband" class="btn btn-default">Disband</button>
			</c:when>
			<c:when test="${phasetype == 'B'}">
				<button id="order-builda" class="btn btn-default">Build
					Army</button>
				<button id="order-buildf" class="btn btn-default">Build
					Fleet</button>
				<button id="order-destroy" class="btn btn-default">Disband</button>
			</c:when>
		</c:choose>
	</div>
	<button id="ready-button"
		class="btn btn-default<c:if test="${isready}"> active</c:if>">Ready</button>
	<img id="ready-img" src="<c:url value="/resources/img/check.png"/>"
		<c:if test="${!isready}">class="intangible"</c:if> />
	<h5><div style="margin-top:2px;margin-right:3px;float:left;height:10px;width:10px;background-color:${me.color}"></div>${me.power}
		- ${me.supply_centers}
		<c:if test="${not empty next}"> - Next Turn: <span
				data-localtime-format="dd MMM h:mm a"><fmt:formatDate
					pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" value="${next}" /></span>
		</c:if>
	</h5>
</c:if>

<h4>
	<span id="previous-phase" class="glyphicon glyphicon-backward"></span>
	<span id="phase-name">${gamephase}</span> <span id="next-phase"
		class="glyphicon glyphicon-forward"></span>
</h4>

<c:if test="${member_of_game}">
<c:if test="${playing}">
	<div id="orders-panel">
		Text Order Input:<input type="text" id="order_textinput" />
		<button id="ordertext_send" class="btn btn-default" type="btn">Go</button>
		<c:forEach items="${textorders}" var="order">
			<p id="${order.key}_text">
				<span class="label label-success">${order.value}</span>
				<button province_id="${order.key}" class="glyphicon glyphicon-trash"></button>
			</p>
		</c:forEach>
	</div>
	</c:if>
	<div class="row">
		<ul id="chat-tabs" class="nav nav-tabs">
			<li chatid="-1" class="active"><a href="#allchat"
				data-toggle="tab">All Players</a></li>
			<c:forEach items="${players}" var="player">
				<c:if test="${player.id != me_id}">
					<li chatid="${player.id}"><a id="a-${player.id}" href="#tab-${player.id}"
						data-toggle="tab"><div style="margin-top:5px;margin-right:3px;float:left;height:10px;width:10px;background-color:${player.color}"></div>${player.user.username}(${player.power}
							${player.supply_centers})<c:if test="${player.ready}">
								<img src="<c:url value="/resources/img/check.png"/>" />
							</c:if>
					</a></li>
				</c:if>
			</c:forEach>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="allchat">
				<div id="chatlog--1" class="chat-messages"></div>
				<textarea id="chat--1" name="-1"
					class="chat-input span4 form-control" cols="" rows="3"></textarea>
				<button class="dipchat-submit btn btn-default" type="btn"
					userid="-1">Send</button>

				<h4 class="pull-right">All Players</h4>
			</div>
			<c:forEach items="${players}" var="player">
				<c:if test="${player.id != me_id}">
					<div class="tab-pane" id="tab-${player.id}">
						<div id="chatlog-${player.id}" class="chat-messages"></div>
						<textarea id="chat-${player.id}" name="${player.id}"
							class="chat-input form-control span4" rows="3" cols=""></textarea>
						<button userid="${player.id}"
							class="dipchat-submit btn btn-default" type="btn">Send</button>
						<div class="pull-right">
							<p>
								<span style="font-weight: bold;">${player.user.username}</span>
								${player.power}
							</p>
						</div>
					</div>
				</c:if>
			</c:forEach>
		</div>

	</div>
	<div>
		<p>
			<a
				onclick="confirm('Are you sure you want to leave? Retreating from battle is frowned upon.');"
				href="../leavegame/${gid}"><span
				class="glyphicon glyphicon-remove-circle"></span> Leave This Game</a>
		</p>
	</div>
</c:if><c:if test="${!member_of_game}">
<div class="well well-small">
<%-- 	<h4>${game.name} - ${game.phase}</h4> --%>
<%-- 	<p>${game.w.nonTurnData['_variant_info_'].variantName}</p> --%>
	<c:forEach items="${players}" var="player">
		<p style="color: blue">
			<a href="<c:url value="/player/${player.user.id}" />">${player.user.username}</a>
			${player.power} <c:if test="${player.user.username == 'EMPTY'}"><a href="<c:url value="/joingame/${gid}?r=${player.id}" />">[Take Over]</a></c:if>
		</p>
	</c:forEach>
</div>
</c:if>
<script type="text/javascript">
	//<![CDATA[
	$(function() {
		if (!document.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "1.1")){
			$("#svg-map").html("<h3>Error: Your Browser doesn't support SVG, which is vital to this app.</h3>");
		}
		$("#previous-phase").click(function(){
			$.ajax("phase/${gid}/previous", {
				success : function(msg){
					console.log(msg);
					if (msg[0] == "empty"){
						
					}else{
						$("#phase-name").html(msg[1]);
						$("<img>", {
						    src: '../gameimage/${gid}/'+msg[0],
						    load: function() {
						    	$("#jpeg-map").show();
								$("#jpeg-map").html($(this));
								$("#svg-map").hide();
						    }
						});
					}
				}
			});
		});
		$("#next-phase").click(function(){
			$.ajax("phase/${gid}/next", {
				success : function(msg){
					console.log(msg);
					
					if (msg[0] == "empty"){
						
					}else if (msg[0] == "current"){
						$("#jpeg-map").hide();
						$("#svg-map").show();
						$("#phase-name").html(msg[1]);
					}else{
						$("#phase-name").html(msg[1]);
						$("<img>", {
						    src: '../gameimage/${gid}/'+msg[0],
						    load: function() {
								$("#jpeg-map").html($(this));
						    }
						});
					}
				}
			});
		});
<c:if test="${member_of_game}">

		var last_seen = {};
		var loadchat = function(from, last) {
		    var request = {
			"fromid" : from,
			"gameid" : "${gid}",
			"lastseen" : last
		    };
		    //alert(JSON.stringify(request));
			$.ajax("JSONmessages", {
				data : JSON.stringify(request),
				contentType : 'application/json',
				type : 'POST',
				success : function(msg) {
					console.log(msg);
					var arenew = false;
					for (i = 0; i < msg.length; i++) {
						var chatclass = "";
						if (msg[i]["fromid"] == ${me_id}){
							chatclass = "chat-me";
						}
						$("#chatlog-" + from).append("<div class='chatcontainer "+chatclass+"'>" + msg[i]["text"] + "<span class='pull-right'><b>"+msg[i]["fromuser"]+"</b> "+$.localtime.toLocalTime(msg[i]["timestamp"], 'dd MMM h:mm a')+"</span><hr style='clear:both;visibility:hidden;margin:0px 0px 0px 0px'/></div>");
						$("#chatlog-" + from).scrollTop($("#chatlog-" + from)[0].scrollHeight);
						last_seen[from] = msg[i]["id"];
						if (msg[i]["new"] == "true"){
							arenew=true;
						}
					}
					if (arenew){
						$("#a-"+from).css("background-color","gold");	
					}
					
				}
			});
		};
		$("#chat-tabs > li").click(function() {
			loadchat($(this).attr("chatid"), last_seen[$(this).attr("chatid")]);
			$("#a-"+$(this).attr("chatid")).css("background-color","transparent");
		});
		$("#chat-tabs").children().each(function(){
			loadchat($(this).attr("chatid"), 0);
		});
		$(".dipchat-submit").click(function() {
			var this_user = $(this).attr("userid");
			var chat = {
				"gameid" : "${gid}",
				"to" : this_user,
				"message" : $("#chat-" + this_user).val()
			};
			console.log(chat);
			$.ajax("JSONchat", {
				data : JSON.stringify(chat),
				contentType : 'application/json',
				type : 'POST',
				success : function(msg) {
					$("#chat-" + this_user).val('');
					setTimeout(loadchat(this_user,last_seen[this_user]), 500);
					//alert(this_user);	
				}
			});
		});
<c:if test="${playing}">
        $("#ordertext_send").click(function(){
        	$.ajax("${gid}/JSONtextorder",
					{
						data : JSON.stringify($("#order_textinput").val()),
						contentType : 'application/json',
						type : 'POST',
						success : function(msg) {
							for ( var layer in msg["orders"]) {
								var $element = document.importNode(new DOMParser().parseFromString(msg["orders"][layer],"image/svg+xml").documentElement,true);
								$("#"+$element.id.replace("/","\\/")).remove();
								$("#Layer1 > #" + layer).append($element);
							}
							for ( var id in msg["orders_text"]) {
								$("#"+id.replace("/","\\/")+"_text").remove();
								$("#orders-panel").append('<p id="'+id+'_text"><span class="label label-success">'+msg["orders_text"][id]+'</span> <button province_id="'+id+'" class="glyphicon glyphicon-trash"></button></p>');
								$("#orders-panel > p > button").on('click',removeorder);
							}
							if (msg["error"] !== undefined){
								for (var i = 0;i < msg["error"].length;i++) {
									alert(msg["error"][i]);
								}
							}else if (msg["orders"] === undefined){
								alert("An error occured, refreshing the page may fix the problem.");
							}
							//console.log(msg);
						}
					});
        });
		var from = 0;
		var order = {};
		$("#MouseLayer").children().hover(function() {
			$("#bottom-bar").html("<p>" + provinces[$(this).attr("id")] + "</p>");
		}, function() {
			$("#bottom-bar").html("");
		});
		//Select Order Type
		$("#order-type > *").click(function() {
			$("#order-type > *").removeClass("active");
			$(this).addClass("active");
			order = {
				"type" : $(this).attr("id")
			};
			from = 0;
		});
		//Remove Order
		var removeorder = function() {
			var prov = $(this).attr("province_id");
			$.ajax("${gid}/JSONorder-remove?prov="+encodeURI(prov),
				{
					success : function(msg) {
						console.log(msg);
						if (msg == "success"){
							$("#order_"+prov.replace("/","\\/")).remove();
							$("#"+prov.replace("/","\\/")+"_text").remove();
						}
					}
				});
		};
		$("#orders-panel > p > button").click(removeorder);
		//Click On Map
		$("#MouseLayer > *").click(function() {
			var send = function() {
				$.ajax("${gid}/JSONorder",
					{
						data : JSON.stringify(order),
						contentType : 'application/json',
						type : 'POST',
						success : function(msg) {
							for ( var layer in msg["orders"]) {
								var $element = document.importNode(new DOMParser().parseFromString(msg["orders"][layer],"image/svg+xml").documentElement,true);
								$("#"+$element.id.replace("/","\\/")).remove();
								$("#Layer1 > #" + layer).append($element);
							}
							for ( var id in msg["orders_text"]) {
								$("#"+id.replace("/","\\/")+"_text").remove();
								$("#orders-panel").append('<p id="'+id+'_text"><span class="label label-success">'+msg["orders_text"][id]+'</span> <button province_id="'+id+'" class="glyphicon glyphicon-trash"></button></p>');
								$("#orders-panel > p > button").on('click',removeorder);
							}
							if (msg["error"] !== undefined){
								for (var i = 0;i < msg["error"].length;i++) {
									alert(msg["error"][i]);
								}
							}else if (msg["orders"] === undefined){
								alert("An error occured, refreshing the page may fix the problem.");
							}
							//console.log(msg);
						}
					});
			};
			if (!order.hasOwnProperty("type")) {
				return;
			}
			if (!order.hasOwnProperty("loc")) {
				order["loc"] = $(this).attr("id");
				if (order["type"] == "order-hold" || order["type"] == "order-builda" || order["type"] == "order-buildf" || order["type"] == "order-destroy" || order["type"] == "order-disband") {
					send();
					order = {
						"type" : order["type"]
					};
				}
			} else {
				if (!order.hasOwnProperty("loc1")) {
					order["loc1"] = $(this).attr("id");
					if (order["type"] == "order-shold" || order["type"] == "order-move" || order["type"] == "order-retreat") {
						send();
						order = {
							"type" : order["type"]
						};
					}
				} else {
					order["loc2"] = $(this).attr("id");
					send();
					order = {
						"type" : order["type"]
					};
				}
			}
		});
		$('button#ready-button').click(function() {
			var ready = $(this).hasClass("active");
			$.ajax("${gid}/JSONready", {
				success : function(msg) {
					if (msg["ready"] != ready) {
						$('button#ready-button').toggleClass("active");
						$('img#ready-img').toggleClass("intangible");
					}
				}
			});
		});
		var provinces = {
<c:forEach items="${provinces}" var="province">
			"${province.key}" : "${province.value}",</c:forEach>
		};
</c:if></c:if>
	});
	//]]>
</script>

<%@include file="tail.jsp"%>