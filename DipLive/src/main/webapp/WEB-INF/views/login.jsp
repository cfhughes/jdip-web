<%@include file="head.jsp"%>


<form name='f' action="<c:url value='j_spring_security_check' />"
	method='POST' class="form-horizontal" role="form">
	<c:if test="${not empty error}">
		<div class="alert alert-error">
			Your login attempt was not successful, try again.<br /> Caused :
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
	<div class="form-group">
		<label class="control-label col-lg-2" for="uname">User</label>
		<div class="col-lg-5">
			<input class="form-control" id="uname" type='text' name='j_username'
				value='' />
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-lg-2" for="pass">Password</label>
		<div class="col-lg-5">
			<input id="pass" type='password' name='j_password'
				class="form-control" />
		</div>
	</div>
	<div class="form-group">
		<div class="col-lg-offset-2 col-lg-5">
			<button class="btn btn-default" type="submit">Login</button>
		</div>
	</div>
</form>
<div class="col-lg-offset-2 col-lg-5">
	<form id="fb_signin" action="<c:url value="/signin/facebook"/>"
		method="POST">
		<div id="fb-root"></div>
		<input type="image" src="<c:url value="/resources/img/fb.gif"/>"
			name="submit" alt="Signin with Facebook" />
	</form>
</div>
<div class="col-lg-offset-2 col-lg-5">
	<form id="google_signin" action="<c:url value="/signin/google"/>"
		method="POST">
		<input type="image" src="<c:url value="/resources/img/gg.gif"/>"
			name="submit" alt="Signin with Google" />
	</form>
</div>
<%@include file="tail.jsp"%>