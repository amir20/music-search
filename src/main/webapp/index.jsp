<%@ page import="net.studio20design.tuneserver.IndexerStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%
    pageContext.setAttribute("status", IndexerStatus.getInstance());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Tune Server</title>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery.history.js"></script>
    <script type="text/javascript" src="js/main.js"></script>
    <script type="text/javascript" src="js/player.js"></script>
    <link rel="stylesheet" href="styles/main.css" type="text/css"/>
</head>
<body>
<div id="background">
    <div id="menu">
        <object type="application/x-shockwave-flash" data="player/player.swf" width="203" height="20" id="player">
            <param name="movie" value="player/player.swf"/>
            <param name="wmode" value="transparent"/>
            <param name="allowScriptAccess" value="sameDomain"/>
        </object>
        <a href="#ajax/browse-albums.jsp">browse all albums</a>
        <a href="#">search</a>
        <a href="#">login</a>
        <a href="#">help</a>
    </div>

    <form id="search" method="get" action="">
        <input type="text" name="query" value="<c:out value="${param.query}" default="Search"/>"/>
        <input type="submit"/>
    </form>

    <div id="page">
        <c:choose>
            <c:when test="${applicationScope.firstTime or status.waitingToIndex}">
                <c:import url="WEB-INF/includes/settings.jsp"/>
            </c:when>
            <c:otherwise>
                <c:import url="WEB-INF/includes/home.jsp"/>
            </c:otherwise>
        </c:choose>
    </div>
    <br class="clear"/>
</div>
</body>
</html>
