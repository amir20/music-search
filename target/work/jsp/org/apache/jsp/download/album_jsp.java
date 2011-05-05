package org.apache.jsp.download;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import net.studio20design.tuneserver.Album;
import net.studio20design.tuneserver.MusicDatabase;
import net.studio20design.tuneserver.Song;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.studio20design.fileutils.ZipUtils;

public final class album_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.Vector _jspx_dependants;

  private org.apache.jasper.runtime.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

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

    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
