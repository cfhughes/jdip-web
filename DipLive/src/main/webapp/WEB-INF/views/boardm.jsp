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


<div id="svg-map">${svg}</div>

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
</html>