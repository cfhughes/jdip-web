<%@ page language="java"
	contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head><script src="//code.jquery.com/jquery-2.0.3.min.js"
	type="text/javascript"></script>
	<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"
	rel="stylesheet" />
<style type="text/css">
#orderbar {
  position: fixed;
  bottom: 0;
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

svg {
	width: 1000px;
	height: 1000px;	
}

svg:not(:root ) {
	overflow: visible;
}

svg:FIRST-CHILD {
	overflow: hidden;
}
</style>

</head>
<body>

<div id="svg-map">${svg}</div>
<c:if test="${member_of_game and playing}">
	<div id="orderbar">
	<form class="form-inline"><div class="form-group"><select id="order-type" class="form-control" >
	
		<c:choose>
			<c:when test="${phasetype == 'M'}">

				<option value="order-move" >Move</option>
				<option value="order-hold" >Hold</option>
				<option value="order-shold" >Support
					Hold</option>
				<option value="order-smove" >Support
					Move</option>
				<option value="order-convoy" >Convoy</option>

			</c:when>
			<c:when test="${phasetype == 'R'}">
				<option value="order-retreat" >Retreat</option>
				<option value="order-disband" >Disband</option>
			</c:when>
			<c:when test="${phasetype == 'B'}">
				<option value="order-builda" >Build
					Army</option>
				<option value="order-buildf" >Build
					Fleet</option>
				<option value="order-destroy" >Disband</option>
			</c:when>
		</c:choose>
	</select>
	</div><div class="form-group">
	<button id="ready-button"
		class="btn btn-default<c:if test="${isready}"> active</c:if>">Ready</button>
	<img id="ready-img" src="<c:url value="/resources/img/check.png"/>"
		<c:if test="${!isready}">class="intangible"</c:if> /></div></form>
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
		var from = 0;
		var order = {"type" : "order-move"};
		$("#MouseLayer").children().hover(function() {
			$("#bottom-bar").html("<p>" + provinces[$(this).attr("id")] + "</p>");
		}, function() {
			$("#bottom-bar").html("");
		});
		//Select Order Type
		$("#order-type").change(function() {
			//$("#order-type > *").removeClass("active");
			//$(this).addClass("active");
			order = {
				"type" : $(this).val()
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
		$("#MouseLayer > *").on('touchend',function() {
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
			return false;
		});
		var provinces = {
<c:forEach items="${provinces}" var="province">
			"${province.key}" : "${province.value}",</c:forEach>
		};
</c:if></c:if>
	});
	//]]>
</script>
</body>
</html>