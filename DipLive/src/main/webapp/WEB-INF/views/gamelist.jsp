<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:forEach items="${games}" var="game">
<p><a href="joingame/${game.id}">${game.name}</a></p>
</c:forEach>