<%@include file="head.jsp"%>
<style type="text/css">

.chatcontainer {
	position:relative;
	padding:10px;
	margin:1em 0 3em;
	color:#000;
	background:#f3961c; /* default background for browsers without gradient support */
	/* css3 */
	background:-webkit-gradient(linear, 0 0, 0 100%, from(#f9d835), to(#f3961c));
	background:-moz-linear-gradient(#f9d835, #f3961c);
	background:-o-linear-gradient(#f9d835, #f3961c);
	background:linear-gradient(#f9d835, #f3961c);
	-webkit-border-radius:10px;
	-moz-border-radius:10px;
	border-radius:10px;
	max-width: 300px;
}

/* Variant : for left/right positioned triangle */

.chatcontainer.chat-left {
	margin-left:50px;
}

/* Variant : for right positioned triangle
------------------------------------------ */

.chatcontainer.chat-right {
	margin-right:50px;
}

/* THE TRIANGLE
------------------------------------------------------------------------------------------------------------------------------- */

/* creates triangle */
.chatcontainer:after {
	content:"";
	position:absolute;
	bottom:-15px; /* value = - border-top-width - border-bottom-width */
	left:50px; /* controls horizontal position */
	border-width:15px 15px 0; /* vary these values to change the angle of the vertex */
	border-style:solid;
	border-color:#f3961c transparent;
    /* reduce the damage in FF3.0 */
    display:block; 
    width:0;
}



/* Variant : left
------------------------------------------ */

.chatcontainer.chat-left:after {
	top:16px; /* controls vertical position */
	left:-50px; /* value = - border-left-width - border-right-width */
	bottom:auto;
	border-width:10px 50px 10px 0;
	border-color:transparent #f3961c;
}

/* Variant : right
------------------------------------------ */

.chatcontainer.chat-right:after {
	top:16px; /* controls vertical position */
	right:-50px; /* value = - border-left-width - border-right-width */
	bottom:auto;
    left:auto;
	border-width:10px 0 10px 50px;
	border-color:transparent #f3961c;
}

.chat-me:after {
	border-color:#075698 transparent;
}

.chat-me {
	background:#075698;
	/* css3 */
	background:-webkit-gradient(linear, 0 0, 0 100%, from(#2e88c4), to(#075698));
	background:-moz-linear-gradient(#2e88c4, #075698);
	background:-o-linear-gradient(#2e88c4, #075698);
	background:linear-gradient(#2e88c4, #075698);
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
</style>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"
	type="text/javascript"></script>
${svg}
<br />
<h3>${gamephase}</h3>

<c:if test="${member_of_game and started}">
	<div id="order-type" class="btn-group">
		<c:choose>
			<c:when test="${phasetype == 'M'}">

				<button id="order-move" class="btn">Move</button>
				<button id="order-hold" class="btn">Hold</button>
				<button id="order-shold" class="btn">Support Hold</button>
				<button id="order-smove" class="btn">Support Move</button>
				<button id="order-convoy" class="btn">Convoy</button>

			</c:when>
			<c:when test="${phasetype == 'R'}">
				<button id="order-retreat" class="btn">Retreat</button>
				<button id="order-disband" class="btn">Disband</button>
			</c:when>
			<c:when test="${phasetype == 'B'}">
				<button id="order-builda" class="btn">Build Army</button>
				<button id="order-buildf" class="btn">Build Fleet</button>
				<button id="order-destroy" class="btn">Disband</button>
			</c:when>
		</c:choose>
	</div>
	<div style="height: 20px" id="bottom-bar"></div>
	<button id="ready-button"
		class="btn<c:if test="${isready}"> active</c:if>">Ready</button>
</c:if>
<c:if test="${member_of_game}">
	<div>
		<ul id="chat-tabs" class="nav nav-tabs">
		<li chatid="-1"><a href="#allchat" data-toggle="tab">All Players</a></li>
			<c:forEach items="${players}" var="player">
				<c:if test="${player.id != me_id}">
					<li chatid="${player.id}"><a href="#tab-${player.id}"
						data-toggle="tab">${player.user.username}(${player.power})</a></li>
				</c:if>
			</c:forEach>
		</ul>
		<div class="tab-content">
			<div class="tab-pane" id="allchat">
				<div id="chatlog--1" class="chat-messages"></div>
				<textarea id="chat--1" name="-1"
							class="chat-input" rows="3" cols=""></textarea>
				<button userid="-1" class="dipchat-submit button"
							type="btn">Send</button>
				<br>
				<h3>All Players</h3>
			</div>
			<c:forEach items="${players}" var="player">
				<c:if test="${player.id != me_id}">
					<div class="tab-pane" id="tab-${player.id}">
						<div id="chatlog-${player.id}" class="chat-messages"></div>
						<textarea id="chat-${player.id}" name="${player.id}"
							class="chat-input" rows="3" cols=""></textarea>
						<button userid="${player.id}" class="dipchat-submit button"
							type="btn">Send</button>
						<br>
						<h3>${player.user.username}</h3>
						<p>${player.power}</p>
					</div>
				</c:if>
			</c:forEach>
		</div>
	</div>
</c:if>
<c:if test="${member_of_game}">
	<script type="text/javascript">
		$(function() {
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
						for (i = 0; i < msg.length; i++) {
							var chatclass = "";
							if (msg[i][2] == ${me_id}){
								chatclass = "chat-me";
							}
							$("#chatlog-" + from).append(
									"<p class='chatcontainer "+chatclass+"'>" + msg[i][1] + "</p>");
							last_seen[from] = msg[i][0];
						}
					}
				});
			};
			$("#chat-tabs > li").click(
					function() {
						loadchat($(this).attr("chatid"), last_seen[$(this)
								.attr("chatid")]);
					});
			loadchat($("#chat-tabs > li:first-child").attr("chatid"), 0);
			$(".dipchat-submit").click(
					function() {
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
								setTimeout(loadchat(this_user,
										last_seen[this_user]), 500);
								//alert(this_user);	
							}
						});
					});
			<c:if test="${started}">
			var from = 0;
			var order = {};
			$("#MouseLayer > *").hover(
					function() {
						$("#bottom-bar").html(
								"<p>Hovering Over " + $(this).attr("id")
										+ " </p>");
					}, function() {
						$("#bottom-bar").html("");
					});
			$("#order-type > *").click(function() {
				$("#order-type > *").removeClass("active");
				$(this).addClass("active");
				order = {
					"type" : $(this).attr("id")
				};
				from = 0;
			});
			$("#MouseLayer > *")
					.click(
							function() {
								var send = function() {
									$.ajax("${gid}/JSONorder",
											{
												data : JSON.stringify(order),
												contentType : 'application/json',
												type : 'POST',
												success : function(msg) {
													for ( var layer in msg["orders"]) {
														var $element = document
																.importNode(
																		new DOMParser()
																				.parseFromString(msg["orders"][layer],"image/svg+xml").documentElement,true);
														$("#"+$element.id).remove();
														$("#Layer1 > #" + layer).append($element);
													}
													if (msg["error"] !== undefined){
														for (var i = 0;i < msg["error"].length;i++) {
															alert(msg["error"][i]);
														}
													}
													console.log(msg);
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
						}
					}
				});

			});
			</c:if>

		});
	</script>
</c:if>
<%@include file="tail.jsp"%>