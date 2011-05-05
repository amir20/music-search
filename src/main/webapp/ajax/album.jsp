<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.studio20design.tuneserver.MusicDatabase" %>
<%
    long albumId = Long.parseLong(request.getParameter("album-id"));
    request.setAttribute("album", MusicDatabase.getAlbum(albumId));
%>
<div class="breadcrumb">
    <a href="#ajax/artist.jsp?artist-id=${album.firstSong.artistId}">${album.artist}</a> &raquo; ${album.title}
</div>

<div class="album-art">
    <img src="images/large-album-art.jsp?_=${album.albumId}" alt="${album.title}"/>
</div>

<ul id="album" class="left">
    <c:forEach items="${album.songs}" var="song">
        <li><a href="download/song.jsp?_=${song.songId}">${song.title}</a></li>
    </c:forEach>
    <li><br/><br/><br/>
        <a href="download/album.jsp?album-id=${album.albumId}" class="album">Download all</a>

        <div id="status">
        </div>
    </li>
</ul>