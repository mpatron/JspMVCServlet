package org.jobjects.mvc.event;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Class a implementé pour avoir un evenement utilisable par le controleur.
 * 
 * @author Mickael Patron
 * @version $Date: 2010/04/13 08:14:35 $
 * @version 1.95
 * 
 */
public abstract class AbstractEvent {

  private transient Log log = LogFactory.getLog(getClass());

  /**
   * url relative d'où provient l'évenement exemple :
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
   * première étape : chargement du bean
   * 
   * @param <T>
   * @param request
   * @param classe
   * @return
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws InstantiationException
   * @throws NoSuchMethodException
   */
  public final <T> T loadJspBean2(final HttpServletRequest request, final Class<T> classe) throws IllegalAccessException,
      InvocationTargetException, InstantiationException, NoSuchMethodException {
    PropertyUtilsBean pub = new PropertyUtilsBean();
    final T returnValue = classe.newInstance();
    final PropertyDescriptor[] pds = pub.getPropertyDescriptors(classe);
    for (PropertyDescriptor propertyDescriptor : pds) {
      if ("java.lang.String[]".equals(propertyDescriptor.getPropertyType().getCanonicalName())) {
        final String fieldName = propertyDescriptor.getName();
        final String values[] = request.getParameterValues(fieldName);
        for (int i = 0; (values != null) && (i < values.length); i++) {
          values[i] = StringUtils.trimToEmpty(values[i]);
        }
        PropertyUtils.setSimpleProperty(returnValue, fieldName, values);
      } else if ("java.lang.String".equals(propertyDescriptor.getPropertyType().getCanonicalName())) {
        final String fieldName = propertyDescriptor.getName();
        final String value = StringUtils.trimToEmpty(request.getParameter(fieldName));
        BeanUtils.setProperty(returnValue, fieldName, value);
        log.debug("  -" + fieldName + "=" + value);
      } else if ("java.lang.Class".equals(propertyDescriptor.getPropertyType().getCanonicalName())) {
        log.debug("Passage sur le parametre java.lang.Class d'un bean qui n'est pas utile.");
      } else {
        new IllegalArgumentException("Type non supportee par le marshalling MVC : "
            + propertyDescriptor.getPropertyType().getCanonicalName());
      }
    }
    return returnValue;
  }

  /**
   * première étape : chargement du bean
   * 
   * @param <T>
   * @param request
   * @param classe
   * @return
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws InstantiationException
   * @throws NoSuchMethodException
   * @throws ClassNotFoundException
   */
  @SuppressWarnings("unchecked")
  public final <T> T loadJspBean3(final HttpServletRequest request, final Class<T> classe) throws IllegalAccessException,
      InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
    final T returnValue = classe.newInstance();

    PropertyUtilsBean bub = new PropertyUtilsBean();
    Map<String, ?> map = (Map<String, ?>) bub.describe(returnValue);
    map.remove("class");
    for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
      String fieldName = iterator.next();
      String propertyType = bub.getPropertyDescriptor(returnValue, fieldName).getPropertyType().getCanonicalName();
      if ("java.lang.String".equals(propertyType)) {
        final String value = StringUtils.trimToEmpty(request.getParameter(fieldName));
        PropertyUtils.setProperty(returnValue, fieldName, value);
      } else if ("java.lang.String[]".equals(propertyType)) {
        final String values[] = request.getParameterValues(fieldName);
        for (int i = 0; (values != null) && (i < values.length); i++) {
          values[i] = StringUtils.trimToEmpty(values[i]);
        }
        PropertyUtils.setSimpleProperty(returnValue, fieldName, values);
      } else { // Alors on a un bean perso
        Object value = Class.forName(propertyType).newInstance();
        PropertyUtils.setProperty(returnValue, fieldName, value);

        Map<String, ?> mapBean = (Map<String, ?>) bub.describe(value);
        mapBean.remove("class");
        for (Iterator<String> itMapBean = mapBean.keySet().iterator(); itMapBean.hasNext();) {
          String fieldNameBean = itMapBean.next();
          String propertyTypeBean = bub.getPropertyDescriptor(value, fieldNameBean).getPropertyType().getCanonicalName();
          if ("java.lang.String".equals(propertyTypeBean)) {
            final String valueBean = StringUtils.trimToEmpty(request.getParameter(fieldName + "." + fieldNameBean));
            PropertyUtils.setNestedProperty(returnValue, fieldName + "." + fieldNameBean, valueBean);
          } else if ("java.lang.String[]".equals(propertyTypeBean)) {
            final String values[] = request.getParameterValues(fieldName + "." + fieldNameBean);
            for (int i = 0; (values != null) && (i < values.length); i++) {
              values[i] = StringUtils.trimToEmpty(values[i]);
            }
            PropertyUtils.setNestedProperty(returnValue, fieldName + "." + fieldNameBean, values);
          }
        }

      }
    }
    return returnValue;
  }

  /**
   * première étape : chargement du bean
   * 
   * @param <T>
   * @param request
   * @param classe
   * @return
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws InstantiationException
   * @throws NoSuchMethodException
   * @throws ClassNotFoundException
   */
  public final <T> T loadJspBean(final HttpServletRequest request, final Class<T> classe) throws IllegalArgumentException,
      InstantiationException, IllegalAccessException {
    final T returnValue = classe.newInstance();
    InstanciatePojo instanciatePojo = new InstanciatePojo();
    instanciatePojo.createInstance(request, returnValue);
    return returnValue;
  }

  /**
   * deuxième étape : validation du bean
   * 
   * @param request
   * @return
   */
  public abstract ErrorMessages validation(HttpServletRequest request);

  /**
   * Troisième étape : exécution de l'action avec redirection (forward) va la
   * page désirer
   * 
   * @param sc
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public abstract void process(final ServletContext context, final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException;

  // public BigDecimal createBigDecimal(final String chaine, final Float defaut)
  // throws NumberFormatException {
  // BigDecimal returnValue = null;
  // // try {
  // if (!StringUtils.isBlank(chaine)) {
  // final BigDecimal myfloat = NumberUtils.createBigDecimal(chaine);
  // if (myfloat == null) {
  // returnValue = new BigDecimal(defaut);
  // } else {
  // returnValue = myfloat;
  // }
  // } else {
  // returnValue = new BigDecimal(defaut);
  // }
  // // } catch (Throwable t) {
  // // final NumberFormatException nfe = new
  // NumberFormatException(StringUtils.EMPTY + chaine
  // // + " n'est pas un réel.");
  // // throw nfe;
  // // }
  // return returnValue;
  // }
}
