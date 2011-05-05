<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.studio20design.tuneserver.Album" %>
<%@ page import="net.studio20design.tuneserver.MusicDatabase" %>
<%@ page import="net.studio20design.tuneserver.Song" %>
<%
    long songId = Long.parseLong(request.getParameter("song-id"));
    Song song = MusicDatabase.getSong(songId);
    Album album = MusicDatabase.getAlbum(song.getAlbumId());
    album.getSongs().remove(song);
    request.setAttribute("song", song);
    request.setAttribute("album", album);

%>


<div class="breadcrumb">
    <a href="#ajax/artist.jsp?artist-id=${song.artistId}">${song.artist}</a> &raquo;
    <a href="#ajax/album.jsp?album-id=${song.albumId}">${song.album}</a> &raquo;
    ${song.title}
</div>

<div class="album-art">
    <img src="images/large-album-art.jsp?_=${song.albumId}" alt="${song.title}"/>
</div>

<ul id="song">
    <li><b>${song.title}</b> (<a href="download/song.jsp?_=${song.songId}">download</a>)</li>
    <li class="small">Album: <a href="#ajax/album.jsp?album-id=${song.albumId}" class="album">${song.album}</a></li>
    <li class="small">Artist: ${song.artist}</li>
    <li class="small">Length: ${song.length}</li>
    <li class="small"><a href="javascript: playSong('download/song.jsp?_=${song.songId}')">Play this song in player</a> </li>
    <li class="small"><a href="javascript: addSongToPlaylist('download/song.jsp?_=${song.songId}')">Add this song to playlist</a> </li>
    <li class="player">        
        <object type="application/x-shockwave-flash" data="player/player_mp3.swf" width="200" height="20">
            <param name="movie" value="player/player_mp3.swf"/>
            <param name="wmode" value="transparent"/>
            <param name="FlashVars" value="mp3=download/song.jsp?_=${song.songId}"/>
        </object>
    </li>
</ul>

<div class="right module">
    <h3>Other Songs in this Album</h3>
    <ul>
        <c:forEach items="${album.songs}" var="song">
            <li><a href="download/song.jsp?_=${song.songId}">${song.title}</a></li>
        </c:forEach>
    </ul>
</div>