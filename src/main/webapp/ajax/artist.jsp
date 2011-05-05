<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.studio20design.tuneserver.Artist" %>
<%@ page import="net.studio20design.tuneserver.MusicDatabase" %>
<%
    Artist artist = MusicDatabase.getArtist(Long.parseLong(request.getParameter("artist-id")));
    pageContext.setAttribute("artist", artist);
%>
<h1>${artist.name}</h1>
<c:forEach items="${artist.albums}" var="album">
    <div class="album-group">
        <div class="album-art">
            <a href="#ajax/album.jsp?album-id=${album.albumId}" class="left">
                <img src="images/medium-album-art.jsp?_=${album.albumId}" alt="${album.title}"/>
            </a>
        </div>
        <h1 class="album-title">
            <a href="#ajax/album.jsp?album-id=${album.albumId}" class="album">${album.title}</a>
        </h1>
        <ul class="left">
            <c:forEach items="${album.songs}" var="song">
                <li class="song">
                    <a href="#ajax/song.jsp?song-id=${song.songId}">${song.title}</a>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:forEach>