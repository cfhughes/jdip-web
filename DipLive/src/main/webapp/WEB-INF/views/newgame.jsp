<%@include file="head.jsp"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<form:form action="savegame" commandName="game" class="form-horizontal"
	role="form">
	<div class="form-group">
		<label for="name" class="control-label col-lg-2">Game Name:</label>
		<div class="col-lg-5">
			<form:input path="name" id="name" type="text" class="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="turnlength" class="control-label col-lg-2">Turn
			Length (1-336 hours)</label>
		<div class="col-lg-5">
			<form:input path="turnlength" id="turnlength" type="text"
				class="form-control" />

		</div>
		<form:errors path="turnlength" cssClass="alert alert-danger" />
	</div>
	<div class="form-group">
		<label for="secret" class="control-label col-lg-2">Password
			(Blank for public):</label>
		<div class="col-lg-5">
			<form:input path="secret" id="secret" type="text"
				class="form-control" />
		</div>
	</div>
	<div class="col-lg-2" style="height: 300px; overflow: auto;">
		<ul class="nav nav-pills nav-stacked" id="choose-variant">
			<c:forEach items="${variants}" var="variant">
				<li><a id="${variant.name}"
					href="#${variant}" data-toggle="tab" >${variant.name}</a></li>

			</c:forEach>
		</ul>
	</div>
	<input type="hidden" id="variant" name="variant" value="Standard" />
	<div class="tab-content" class="col-lg-5" style="height: 300px; overflow: auto;">
		<c:forEach items="${variants}" var="variant">
			<div class="tab-pane panel panel-default" id="${variant}"><div class="panel-body"><img style="width:50%;min-width:300px;" src="<c:url value="/vmap/${variant.name}" />" /><p>${variant.description}</p><p><b>Powers: </b><c:forEach items="${variant.powers}" var="power">${power} </c:forEach></p></div></div>
		</c:forEach>
	</div>
	<div class="form-group">
		<div class="col-lg-offset-2 col-lg-5">
			<button class="btn btn-default" type="submit">Create!</button>
		</div>
	</div>
</form:form>
<script type="text/javascript">
$(function(){
$("#choose-variant a").click(function(){
	$('#variant').val($(this).attr('id'));	
});
});
</script>
<%@include file="tail.jsp"%>