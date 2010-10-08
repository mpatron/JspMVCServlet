package org.jobjects.mvc.event;

import java.util.EnumSet;

import org.apache.commons.lang.StringUtils;

/**
 * boolean, byte, short, int, long, char, float, and double
 * 
 * @author patronmi
 * 
 */
public enum PrimitiveTypeEnum {
  BOOLEAN("boolean", Boolean.TYPE),
  SHORT("short", Short.TYPE),
  INT("int", Integer.TYPE),
  LONG("long", Long.TYPE),
  CHAR("char", Character.TYPE),
  FLOAT("float", Float.TYPE),
  DOUBLE("double", Double.TYPE);

  private PrimitiveTypeEnum(String typeName, Class<?> type) {
    this.typeName = typeName;
    this.type = type;
  }

  private String typeName;
  private Class<?> type;

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public Class<?> getType() {
    return type;
  }

  public void setType(Class<?> type) {
    this.type = type;
  }

  public static boolean isPrimitif(String typeName) {
    boolean returnValue=Boolean.FALSE; 
    try {
      EnumSet<PrimitiveTypeEnum> ee = EnumSet.allOf(PrimitiveTypeEnum.class);
      for (PrimitiveTypeEnum en : ee) {
        if(StringUtils.equals(en.getTypeName(), typeName)) {
          returnValue=Boolean.TRUE;
          break;
        }
      }
    } catch (Exception e) {
      returnValue=Boolean.FALSE;
    }
    return returnValue;
  }
  
  public static PrimitiveTypeEnum valueOfType(String typeName) {
    PrimitiveTypeEnum returnValue=null;
    try {
      EnumSet<PrimitiveTypeEnum> ee = EnumSet.allOf(PrimitiveTypeEnum.class);
      for (PrimitiveTypeEnum en : ee) {
        if(StringUtils.equals(en.getTypeName(), typeName)) {
          returnValue=en;
          break;
        }
      }
    } catch (Exception e) {
      //Ne rien faire, renvoit de null.
    }
    return returnValue;
  }
}
