<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.studio20design.tuneserver.Album" %>
<%@ page import="net.studio20design.tuneserver.MusicDatabase" %>
<%@ page import="net.studio20design.tuneserver.Song" %>
<%@ page import="java.util.List" %>
<%
    List<Album> albums = MusicDatabase.getAlbums();
    List<Song> songs = MusicDatabase.getSongs();
    pageContext.setAttribute("songs", songs.size());
    pageContext.setAttribute("albums", albums.size());

%>

<h1>Latest Search History</h1>
<ul>
    <c:forEach items="${sessionScope.userHistory.history}" var="history">
        <li><a href="#ajax/search.jsp?query=${history}">${history}</a></li>
    </c:forEach>
</ul>

<h1>Total Number of Songs ${songs}</h1>
<h1>Total Number of Albums ${albums}</h1>