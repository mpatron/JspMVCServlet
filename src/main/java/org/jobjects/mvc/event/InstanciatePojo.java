package org.jobjects.mvc.event;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
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
  private static String SEPARATOR_MAP = "$";
  private static String SEPARATOR_OBJECT = ".";

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

  /**
   * java.lang.String -> java.lang.String java.lang.String[] -> java.lang.String
   * xxxxxxxxxxxxxxxx[] -> xxxxxxxxxxxxxxxx null -> null
   */
  private final String getSousType(String propertyType) {
    String returnValue;
    // if (StringUtils.endsWith(propertyType, "[]")) {commons-lang-2.5
    if (StringUtils.indexOf(propertyType, "[]") >= 0) {
      returnValue = StringUtils.left(propertyType, propertyType.length() - 2);
    } else {
      returnValue = propertyType;
    }
    return returnValue;
  }

  protected final Object createInstanceSimple(final String propertyType, final String value) throws InvalidArgumentException {
    Object returnValue = null;
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
        // returnValue = new Short(value == null ? 0 : Short.parseShort(value));
        returnValue = Short.valueOf(value == null ? 0 : Short.parseShort(value));
      } else if ("java.math.BigDecimal".equals(propertyType)) {
        if (!StringUtils.isBlank(value)) {
          returnValue = new BigDecimal(value);
        }
      } else {
        throw new InvalidArgumentException("Argument inconnue propertyType=" + propertyType + " value=" + value);
      }
    } catch (java.lang.Throwable t) {
      throw new InvalidArgumentException("Argument inconnue propertyType=" + propertyType + " value=" + value, t);
    }
    return returnValue;
  }

  private String getValue(String name, List<String[]> listParam) {
    if (listParam == null) {
      return null;
    }
    for (String[] strings : listParam) {
      if (StringUtils.equals(name, strings[0])) {
        return strings[1];
      }
    }
    return null;
  }

  private List<String> getValues(String fieldName, List<String[]> listParam) {
    if (listParam == null) {
      return null;
    }
    List<String> returnValue = null;
    for (String[] value : listParam) {
      if (StringUtils.equals(fieldName, value[0])) {
        if (returnValue == null) {
          returnValue = new ArrayList<String>();
        }
        returnValue.add(value[1]);
      }
    }
    return returnValue;
  }

  private Map<String, String> getValuesMap(String fieldName, List<String[]> listParam) {
    if (listParam == null) {
      return null;
    }
    Map<String, String> returnValue = null;
    for (String[] value : listParam) {
      if (StringUtils.contains(value[0], SEPARATOR_MAP)
          && StringUtils.equals(fieldName, StringUtils.substringBefore(value[0], SEPARATOR_MAP))) {
        if (returnValue == null) {
          returnValue = new HashMap<String, String>();
        }
        String key = StringUtils.substringAfter(value[0], SEPARATOR_MAP);
        returnValue.put(key, value[1]);
      }
    }
    return returnValue;
  }

  private List<String[]> getChildValues(String fieldName, List<String[]> listParam) {
    if (listParam == null) {
      return null;
    }
    List<String[]> returnValue = null;
    for (String[] value : listParam) {
      if (StringUtils.contains(value[0], SEPARATOR_OBJECT)
          && StringUtils.equals(fieldName, StringUtils.substringBefore(value[0], SEPARATOR_OBJECT))) {
        if (returnValue == null) {
          returnValue = new ArrayList<String[]>();
        }
        String newValue0 = StringUtils.substringAfter(value[0], SEPARATOR_OBJECT);
        String[] tab = new String[2];
        tab[0] = newValue0;
        tab[1] = value[1];
        returnValue.add(tab);
      }
    }
    return returnValue;
  }

  /**
   * marche pas pour int byte float etc. uniquement pour les object
   * 
   * @param propertyType
   * @param values
   * @return
   * @throws InvalidArgumentException
   */
  protected final Object createInstanceArray(final String propertyType, final String[] values) throws InvalidArgumentException {
    Object returnValue = null;
    try {
      Class<?> propertyTypeClass;
      if (PrimitiveTypeEnum.isPrimitif(propertyType)) {
        propertyTypeClass = PrimitiveTypeEnum.valueOfType(propertyType).getType();
      } else {
        propertyTypeClass = Class.forName(propertyType);
      }

      if (values == null) {
        returnValue = Array.newInstance(propertyTypeClass, 0);
      } else {
        returnValue = Array.newInstance(propertyTypeClass, values.length);
        for (int i = 0; i < values.length; i++) {
          Array.set(returnValue, i, createInstanceSimple(propertyType, values[i]));
        }
      }
    } catch (java.lang.Throwable t) {
      t.printStackTrace();
      throw new InvalidArgumentException("Argument inconnue propertyType=" + propertyType + " values=[" + StringUtils.join(values, ";")
          + "]", t);
    }
    return returnValue;
  }

  public final void createInstance(final HttpServletRequest request, Object myObject) throws InvalidArgumentException {
    @SuppressWarnings("unchecked")
    Enumeration<String> en = request.getParameterNames();
    List<String[]> listParam = new ArrayList<String[]>();
    while (en.hasMoreElements()) {
      String key = (String) en.nextElement();
      String[] values = request.getParameterValues(key);
      for (int i = 0; (values != null) && (i < values.length); i++) {
        String[] value = new String[2];
        value[0] = key;
        value[1] = values[i];
        listParam.add(value);
      }
    }
    pupolateObject(myObject, listParam);
  }

  @SuppressWarnings("unchecked")
  public void pupolateObject(Object myObject, List<String[]> listParam) throws InvalidArgumentException {
    String fieldName = StringUtils.EMPTY;
    String propertyType = StringUtils.EMPTY;
    try {
      Map<String, ?> map = (Map<String, ?>) PropertyUtils.describe(myObject);
      map.remove("class");
      for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
        fieldName = iterator.next();
        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(myObject, fieldName);
        propertyType = propertyDescriptor.getPropertyType().getCanonicalName();

        if (listTypes.contains(getSousType(propertyType))) { // Si c'est un type
                                                             // simple ou
                                                             // tableau de type
                                                             // simple
          if (StringUtils.indexOf(propertyType, "[]") >= 0) { // Si c'est
                                                              // tableau de type
                                                              // simple
            List<String> f = getValues(fieldName, listParam);
            String values[]=null;
            if (f != null) {
              values = f.toArray(new String[0]);              
            }
            /* un tableau est toujours instancié dans les pojo, il peut avoir un tableau vide.*/
            PropertyUtils.setNestedProperty(myObject, fieldName, createInstanceArray(getSousType(propertyType), values));
          } else { // Si c'est un type simple (int, long, Integer, etc...)
            String value = getValue(fieldName, listParam);
            if (value != null) {
              PropertyUtils.setNestedProperty(myObject, fieldName, createInstanceSimple(propertyType, value));
            }
          }
        } else if ("java.util.List".equals(propertyType)) {
          List<String> f = getValues(fieldName, listParam);
          if (f != null) {
            if (PropertyUtils.getProperty(myObject, fieldName) == null) {
              PropertyUtils.setNestedProperty(myObject, fieldName, f);
            } else {
              ((List<String>) PropertyUtils.getProperty(myObject, fieldName)).addAll(f);
            }
          }
        } else if ("java.util.Map".equals(propertyType)) {
          Map<String, String> f = getValuesMap(fieldName, listParam);
          if (f != null) {
            if (PropertyUtils.getProperty(myObject, fieldName) == null) {
              PropertyUtils.setNestedProperty(myObject, fieldName, f);
            } else {
              ((Map<String, String>) PropertyUtils.getProperty(myObject, fieldName)).putAll(f);
            }
          }
        } else { // Si c'est une class
          Object child = Class.forName(propertyType).newInstance();
          PropertyUtils.setProperty(myObject, fieldName, child);
          pupolateObject(child, getChildValues(fieldName, listParam));
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new InvalidArgumentException("Argument inconnue myObject="
          + ToStringBuilder.reflectionToString(myObject, ToStringStyle.MULTI_LINE_STYLE) + " fieldName=" + fieldName + " propertyType="
          + propertyType, e);
    }

  }
}
