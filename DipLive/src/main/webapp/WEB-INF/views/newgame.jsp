<%@include file="head.jsp"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<form:form action="savegame" commandName="game" class="form-horizontal"
	role="form">
	<div class="form-group">
		<label for="name" class="control-label col-lg-2">Game Name:</label>
		<div class="col-lg-5">
			<form:input path="name" id="name" type="text" class="form-control" />
		</div>
	</div>
	<div class="form-group">
		<label for="variant" class="control-label col-lg-2">Variant</label>
		<div class="col-lg-5">
			<select id="variant" name="variant" class="form-control">
				<c:forEach items="${variants}" var="variant">
					<option value="${variant.name}">${variant.name}(
						<c:forEach items="${variant.powers}" var="power">
							${power.name}
						</c:forEach>)
					</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label for="secret" class="control-label col-lg-2">Password
			(Blank for public):</label>
			<div class="col-lg-5">
		<form:input path="secret" id="secret" type="text" class="form-control"/>
		</div>
	</div>
	<div class="form-group">
	<div class="col-lg-offset-2 col-lg-5">
		<button class="btn btn-default" type="submit">Create!</button>
		</div>
	</div>
</form:form>
<%@include file="tail.jsp"%>