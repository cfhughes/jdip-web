<%@include file="head.jsp"%>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
${svg}
<div id="order-type" class="btn-group">
	<button id="order-move" class="btn selected">Move</button>
	<button id="order-hold" class="btn">Hold</button>
	<button id="order-shold" class="btn">Support Hold</button>
	<button id="order-smove" class="btn">Support Move</button>
	<button id="order-convoy" class="btn">Convoy</button>
</div>
<div id="bottom-bar"></div>
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
<%@include file="tail.jsp"%>