package org.jobjects.mvc.event;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.jobjects.mvc.event.beans.ClassPojo;

public class Main {

  /**
   * @param args
   * @throws NoSuchMethodException 
   * @throws InvocationTargetException 
   * @throws IllegalAccessException 
   * @throws NegativeArraySizeException 
   */
  public static void main(String[] args) throws NegativeArraySizeException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    ClassPojo pojo = new ClassPojo();
    
    PropertyUtils.setProperty(pojo, "monTableauInt", Array.newInstance(Integer.TYPE, 5));
    System.out.println("coucou"+pojo.getMonTableauInt().length);

    System.out.println("ee"+PrimitiveTypeEnum.isPrimitif("toto"));
    System.out.println("ee"+PrimitiveTypeEnum.isPrimitif("int"));
    
  }

}
