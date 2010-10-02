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

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Servlet faisant office de controler.
 * <ol>
 * <li>Chargement de l'action spécifié par le paramétre "action" (une
 * implementation de fr.acmn.portadmin.parametres.servlet.event.Event</li>
 * <li>Chargement de fr.acmn.portadmin.parametres.servlet.jsp.* representant
 * l'ensemble des attributs de la page</li>
 * <li>Excution de la méthode "validation" de l'implémention</li>
 * <li>S'il n'y a pas d'erreur, execution de la méthode "process" de
 * l'implémention, c'est l'implementation qui choisi la jsp de destination.</li>
 * <li>S'il y a une erreur, retour sur la jsp source choisi par
 * l'implementation.</li>
 * </ol>
 * <ul>
 * Liste des instances pasé en attributs à la page JSP
 * <li>Une instance de jspBean est passé en attribut à la page JSP contenant le
 * bean des attributs de la pages.</li>
 * <li>Une instance de errors est passé en attribut à la page JSP contenant la
 * liste de erreurs.</li>
 * </ul>
 * 
 * @author Mickael Patron
 * @version $Date: 2010/04/13 08:14:35 $
 * @version 1.95
 * 
 */
public class ControlerServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private boolean onWorkingMode = true;
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
    Enumeration<String> parameterNames = getInitParameterNames();
    while (parameterNames.hasMoreElements()) {
      String parameterName = parameterNames.nextElement();
      String eventName = getInitParameter(parameterName);
      if ("onWorkingMode".equalsIgnoreCase(parameterName)) {
        onWorkingMode = BooleanUtils.toBoolean(getInitParameter(parameterName));
      } else {
        try {
          AbstractEvent event = (AbstractEvent) Class.forName(eventName).newInstance();
          events.put(parameterName, event);
        } catch (InstantiationException e) {
          String message = Constants.action.name() + "=" + parameterName + " n'est pas une classe instanciable.";
          log(message, e);
          throw new ServletException(message, e);
        } catch (IllegalAccessException e) {
          String message = "La classe " + eventName + " n'est pas authorisé à être instancié.";
          log(message, e);
          throw new ServletException(message, e);
        } catch (ClassNotFoundException e) {
          String message = Constants.action.name() + "=" + parameterName + " n'est pas une classe instanciable.";
          log(message, e);
          throw new ServletException(message, e);
        }
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
    String actionName = (String) request.getParameter(Constants.action.name());
    AbstractEvent event = null;

    if (StringUtils.isBlank(actionName)) {
      String message = "Le paramètre " + Constants.action.name() + " n'a pas été fourni en paramètre de la request.";
      if (onWorkingMode) {
        ControlerErrorTools controlerErrorTools = new ControlerErrorTools();
        controlerErrorTools.regenereHtml(request, response, message);
      } else {
        log(message);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
      }
      return;
    } else {
      event = events.get(actionName);
      if (event == null) {
        String message = Constants.action.name() + "=" + actionName + " n'est pas une classe instanciable.";
        if (onWorkingMode) {
          ControlerErrorTools controlerErrorTools = new ControlerErrorTools();
          controlerErrorTools.regenereHtml(request, response, message);
        } else {
          log(message);
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
        }
        return;
      }
    }

    try {
      JspBean jspBean = (JspBean) event.loadJspBean(request, event.getJspBean());
      request.setAttribute(Constants.jspBean.name(), jspBean);
    } catch (Exception e) {
      String message = "L'evenement \"" + actionName + "\" n'a pas de bean \"" + Constants.jspBean.name() + "\"."
          + ExceptionUtils.getStackTrace(e);
      if (onWorkingMode) {
        ControlerErrorTools controlerErrorTools = new ControlerErrorTools();
        controlerErrorTools.regenereHtml(request, response, message);
      } else {
        log(message);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
      }
      return;
    }

    ErrorMessages errorMessages = event.validation(request);
    if (errorMessages.getErrorMessages().size() == 0) {
      event.process(getServletContext(), request, response);
    } else {
      request.setAttribute(Constants.errors.name(), errorMessages);
      getServletContext().getRequestDispatcher(event.getFromUrl()).forward(request, response);
      return;
    }
  }
}
