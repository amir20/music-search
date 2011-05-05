<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.studio20design.tuneserver.Album" %>
<%@ page import="net.studio20design.tuneserver.MusicDatabase" %>
<%@ page import="net.studio20design.tuneserver.Song" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="net.studio20design.fileutils.ZipUtils" %>
<%
    if (request.getParameter("album-id") != null) {
        long albumId = Long.parseLong(request.getParameter("album-id"));
        Album album = MusicDatabase.getAlbum(albumId);
        List<File> files = new ArrayList<File>();
        for (Song song : album.getSongs()) {
            files.add(song.getFile());
        }
        response.setHeader("Content-Disposition", "attachment;filename=\"" + album.getArtist() + " - " + album.getTitle() + ".zip\"");
        OutputStream outputStream = response.getOutputStream();
        ZipUtils.zipFilesToStream(outputStream, files, ZipUtils.ZipLevel.DEFLATED);
    }
%>