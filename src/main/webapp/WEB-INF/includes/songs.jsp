<%@ page import="net.studio20design.tuneserver.Album" %>
<%@ page import="net.studio20design.tuneserver.Song" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String group = request.getParameter("group");
    List<Song> songs = (List<Song>) request.getAttribute("songs");
    if (group == null) {
        group = "song";
    }
    pageContext.setAttribute("group", group);

    if (group.equals("album")) {
        Map<Long, List<Song>> albumsTemp = new HashMap<Long, List<Song>>();
        for (Song song : songs) {
            if (!albumsTemp.containsKey(song.getAlbumId())) {
                albumsTemp.put(song.getAlbumId(), new ArrayList<Song>());
            }
            albumsTemp.get(song.getAlbumId()).add(song);
        }
        Map<String, Album> albums = new HashMap<String, Album>();
        for (List<Song> s : albumsTemp.values()) {
            Album album = new Album(s);
            if (albums.containsKey(album.getTitle())) {
                albums.get(album.getTitle()).getSongs().addAll(album.getSongs());
            } else {
                albums.put(album.getTitle(), album);
            }
        }
        pageContext.setAttribute("albums", albums);
    } else if (group.equals("artist")) {
        Map<String, List<Song>> artists = new HashMap<String, List<Song>>();
        for (Song song : songs) {
            if (!artists.containsKey(song.getArtist())) {
                artists.put(song.getArtist(), new ArrayList<Song>());
            }
            artists.get(song.getArtist()).add(song);
        }
        pageContext.setAttribute("artists", artists);
    }

%>
<div>
    Group by
    <a href="#ajax/search.jsp?query=${fn:replace(param.query, " ", "+")}&group=song">Songs</a>
    <a href="#ajax/search.jsp?query=${fn:replace(param.query, " ", "+")}&group=album">Album</a>
    <a href="#ajax/search.jsp?query=${fn:replace(param.query, " ", "+")}&group=artist">Artist</a>
</div>

<c:choose>
    <c:when test="${group eq 'album'}">
        <c:forEach items="${albums}" var="album">
            <div class="album-group">
                <div class="album-art">
                    <a href="#ajax/album.jsp?album-id=${album.value.albumId}" class="left">
                        <img src="images/medium-album-art.jsp?_=${album.value.albumId}" alt="${album.value.title}"/>
                    </a>
                </div>
                <h1 class="album-title"><a href="#ajax/album.jsp?album-id=${album.value.albumId}" class="album">${album.value.title}</a> <span class="small">by</span> <a href="#ajax/artist.jsp?artist-id=${song.artistId}" class="small artist"> ${album.value.artist}</a></h1>
                <ul class="left">
                    <c:forEach items="${album.value.songs}" var="song">
                        <li class="song">
                            <a href="#ajax/song.jsp?song-id=${song.songId}">${song.title}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:forEach>
    </c:when>
    <c:when test="${group eq 'artist'}">
        <c:forEach items="${artists}" var="artist">
            <div class="clear">
                <h1>${artist.key}</h1>
                <ul>
                    <c:forEach items="${artist.value}" var="song">
                        <li class="clear">
                            <div class="album-art">
                                <a href="#ajax/song.jsp?song-id=${song.songId}" class="left">
                                    <img src="images/tiny-album-art.jsp?_=${song.albumId}" alt="${song.album}"/>
                                </a>
                            </div>
                            <a href="#ajax/song.jsp?song-id=${song.songId}">${song.title}</a> from album
                            <a href="#ajax/album.jsp?album-id=${song.albumId}" class="album">${song.album}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:forEach>
    </c:when>
    <c:when test="${group eq 'song'}">
        <ul id="songs">
                <%--@elvariable id="songs" type="java.util.List<net.studio20design.tuneserver.Song>"--%>
            <c:forEach items="${songs}" var="song">
                <li class="song">
                    <div class="album-art">
                        <a href="#ajax/song.jsp?song-id=${song.songId}" class="left">
                            <img src="images/small-album-art.jsp?_=${song.albumId}" alt="${song.album}"/>
                        </a>
                    </div>
                    <ul>
                        <li><a href="#ajax/song.jsp?song-id=${song.songId}">${song.title}</a></li>
                        <li><a href="#ajax/album.jsp?album-id=${song.albumId}" class="album">${song.album}</a></li>
                        <li><a href="#ajax/artist.jsp?artist-id=${song.artistId}" class="artist">${song.artist}</a></li>
                    </ul>
                </li>
            </c:forEach>
        </ul>
    </c:when>
</c:choose>
