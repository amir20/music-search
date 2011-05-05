<%@ page import="net.studio20design.tuneserver.MusicDatabase" %>
<%@ page import="net.studio20design.tuneserver.Song" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.OutputStream" %>
<%
    Song song = MusicDatabase.getSong(Long.parseLong(request.getParameter("_")));
    response.setHeader("Content-Disposition", "attachment;filename=\"" + song.getFile().getName() + "\"");
    FileInputStream fileinputstream = new FileInputStream(song.getFile());
    response.setContentLength(fileinputstream.available());
    byte[] data = new byte[fileinputstream.available()];
    fileinputstream.read(data);
    OutputStream outputStream = response.getOutputStream();
    outputStream.write(data);
    fileinputstream.close();
%>