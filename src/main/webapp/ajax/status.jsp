<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.studio20design.tuneserver.IndexerStatus" %>
<%
    pageContext.setAttribute("status", IndexerStatus.getInstance());
%>
<c:if test="${status.indexing or status.waitingToIndex}">
    Please wait until your music is being index. This will only happen once. After the first time, every hour new music will be found and indexed in the background.

    <div id="progress">
        <div id="bar" style="width: ${status.percentComlete}%">
        </div>
    </div>
    <div class="left">
        Songs Indexed: ${status.totalIndexed}
    </div>
    <div class="right">
        Total songs to index: ${status.totalToIndex}
    </div>
</c:if>
<c:if test="${not status.indexing and not status.waitingToIndex}">
<div class="notindexing">
    Indexing complete.    
</div>
</c:if>