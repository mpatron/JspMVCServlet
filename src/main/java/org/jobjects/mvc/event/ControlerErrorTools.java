package org.jobjects.mvc.event;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class ControlerErrorTools {

  public ControlerErrorTools() {
  }

  @SuppressWarnings("unchecked")
  public final void regenereHtml(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
    ServletOutputStream sos = response.getOutputStream();
    response.setContentType("text/html; charset=iso-8859-1");
    response.setCharacterEncoding("iso-8859-1");

    response.reset();

    InputStream is = getClass().getResourceAsStream("ControlerErrorTools.html");
    InputStreamReader isr = new InputStreamReader(is);
    LineNumberReader lnr = new LineNumberReader(isr);
    String line = null;
    while ((line = lnr.readLine()) != null) {
      sos.println(line);
    }
    lnr.close();
    isr.close();
    is.close();

    if (!StringUtils.isBlank(message)) {
      sos.println(SystemUtils.LINE_SEPARATOR);
      sos.println("<pre>");
      sos.println(message);
      sos.println("</pre>");
    }

    /* Informations diverses */
    sos.println(SystemUtils.LINE_SEPARATOR);
    makeTable(request, sos);

    /* HEADERS */
    sos.println(SystemUtils.LINE_SEPARATOR);
    Map<String, String> map = new HashMap<String, String>();
    Enumeration<String> enumsNames = request.getHeaderNames();
    while (enumsNames.hasMoreElements()) {
      String enumName = (String) enumsNames.nextElement();
      map.put(enumName, request.getHeader(enumName));
    }
    makeTable("HEADERS", map, sos);

    /* ATTRIBUTES */
    sos.println(SystemUtils.LINE_SEPARATOR);
    map = new HashMap<String, String>();
    enumsNames = request.getAttributeNames();
    while (enumsNames.hasMoreElements()) {
      String enumName = (String) enumsNames.nextElement();
      map.put(enumName, (String) request.getAttribute(enumName));
    }
    makeTable("ATTRIBUTES", map, sos);

    /* PARAMETERS */
    sos.println(SystemUtils.LINE_SEPARATOR);
    map = new HashMap<String, String>();
    enumsNames = request.getParameterNames();
    while (enumsNames.hasMoreElements()) {
      String enumName = (String) enumsNames.nextElement();
      map.put(enumName, (String) request.getParameter(enumName));
    }
    makeTable("PARAMETERS", map, sos);

    sos.println(SystemUtils.LINE_SEPARATOR);
    sos.println("</body>");
    sos.println("</html>");
    sos.flush();
  }

  private void makeTable(HttpServletRequest request, ServletOutputStream sos) throws IOException {
    sos.println("<table class=\"tableError\">");
    sos.println("<caption>Informations diverses</caption>");
    sos.println("<tr>");
    sos.println("<th>Nom du champ</th>");
    sos.println("<th>Valeur</th>");
    sos.println("</tr>");
    sos.println("<tr><td>Date</td><td>" + (new Date(System.currentTimeMillis())).toString() + "</td></tr>");
    sos.println("<tr><td>AuthType</td><td>" + request.getAuthType() + "</td></tr>");
    sos.println("<tr><td>ContextPath</td><td>" + request.getContextPath() + "</td></tr>");
    sos.println("<tr><td>Method</td><td>" + request.getMethod() + "</td></tr>");
    sos.println("<tr><td>PathInfo</td><td>" + request.getPathInfo() + "</td></tr>");
    sos.println("<tr><td>PathTranslated</td><td>" + request.getPathTranslated() + "</td></tr>");
    sos.println("<tr><td>QueryString</td><td>" + request.getQueryString() + "</td></tr>");
    sos.println("<tr><td>RemoteUser</td><td>" + request.getRemoteUser() + "</td></tr>");
    sos.println("<tr><td>RequestURI</td><td>" + request.getRequestURI() + "</td></tr>");
    sos.println("<tr><td>RequestURL</td><td>" + request.getRequestURL() + "</td></tr>");
    sos.println("<tr><td>ServletPath</td><td>" + request.getServletPath() + "</td></tr>");
    sos.println("</table>");
  }

  private void makeTable(String title, Map<String, String> values, ServletOutputStream sos) throws IOException {
    sos.println("<table class=\"tableError\">");
    sos.println("<caption>" + title + "</caption>");
    sos.println("<tr>");
    sos.println("<th>Nom du champ</th>");
    sos.println("<th>Valeur</th>");
    sos.println("</tr>");
    Iterator<String> keys = values.keySet().iterator();
    while (keys.hasNext()) {
      String key = (String) keys.next();
      sos.println("<tr><td>" + key + "</td><td>" + values.get(key) + "</td></tr>");
    }

    sos.println("</table>");
  }

}
