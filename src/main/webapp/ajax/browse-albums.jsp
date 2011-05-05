<%@ page import="net.studio20design.tuneserver.Album" %>
<%@ page import="net.studio20design.tuneserver.MusicDatabase" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    List<Album> albums = MusicDatabase.getAlbums();
    Map<Character, List<Album>> alpha = new HashMap<Character, List<Album>>();
    for (Album album : albums) {
        if (album.getArtist().length() > 0) {
            char c = album.getArtist().toLowerCase().charAt(0);
            if (!alpha.containsKey(c)) {
                alpha.put(c, new ArrayList<Album>());
            }
            alpha.get(c).add(album);
        }
    }

    if (request.getParameter("index") == null) {
        albums = alpha.get('a');
    } else {
        albums = alpha.get(request.getParameter("index").charAt(0));
    }
    pageContext.setAttribute("albums", albums);
    char[] alphabet = new char[26];
    for (char c = 'a'; c <= 'z'; c++) {
        alphabet[c - 'a'] = c;
    }
    pageContext.setAttribute("alphabet", alphabet);
%>
<ul id="songs">
    <c:forEach items="${albums}" var="album">
        <li class="song">
            <div class="album-art">
                <a href="#ajax/album.jsp?album-id=${album.albumId}" class="left">
                    <img src="images/small-album-art.jsp?_=${album.albumId}" alt="${album.title}"/>
                </a>
            </div>
            <ul>
                <li><a href="#ajax/album.jsp?album-id=${album.albumId}" class="ajax">${album.title}</a></li>
                <li>${album.artist}</li>
            </ul>
        </li>
    </c:forEach>
</ul>
<div>
    <c:forEach items="${alphabet}" var="c">
        <a href="#ajax/browse-albums.jsp?index=${c}">${c}</a>
    </c:forEach>
</div>
