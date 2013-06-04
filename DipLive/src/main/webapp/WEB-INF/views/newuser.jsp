<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<form:form action="saveuser" commandName="user">
<label for="uname">User Name:</label><form:input path="username" id="uname" type="text"/><br>
<label for="pword">Password:</label><form:input path="password" id="pword" type="text"/><br>
<button type="submit">Submit</button>
</form:form>