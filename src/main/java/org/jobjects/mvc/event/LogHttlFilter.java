package org.jobjects.mvc.event;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet Filter implementation class LogHttlFilter
 */
public final class LogHttlFilter implements Filter {
  private FilterConfig filterConfig = null;

  private transient Log log = LogFactory.getLog(getClass());

  /**
   * Default constructor.
   */
  public LogHttlFilter() {
  }

  /**
   * @see Filter#destroy()
   */
  public void destroy() {
    filterConfig = null;
  }

  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    // TODO Auto-generated method stub
    // place your code here
    if (BooleanUtils.toBoolean(filterConfig.getInitParameter("active"))) {
      if (request instanceof HttpServletRequest) {
        // System.out.println(afficheHttpServletRequest((HttpServletRequest)
        // request));
        log.info(afficheHttpServletRequest((HttpServletRequest) request));
      }
    }

    // pass the request along the filter chain
    chain.doFilter(request, response);
  }

  /**
   * @see Filter#init(FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
  }

  @SuppressWarnings("unchecked")
  public static String afficheHttpServletRequest(HttpServletRequest request) {
    String returnValue = StringUtils.EMPTY;
    String tab = "  ";
    String separatorLigne = "+" + StringUtils.repeat("-", 78) + "+" + SystemUtils.LINE_SEPARATOR;

    {
      returnValue += separatorLigne;
      returnValue += "|" + tab + "Informations diverses : " + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "Date=" + (new Date(System.currentTimeMillis())).toString() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "AuthType=" + request.getAuthType() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "ContextPath=" + request.getContextPath() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "Method=" + request.getMethod() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "PathInfo=" + request.getPathInfo() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "PathTranslated=" + request.getPathTranslated() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "QueryString=" + request.getQueryString() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "RemoteUser=" + request.getRemoteUser() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "RequestURI=" + request.getRequestURI() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "RequestURL=" + request.getRequestURL() + SystemUtils.LINE_SEPARATOR;
      returnValue += "|" + tab + "ServletPath=" + request.getServletPath() + SystemUtils.LINE_SEPARATOR;
    }

    {
      returnValue += separatorLigne;
      returnValue += "|" + tab + "HEADERS : " + SystemUtils.LINE_SEPARATOR;
      Enumeration<String> enumsNames = request.getHeaderNames();
      while (enumsNames.hasMoreElements()) {
        String enumName = (String) enumsNames.nextElement();
        returnValue += "|" + tab + enumName + "=" + request.getHeader(enumName) + SystemUtils.LINE_SEPARATOR;
      }
    }

    {
      returnValue += separatorLigne;
      returnValue += "|" + tab + "ATTRIBUTES : " + SystemUtils.LINE_SEPARATOR;
      Enumeration<String> enumsNames = request.getAttributeNames();
      while (enumsNames.hasMoreElements()) {
        String enumName = (String) enumsNames.nextElement();
        returnValue += "|" + tab + enumName + "="
            + ToStringBuilder.reflectionToString(request.getAttribute(enumName), ToStringStyle.SHORT_PREFIX_STYLE)
            + SystemUtils.LINE_SEPARATOR;
      }
    }

    {
      returnValue += separatorLigne;
      returnValue += "|" + tab + "PARAMETERS : " + SystemUtils.LINE_SEPARATOR;
      Enumeration<String> enumsNames = request.getParameterNames();
      while (enumsNames.hasMoreElements()) {
        String enumName = (String) enumsNames.nextElement();
        String[] chaines = request.getParameterValues(enumName);
        for (String string : chaines) {
          returnValue += "|" + tab + enumName + "=" + string + SystemUtils.LINE_SEPARATOR;
        }
      }
    }

    returnValue += separatorLigne;
    return returnValue;
  }

}
