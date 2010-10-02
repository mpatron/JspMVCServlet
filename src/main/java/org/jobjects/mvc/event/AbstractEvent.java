package org.jobjects.mvc.event;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Class a implement� pour avoir un evenement utilisable par le controleur.
 * 
 * @author Mickael Patron
 * @version $Date: 2010/04/13 08:14:35 $
 * @version 1.95
 * 
 */
public abstract class AbstractEvent {

  /**
   * url relative d'o� provient l'�venement exemple :
   * "/"+affiliateID+"/"+EventList.PROFIL_FORMULE_SAISI.getViewName();
   * 
   * @return
   */
  public abstract String getFromUrl();

  /**
   * Nom de la de la variable de l'attribut contenant le bean example :
   * supportSpecialBean
   * 
   * @return
   */
  public abstract Class<? extends JspBean> getJspBean();

  /**
   * premi�re �tape : chargement du bean
   * 
   * @param <T>
   * @param request
   * @param classe
   * @return
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws InvalidArgumentException
   */
  public final <T> T loadJspBean(final HttpServletRequest request, final Class<T> classe) throws InstantiationException,
      IllegalAccessException, InvalidArgumentException {
    final T returnValue = classe.newInstance();
    InstanciatePojo instanciatePojo = new InstanciatePojo();
    instanciatePojo.createInstance(request, returnValue);
    return returnValue;
  }

  /**
   * deuxi�me �tape : validation du bean
   * 
   * @param request
   * @return
   */
  public abstract ErrorMessages validation(HttpServletRequest request);

  /**
   * Troisi�me �tape : ex�cution de l'action avec redirection (forward) va la
   * page d�sirer
   * 
   * @param sc
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public abstract void process(final ServletContext context, final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException;

}
