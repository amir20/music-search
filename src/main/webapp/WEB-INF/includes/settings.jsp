<%@ page import="net.studio20design.tuneserver.IndexerStatus" %><%
    pageContext.setAttribute("status", IndexerStatus.getInstance());
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${status.waitingToIndex}">
    <div id="settings">
    </div>
    <script type="text/javascript">
        var interval = setInterval(function() {
            $("#settings").load("ajax/status.jsp", function() {
                if ($("#settings .notindexing").length > 0) {
                    clearInterval(interval);
                }
            });
        }, 1000);
    </script>
</c:if>

<c:if test="${not status.waitingToIndex}">
    <div id="settings">
        <h1>
            Hello there!
        </h1>
        Welcome to tune server.

        <form action="" method="post">
            <label>Tell me where you save your music?</label>
            <input type="text" name="path"/>
            <input type="submit" name="Next" value="Next"/>
        </form>
    </div>
    <script type="text/javascript">
        $("#settings > form").submit(function() {
            $.post("ajax/settingsSubmit.jsp", {path: $("input[name=path]").val()},
                    function() {
                        var interval = setInterval(function() {
                            $("#settings").load("ajax/status.jsp", function() {
                                if ($("#settings .notindexing").length > 0) {
                                    clearInterval(interval);
                                }
                            });
                        }, 1000);
                    });
            return false;
        });
    </script>
</c:if>