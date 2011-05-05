<%@ page import="net.studio20design.tuneserver.DirectoryListener" %>
<%@ page import="java.io.File" %>
<%@ page import="net.studio20design.tuneserver.IndexerStatus" %>
<%
    if (request.getParameter("path") != null) {
        File path = new File(request.getParameter("path"));
        DirectoryListener listener = new DirectoryListener(path);
        application.setAttribute("listener", listener);
        application.setAttribute("firstTime", false);
        IndexerStatus.getInstance().setWaitingToIndex(true);
    }
%>