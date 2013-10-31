<%@include file="head.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script type="text/javascript" src="resources/jquery.localtime-0.8.0.min.js"></script>
<div class="panel-group" id="accordion">
	<c:forEach items="${topics}" var="topic">
		<div class="panel panel-default">
			<div class="panel-heading" style="height: 41px;">
				<a data-toggle="collapse" data-parent="#accordion"
					href="#collapse${topic.id}">T: ${topic.subject}</a> <span
					class="pull-right">${topic.author.username} <i><span
						data-localtime-format="dd MMM h:mm a"><fmt:formatDate
								pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" value="${topic.timestamp}" /></span></i></span>
			</div>

			<div class="panel-body">
				${topic.text}

				<div class="panel-collapse collapse" id="collapse${topic.id}">
					<c:forEach items="${topic.replies}" var="reply">
						<p>${reply.text}</p>
					</c:forEach>
					<textarea class="form-control"></textarea>
					<button topic_id="${topic.id}" class="btn btn-default new-submit">Post</button>

				</div>
			</div>
		</div>
	</c:forEach>
	<div class="panel panel-default">
		<div class="panel-heading">
			Subject:<input type="text" class="form-control" id="new-subject" />
		</div>
		<div class="panel-body">

			<textarea class="form-control" id="new-topic"></textarea>
			<button class="btn btn-default new-submit" topic_id="-1">Post</button>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	$("button.new-submit").click(function(){
		console.log("Clicked");
		var data = {to : parseInt($(this).attr('topic_id')), message : $(this).parent().find("textarea").val()};
		if ($(this).attr('topic_id') == "-1"){
			data["subject"] = $("#new-subject").val();
		}
		$.ajax("JSONchat",{
			data : JSON.stringify(data),
			contentType : 'application/json',
			type : 'POST',
			success : function(msg){
				console.log(msg);
				if (msg == "success"){
					location.reload();
				}
			}
		});
	});	
});
</script>
<%@include file="tail.jsp" %>