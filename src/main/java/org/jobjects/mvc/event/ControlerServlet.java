package org.jobjects.mvc.event;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet faisant office de controler. 
 * <ol>
 * <li>Chargement de l'action spécifié par le paramétre "action" (une implementation de fr.acmn.portadmin.parametres.servlet.event.Event</li>
 * <li>Chargement de fr.acmn.portadmin.parametres.servlet.jsp.* representant l'ensemble des attributs de la page</li>
 * <li>Excution de la méthode "validation" de l'implémention</li>
 * <li>S'il n'y a pas d'erreur, execution de la méthode "process" de l'implémention, c'est l'implementation qui choisi la jsp de destination.</li>
 * <li>S'il y a une erreur, retour sur la jsp source choisi par l'implementation.</li>
 * </ol>
 * <ul>
 * Liste des instances pasé en attributs à la page JSP
 * <li>Une instance de jspBean est passé en attribut à la page JSP contenant le bean des attributs de la pages.</li>
 * <li>Une instance de errors est passé en attribut à la page JSP contenant la liste de erreurs.</li>
 * </ul>
 * @author Mickael Patron
 * @version $Date: 2010/04/13 08:14:35 $
 * @version 1.95
 * 
 */
public class ControlerServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private transient Log log = LogFactory.getLog(getClass());
  
  private Map<String, AbstractEvent> events = new TreeMap<String, AbstractEvent>();

  /**
   * @see HttpServlet#HttpServlet()
   */
  public ControlerServlet() {
    super();
  }

  @SuppressWarnings("unchecked")
  @Override
  public final void init(ServletConfig config) throws ServletException {
    super.init(config);
    Enumeration<String> parameterNames=getInitParameterNames();
    while (parameterNames.hasMoreElements()) {
      String parameterName = parameterNames.nextElement();
      String eventName= getInitParameter(parameterName);
      try {
        AbstractEvent event = (AbstractEvent) Class.forName(eventName).newInstance();
        events.put(parameterName, event);
      } catch (InstantiationException e) {
        String message=Constants.ACTION.name()+"="+parameterName+" n'est pas une classe instanciable.";
        log(message, e);
        throw new ServletException(message, e);
      } catch (IllegalAccessException e) {
        String message="La classe "+ eventName + " n'est pas authorisé à être instancié.";
        log(message, e);
        throw new ServletException(message, e);
      } catch (ClassNotFoundException e) {
        String message=Constants.ACTION.name()+"="+parameterName+" n'est pas une classe instanciable.";
        log(message, e);
        throw new ServletException(message, e);
      }  
    }
  }
  
  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  public final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doRun(request, response);
  }
  
  
  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  public final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doRun(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  public final void doRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    //log.info(afficheHttpServletRequest(request));
    
    String actionName = (String) request.getParameter(Constants.ACTION.name());
    AbstractEvent event = null;
    if (!StringUtils.isBlank(actionName)) {
      event = events.get(actionName);
      if(event==null) {
        String message=Constants.ACTION.name()+"="+actionName+" n'est pas une classe instanciable.";
        log(message);
        throw new ServletException(message);
      }
    } else {
      String message="Le paramétre " + Constants.ACTION.name()+" n'a pas été fourni en paramétre de la request.";
      log(message);
      throw new ServletException(message);
    }

    try {
      JspBean jspBean = (JspBean) event.loadJspBean(request, event.getJspBean());
      request.setAttribute(Constants.JSPBEAN.name(), jspBean);
    } catch (Exception e) {
      log("L'evenement \"" + actionName + "\" n'a pas de bean \"" + Constants.JSPBEAN.name() + "\"", e);
    }

    ErrorMessages errorMessages = event.validation(request);
    if (errorMessages.getErrorMessages().size() == 0) {
      event.process(getServletContext(), request, response);
    } else {
      request.setAttribute(Constants.ERRORS.name(), errorMessages);
      getServletContext().getRequestDispatcher(event.getFromUrl()).forward(request, response);
      return;
    }
  }

  @SuppressWarnings("unchecked")
  private String afficheHttpServletRequest(HttpServletRequest request) {
    String returnValue=StringUtils.EMPTY;
    String tab = "  ";
    String separatorLigne = StringUtils.repeat("-", 80)+SystemUtils.LINE_SEPARATOR;

    {
      returnValue+=separatorLigne;
      returnValue+="|" + tab+ "Informations diverses : "+SystemUtils.LINE_SEPARATOR;;
      returnValue+="|" + tab+ "AuthType=" + request.getAuthType()+SystemUtils.LINE_SEPARATOR;
      returnValue+="|" + tab+ "ContextPath=" + request.getContextPath()+SystemUtils.LINE_SEPARATOR;
      returnValue+="|" + tab+ "Method=" + request.getMethod()+SystemUtils.LINE_SEPARATOR;
      returnValue+="|" + tab+ "PathInfo=" + request.getPathInfo()+SystemUtils.LINE_SEPARATOR;
      returnValue+="|" + tab+ "PathTranslated=" + request.getPathTranslated()+SystemUtils.LINE_SEPARATOR;
      returnValue+="|" + tab+ "QueryString=" + request.getQueryString()+SystemUtils.LINE_SEPARATOR;
      returnValue+="|" + tab+ "RemoteUser=" + request.getRemoteUser()+SystemUtils.LINE_SEPARATOR;
      returnValue+="|" + tab+ "RequestURI=" + request.getRequestURI()+SystemUtils.LINE_SEPARATOR;
      returnValue+="|" + tab+ "RequestURL=" + request.getRequestURL()+SystemUtils.LINE_SEPARATOR;
      returnValue+="|" + tab+ "ServletPath=" + request.getServletPath()+SystemUtils.LINE_SEPARATOR;
    }
    
    {
      returnValue+=separatorLigne;
      returnValue+="|" + tab+ "HEADERS : "+SystemUtils.LINE_SEPARATOR;;
      Enumeration<String> enumsNames =request.getHeaderNames();
      while (enumsNames.hasMoreElements()) {
        String enumName = (String) enumsNames.nextElement();
        returnValue+="|" + tab+ enumName + "=" + request.getAttribute(enumName)+SystemUtils.LINE_SEPARATOR;
      }
    }
    
    {
      returnValue+=separatorLigne;
      returnValue+="|" + tab+ "ATTRIBUTES : "+SystemUtils.LINE_SEPARATOR;;
      Enumeration<String> enumsNames =request.getAttributeNames();
      while (enumsNames.hasMoreElements()) {
        String enumName = (String) enumsNames.nextElement();
        returnValue+="|" + tab+ enumName + "=" + request.getAttribute(enumName)+SystemUtils.LINE_SEPARATOR;
      }
    }
    
    {
      returnValue+=separatorLigne;
      returnValue+="|" + tab+ "PARAMETERS : "+SystemUtils.LINE_SEPARATOR;;
      Enumeration<String> enumsNames =request.getParameterNames();
      while (enumsNames.hasMoreElements()) {
        String enumName = (String) enumsNames.nextElement();
        returnValue+="|" + tab+ enumName + "=" + request.getAttribute(enumName)+SystemUtils.LINE_SEPARATOR;
      }
    }
    
    returnValue+=separatorLigne;
    return returnValue;
  }
  
}
