<%@include file="head.jsp"%>
<form name='f' action="<c:url value='j_spring_security_check' />"
	method='POST' class="form-horizontal">
	<c:if test="${not empty error}">
		<div class="alert alert-error">
			Your login attempt was not successful, try again.<br /> Caused :
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
	<div class="control-group">
		<label class="control-label" for="uname">User</label>
		<div class="controls">
		<input id="uname" type='text' name='j_username' value=''>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="pass">Password</label>
		<div class="controls">
		<input id="pass" type='password' name='j_password' />
		</div>
	</div>
	<div class="control-group">
	<div class="controls">
		<button class="btn" type="submit" >Login</button>
		</div>
	</div>
</form>
<p><a href="<c:url value="/signin/facebook"/>"><img src="<c:url value="/resources/img/fblogin.png"/>" border="0"/></a><br/></p>
<%@include file="tail.jsp"%>