<%@ page import="net.studio20design.imageutils.Image" %>
<%@ page import="net.studio20design.tuneserver.AlbumArt" %>
<%@ page import="java.io.File" %>
<%
    response.setContentType("image/jpeg");
    long id = Long.parseLong(request.getParameter("_"));
    Image artwork = AlbumArt.getAlbumArt(id, 48, 48, new File(getServletConfig().getServletContext().getRealPath("images/default.jpg")));
    artwork.toOutputStream(response.getOutputStream());
%>