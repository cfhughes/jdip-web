<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<form:form action="savegame" commandName="game">
<label for="name">Game Name:</label><form:input path="name" id="name" type="text"/><br>
<button type="submit">Submit</button>
</form:form>