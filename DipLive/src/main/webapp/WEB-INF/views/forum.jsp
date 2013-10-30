<%@include file="head.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:forEach items="${topics}" var="topic">
<div class="panel panel-default">
  <div class="panel-body">
    ${topic.text}
  </div>
</div>
</c:forEach>
<div class="panel panel-default">
  <div class="panel-body">
    <textarea id="new-topic"></textarea>
    <button id="new-submit" topic_id="-1">Post</button>
  </div>
</div>
<script type="text/javascript">
$(function(){
	$("#new-submit").click(function(){
		var data = {topic : $(this).attr('topic_id'), text : $("#new-topic").val()};
		$.ajax("/JSONchat",{
			data : JSON.stringify(data)
		});
	});	
});
</script>
<%@include file="tail.jsp" %>