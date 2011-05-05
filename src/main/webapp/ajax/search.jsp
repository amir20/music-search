<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.studio20design.tuneserver.MusicDatabase" %>
<%@ page import="net.studio20design.tuneserver.Song" %>
<%@ page import="java.util.List" %>
<%@ page import="net.studio20design.tuneserver.SearchHistory" %><%
    String q = request.getParameter("query");
    List<Song> songs = null;
    if (q != null) {
        songs = MusicDatabase.search(q);
        request.setAttribute("songs", songs);
    }

    SearchHistory history = (SearchHistory) session.getAttribute("userHistory");
    if (history == null) {
        history = new SearchHistory();
    }
    history.addToHistory(q);
    history.setLastResults(songs);
    session.setAttribute("userHistory", history);

%>
<c:import url="/WEB-INF/includes/songs.jsp"/>