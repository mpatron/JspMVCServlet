package org.jobjects.mvc.event;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class InstanciatePojo {

  private List<String> listTypes;

  public InstanciatePojo() {
    String[] types = new String[] {
                                   "java.lang.String",
                                   "boolean",
                                   "java.lang.Boolean",
                                   "byte",
                                   "java.lang.Byte",
                                   "double",
                                   "java.lang.Double",
                                   "float",
                                   "java.lang.Float",
                                   "long",
                                   "java.lang.Long",
                                   "int",
                                   "java.lang.Integer",
                                   "short",
                                   "java.lang.Short",
                                   "java.math.BigDecimal" };
    listTypes = Arrays.asList(types);
  }

  public final void createInstance(final HttpServletRequest request, Object myObject) throws InvalidArgumentException {
    instanceChildPojo(myObject);
    createInstanceRec(request, myObject, null);
  }

  @SuppressWarnings("unchecked")
  protected final void createInstanceRec(final HttpServletRequest request, Object myObject, String parentName)
      throws InvalidArgumentException {
    String fieldName = StringUtils.EMPTY;
    String propertyType = StringUtils.EMPTY;
    try {
      Map<String, ?> map = (Map<String, ?>) PropertyUtils.describe(myObject);
      map.remove("class");
      for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
        fieldName = iterator.next();
        propertyType = PropertyUtils.getPropertyDescriptor(myObject, fieldName).getPropertyType().getCanonicalName();
        if (!listTypes.contains(getSousType(propertyType))) {
          createInstanceRec(request, PropertyUtils.getProperty(myObject, fieldName), fieldName);
        } else {
          // if (StringUtils.endsWith(propertyType, "[]")) {commons-lang-2.5
          if (StringUtils.indexOf(propertyType, "[]") >= 0) {
            final String values[] = request.getParameterValues(StringUtils.isBlank(parentName) ? fieldName : parentName + "." + fieldName);
            if(PrimitiveTypeEnum.isPrimitif(getSousType(propertyType))) {
              PropertyUtils.setNestedProperty(myObject, fieldName, Array.newInstance(PrimitiveTypeEnum.valueOfType(getSousType(propertyType)).getType(), 0));
            } else {
              PropertyUtils.setNestedProperty(myObject, fieldName, createInstanceArray(propertyType, values));
            }
          } else {
            final String value = request.getParameter(StringUtils.isBlank(parentName) ? fieldName : parentName + "." + fieldName);
            PropertyUtils.setNestedProperty(myObject, fieldName, createInstanceSimple(propertyType, value));
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new InvalidArgumentException("Argument inconnue myObject="
          + ToStringBuilder.reflectionToString(myObject, ToStringStyle.MULTI_LINE_STYLE) + " parentName=" + parentName + " fieldName="
          + fieldName + " propertyType=" + propertyType, e);
    }
  }

  /**
   * Instancie toutes les propri�t�s qui sont est object par une instance. class
   * A { String c; B b; } donne : class A { String c=null; B b=new B(); }
   * 
   * @param myObject
   * @throws InvalidArgumentException
   */
  @SuppressWarnings("unchecked")
  protected final void instanceChildPojo(Object myObject) throws InvalidArgumentException {
    String fieldName = StringUtils.EMPTY;
    String propertyType = StringUtils.EMPTY;
    try {
      Map<String, ?> map = (Map<String, ?>) PropertyUtils.describe(myObject);
      map.remove("class");
      for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
        fieldName = iterator.next();
        propertyType = PropertyUtils.getPropertyDescriptor(myObject, fieldName).getPropertyType().getCanonicalName();
        /*
         * recherche que les types autre que type simple
         */
        if (!listTypes.contains(getSousType(propertyType))) {
          if (PropertyUtils.getProperty(myObject, fieldName) == null) {
            Object child = Class.forName(propertyType).newInstance();
            PropertyUtils.setProperty(myObject, fieldName, child);
            instanceChildPojo(child);
          }
        }
      }
    } catch (Exception e) {
      throw new InvalidArgumentException("Argument inconnue fieldName=" + fieldName + " propertyType=" + propertyType, e);
    }
  }

  /**
   * java.lang.String -> java.lang.String java.lang.String[] -> java.lang.String
   * xxxxxxxxxxxxxxxx[] -> xxxxxxxxxxxxxxxx null -> null
   */
  protected final String getSousType(String propertyType) {
    String returnValue;
    // if (StringUtils.endsWith(propertyType, "[]")) {commons-lang-2.5
    if (StringUtils.indexOf(propertyType, "[]") >= 0) {
      returnValue = StringUtils.left(propertyType, propertyType.length() - 2);
    } else {
      returnValue = propertyType;
    }
    return returnValue;
  }

  /**
   * marche pas pour int byte float etc.
   * uniquement pour les object
   * @param propertyType
   * @param values
   * @return
   * @throws InvalidArgumentException
   */
  protected final Object[] createInstanceArray(final String propertyType, final String[] values) throws InvalidArgumentException {
    Object[] returnValue = null;
    try {
      if (values == null) {
        returnValue = (Object[]) Array.newInstance(Class.forName(getSousType(propertyType)), 0);
      } else {
        String sousType = getSousType(propertyType);
        returnValue = (Object[]) Array.newInstance(Class.forName(sousType), values.length);
        for (int i = 0; i < values.length; i++) {
          returnValue[i] = createInstanceSimple(sousType, values[i]);
        }
      }
    } catch (java.lang.Throwable t) {
      throw new InvalidArgumentException("Argument inconnue propertyType=" + propertyType + " values=[" + StringUtils.join(values, ";")
          + "]", t);
    }
    return returnValue;
  }

  protected final Object createInstanceSimple(final String propertyType, final String value) throws InvalidArgumentException {
    Object returnValue;
    try {
      if ("java.lang.String".equals(propertyType)) {
        returnValue = StringUtils.trimToEmpty(value);
      } else if (("boolean".equals(propertyType) || ("java.lang.Boolean".equals(propertyType)))) {
        returnValue = BooleanUtils.toBoolean(value);
      } else if (("byte".equals(propertyType) || ("java.lang.Byte".equals(propertyType)))) {
        returnValue = new Byte(value == null ? (byte) 0 : Byte.parseByte(value));
      } else if (("double".equals(propertyType) || ("java.lang.Double".equals(propertyType)))) {
        returnValue = new Double(value == null ? 0 : Double.parseDouble(value));
      } else if (("float".equals(propertyType) || ("java.lang.Float".equals(propertyType)))) {
        returnValue = new Float(value == null ? 0 : Float.parseFloat(value));
      } else if (("long".equals(propertyType) || ("java.lang.Long".equals(propertyType)))) {
        returnValue = new Long(value == null ? 0 : Long.parseLong(value));
      } else if (("int".equals(propertyType) || ("java.lang.Integer".equals(propertyType)))) {
        returnValue = Integer.valueOf(value == null ? 0 : Integer.parseInt(value));
      } else if (("short".equals(propertyType) || ("java.lang.Short".equals(propertyType)))) {
        returnValue = new Short(value == null ? 0 : Short.parseShort(value));
      } else if ("java.math.BigDecimal".equals(propertyType)) {
        returnValue = new BigDecimal(value);
      } else {
        throw new InvalidArgumentException("Argument inconnue propertyType=" + propertyType + " value=" + value);
      }
    } catch (java.lang.Throwable t) {
      throw new InvalidArgumentException("Argument inconnue propertyType=" + propertyType + " value=" + value, t);
    }
    return returnValue;
  }

}
