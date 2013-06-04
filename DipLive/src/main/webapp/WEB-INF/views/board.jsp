<html>
<head>
<title>Board</title>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>

</head>
<body>
	${svg}
	<div id="bottom-bar"></div>
	<script>
		var from = 0;
		$("#bottom-bar").html("Nothing");
		$("path,polygon")
				.hover(
						function() {
							$("#bottom-bar").html(
									"<p>Hovering Over " + $(this).attr("id")
											+ " </p>");
						}, function() {
							$("#bottom-bar").html("");
						});
		$("path,polygon").click(function() {
			if (from == 0) {
				from = $(this).attr("id");
			} else {
				$.ajax("${gid}/move", {
					data : JSON.stringify({
						"from" : from,
						"to" : $(this).attr("id")
					}),
					contentType : 'application/json',
					type : 'POST',
					success : function(msg) {
						alert(msg["success"]);
					}
				});
				from = 0;
			}
		});
	</script>
</body>
</html>