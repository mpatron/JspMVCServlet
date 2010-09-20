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

  public void createInstance(final HttpServletRequest request, Object myObject) throws IllegalArgumentException {
    instanceChildPojo(myObject);
    createInstanceRec(request, myObject, null);
  }

  @SuppressWarnings("unchecked")
  protected void createInstanceRec(final HttpServletRequest request, Object myObject, String parentName) throws IllegalArgumentException {
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
          if (StringUtils.endsWith(propertyType, "[]")) {
            final String values[] = request.getParameterValues(StringUtils.isBlank(parentName) ? fieldName : parentName + "." + fieldName);
            PropertyUtils.setNestedProperty(myObject, fieldName, createInstanceArray(propertyType, values));
          } else {
            final String value = request.getParameter(StringUtils.isBlank(parentName) ? fieldName : parentName + "." + fieldName);
            PropertyUtils.setNestedProperty(myObject, fieldName, createInstanceSimple(propertyType, value));
          }
        }
      }

    } catch (Throwable e) {
      throw new IllegalArgumentException("Argument inconnue myObject="
          + ToStringBuilder.reflectionToString(myObject, ToStringStyle.MULTI_LINE_STYLE) + " parentName=" + parentName + " fieldName="
          + fieldName + " propertyType=" + propertyType);
    }
  }

  @SuppressWarnings("unchecked")
  protected void instanceChildPojo(Object myObject) throws IllegalArgumentException {
    String fieldName = StringUtils.EMPTY;
    String propertyType = StringUtils.EMPTY;
    try {
      Map<String, ?> map = (Map<String, ?>) PropertyUtils.describe(myObject);
      map.remove("class");
      for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
        fieldName = iterator.next();
        propertyType = PropertyUtils.getPropertyDescriptor(myObject, fieldName).getPropertyType().getCanonicalName();
        if (!listTypes.contains(getSousType(propertyType))) {
          if (PropertyUtils.getProperty(myObject, fieldName) == null) {
            Object child = Class.forName(propertyType).newInstance();
            PropertyUtils.setProperty(myObject, fieldName, child);
            instanceChildPojo(child);
          }
        }
      }
    } catch (Throwable e) {
      throw new IllegalArgumentException("Argument inconnue fieldName=" + fieldName + " propertyType=" + propertyType);
    }
  }

  /**
   * java.lang.String -> java.lang.String java.lang.String[] -> java.lang.String
   * xxxxxxxxxxxxxxxx[] -> xxxxxxxxxxxxxxxx null -> null
   */
  protected String getSousType(String propertyType) {
    String returnValue;
    if (StringUtils.endsWith(propertyType, "[]")) {
      returnValue = StringUtils.left(propertyType, propertyType.length() - 2);
    } else {
      returnValue = propertyType;
    }
    return returnValue;
  }

  protected Object[] createInstanceArray(final String propertyType, final String[] values) throws IllegalArgumentException {
    if (values == null) {
      return null;
    }
    Object[] returnValue = null;
    try {
      String sousType = getSousType(propertyType);
      returnValue = (Object[]) Array.newInstance(Class.forName(sousType), values.length);
      for (int i = 0; i < values.length; i++) {
        returnValue[i] = createInstanceSimple(sousType, values[i]);
      }
    } catch (java.lang.Throwable t) {
      throw new IllegalArgumentException("Argument inconnue propertyType=" + propertyType + " values=[" + StringUtils.join(values, ";")
          + "]");
    }
    return returnValue;
  }

  private String[] types = new String[] { "java.lang.String", "boolean", "java.lang.Boolean", "byte", "java.lang.Byte", "double",
      "java.lang.Double", "float", "java.lang.Float", "long", "java.lang.Long", "int", "java.lang.Integer", "short", "java.lang.Short",
      "java.math.BigDecimal" };
  private List<String> listTypes = Arrays.asList(types);


  protected Object createInstanceSimple(final String propertyType, final String value) throws IllegalArgumentException {
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
        returnValue = new Integer(value == null ? 0 : Integer.parseInt(value));
      } else if (("short".equals(propertyType) || ("java.lang.Short".equals(propertyType)))) {
        returnValue = new Short(value == null ? 0 : Short.parseShort(value));
      } else if ("java.math.BigDecimal".equals(propertyType)) {
        returnValue = new BigDecimal(value);
      } else {
        throw new IllegalArgumentException("Argument inconnue propertyType=" + propertyType + " value=" + value);
      }
    } catch (java.lang.Throwable t) {
      throw new IllegalArgumentException("Argument inconnue propertyType=" + propertyType + " value=" + value);
    }
    return returnValue;
  }

}
