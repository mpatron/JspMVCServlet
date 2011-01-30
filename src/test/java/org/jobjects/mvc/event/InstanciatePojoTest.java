package org.jobjects.mvc.event;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jobjects.mvc.event.beans.ClassAlltype;
import org.jobjects.mvc.event.beans.ClassCompose;
import org.jobjects.mvc.event.beans.ClassDeriveCompose;
import org.jobjects.mvc.event.beans.ClassPojo;
import org.junit.Before;
import org.junit.Test;

public class InstanciatePojoTest {

  HttpServletRequest requestMock = null;

  @Before
  public void setUp() throws Exception {
    requestMock = createMock(HttpServletRequest.class);
    // requestMock = createStrictMock(HttpServletRequest.class);
  }

  @Test
  public void testCreateInstance() {

    Vector<String> v = new Vector<String>();
    v.add("composeName");
    v.add("parentName");
    v.add("monTableauString");
    v.add("composition.monchampString");
    v.add("composition.monchampInteger");
    v.add("composition.monTableauInteger");
    v.add("composition.monTableauInt");
    v.add("composition.monchampInt");

    expect(requestMock.getParameterNames()).andReturn(v.elements());

    expect(requestMock.getParameterValues("composeName")).andReturn((String[]) new String[] { "maChaine de ComposeName" });
    expect(requestMock.getParameterValues("parentName")).andReturn((String[]) new String[] { "maChaine de ParentName" });
    expect(requestMock.getParameterValues("monTableauString")).andReturn(
        (String[]) new String[] { "maChaine-a", "maChaine-b", "maChaine-c" });
    expect(requestMock.getParameterValues("composition.monchampString")).andReturn((String[]) new String[] { "azerty" });
    // expect(requestMock.getParameterValues("composition.monTableauString")).andReturn(null);
    expect(requestMock.getParameterValues("composition.monchampInteger")).andReturn((String[]) new String[] { "5" });
    expect(requestMock.getParameterValues("composition.monTableauInteger")).andReturn((String[]) new String[] { "4", "3", "1" });
    expect(requestMock.getParameterValues("composition.monTableauInt")).andReturn(null);
    expect(requestMock.getParameterValues("composition.monchampInt")).andReturn(null);

    /*
     * expect(requestMock.getParameter("composeName")).andReturn((String)
     * "maChaine de ComposeName");
     * expect(requestMock.getParameter("parentName")).andReturn((String)
     * "maChaine de ParentName");
     * expect(requestMock.getParameterValues("monTableauString"
     * )).andReturn((String[]) new String[] { "maChaine-a", "maChaine-b",
     * "maChaine-c" });
     * expect(requestMock.getParameter("composition.monchampString"
     * )).andReturn((String) "azerty");
     * expect(requestMock.getParameterValues("composition.monTableauString"
     * )).andReturn(null);
     * expect(requestMock.getParameter("composition.monchampInteger"
     * )).andReturn((String) "5");
     * expect(requestMock.getParameterValues("composition.monTableauInteger"
     * )).andReturn((String[]) new String[] { "4", "3", "1" });
     * expect(requestMock
     * .getParameterValues("composition.monTableauInt")).andReturn(null);
     * expect(
     * requestMock.getParameter("composition.monchampInt")).andReturn(null);
     */
    replay(requestMock);

    try {
      ClassDeriveCompose myObject = new ClassDeriveCompose();
      InstanciatePojo instanciatePojo = new InstanciatePojo();
      instanciatePojo.createInstance(requestMock, myObject);

      assertEquals("maChaine de ComposeName", myObject.getComposeName());
      assertEquals("maChaine de ParentName", myObject.getParentName());
      assertArrayEquals(new String[] { "maChaine-a", "maChaine-b", "maChaine-c" }, myObject.getMonTableauString());
      assertEquals("azerty", myObject.getComposition().getMonchampString());
      assertEquals(5, (int) myObject.getComposition().getMonchampInteger());
      int[] iComp = new int[] { 4, 3, 1 };
      assertNotNull(myObject.getComposition().getMonTableauInteger());
      assertEquals(iComp.length, myObject.getComposition().getMonTableauInteger().length);
      for (int i = 0; i < iComp.length; i++) {
        int a = iComp[i];
        int b = myObject.getComposition().getMonTableauInteger()[i];
        assertEquals(a, b);
      }

      verify(requestMock);

    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void testCreationDeTableauTypeSimple() {
    ClassPojo pojo = new ClassPojo();
    try {
      PropertyUtils.setProperty(pojo, "monTableauInt", Array.newInstance(Integer.TYPE, 5));
      assertEquals(5, pojo.getMonTableauInt().length);
      assertFalse(PrimitiveTypeEnum.isPrimitif("toto"));
      assertTrue(PrimitiveTypeEnum.isPrimitif("int"));
      
      Object o=PropertyUtils.getProperty(pojo, "monTableauInt");
      int a = 5;
      PropertyUtils.setIndexedProperty(pojo,  "monTableauInt", 0, a);
      PropertyUtils.setIndexedProperty(pojo,  "monTableauInt", 1, a--);
      PropertyUtils.setIndexedProperty(pojo,  "monTableauInt", 2, a--);
      PropertyUtils.setIndexedProperty(pojo,  "monTableauInt", 3, a--);
      o.hashCode();
      //PropertyUtils.setNestedProperty(pojo, "monTableauInt", Array.newInstance(Integer.TYPE, 0));
      
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void testInstanceChildPojo() {
    try {
      ClassDeriveCompose myObject = new ClassDeriveCompose();
      assertNull(myObject.getComposition());
      InstanciatePojo instanciatePojo = new InstanciatePojo();
      //instanciatePojo.instanceChildPojo(myObject);
      instanciatePojo.pupolateObject(myObject, new Vector<String[]>() );
      assertNotNull(myObject.getComposition());
    } catch (InvalidArgumentException e) {
      fail(ExceptionUtils.getFullStackTrace(e));
    }
  }

  @Test
  public void testCreateInstanceArray() {
    try {
      InstanciatePojo instanciatePojo = new InstanciatePojo();
      String[] tString = (String[]) instanciatePojo.createInstanceArray("java.lang.String", new String[] { "abc" });
      String[] checkString = new String[] { "abc" };
      assertEquals(checkString.length, tString.length);

      Boolean[] tBoolean = (Boolean[]) instanciatePojo.createInstanceArray("java.lang.Boolean", new String[] {
                                                                                                              "abc",
                                                                                                              "TrUe",
                                                                                                              "fAlSe",
                                                                                                              "yes" });
      Boolean[] checkBoolean = new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE };
      assertEquals(checkBoolean.length, tBoolean.length);
      for (int i = 0; i < checkBoolean.length; i++) {
        assertEquals(checkBoolean[i], tBoolean[i]);
      }

      Byte[] tByte = (Byte[]) instanciatePojo.createInstanceArray("java.lang.Byte", new String[] { "0", "1", "2", "4" });
      Byte[] checkByte = new Byte[] { 0, 1, 2, 4 };
      assertEquals(checkByte.length, tByte.length);
      for (int i = 0; i < checkByte.length; i++) {
        assertEquals(checkByte[i], tByte[i]);
      }

      Short[] tShort = (Short[]) instanciatePojo.createInstanceArray("java.lang.Short", new String[] { "0", "1", "2", "4" });
      Short[] checkShort = new Short[] { 0, 1, 2, 4 };
      assertEquals(checkShort.length, tShort.length);
      for (int i = 0; i < checkShort.length; i++) {
        assertEquals(checkShort[i], tShort[i]);
      }

      Integer[] tInteger = (Integer[]) instanciatePojo.createInstanceArray("java.lang.Integer", new String[] { "0", "1", "2", "4" });
      Integer[] checkInteger = new Integer[] { 0, 1, 2, 4 };
      assertEquals(checkInteger.length, tInteger.length);
      for (int i = 0; i < checkInteger.length; i++) {
        assertEquals(checkInteger[i], tInteger[i]);
      }

      int[] tInt = (int[]) instanciatePojo.createInstanceArray("int", new String[] { "0", "1", "2", "4" });
      int[] checkInt = new int[] { 0, 1, 2, 4 };
      assertEquals(checkInt.length, tInt.length);
      for (int i = 0; i < checkInt.length; i++) {
        assertEquals(checkInt[i], tInt[i]);
      }
      
      
      Long[] tLong = (Long[]) instanciatePojo.createInstanceArray("java.lang.Long", new String[] { "0", "1", "2", "4" });
      Long[] checkLong = new Long[] { 0l, 1l, 2l, 4l };
      assertEquals(checkLong.length, tLong.length);
      for (int i = 0; i < checkLong.length; i++) {
        assertEquals(checkLong[i], tLong[i]);
      }

      Float[] tFloat = (Float[]) instanciatePojo.createInstanceArray("java.lang.Float", new String[] { "0.1", "1.2", "2.3", "4.4" });
      Float[] checkFloat = new Float[] { 0.1f, 1.2f, 2.3f, 4.4f };
      assertEquals(checkFloat.length, tFloat.length);
      for (int i = 0; i < checkFloat.length; i++) {
        assertEquals(checkFloat[i], tFloat[i]);
      }

      Double[] tDouble = (Double[]) instanciatePojo.createInstanceArray("java.lang.Double", new String[] { "0.1", "1.2", "2.3", "4.4" });
      Double[] checkDouble = new Double[] { 0.1d, 1.2d, 2.3d, 4.4d };
      assertEquals(checkDouble.length, tDouble.length);
      for (int i = 0; i < checkDouble.length; i++) {
        assertEquals(checkDouble[i], tDouble[i]);
      }

      BigDecimal[] tBigDecimal = (BigDecimal[]) instanciatePojo.createInstanceArray("java.math.BigDecimal", new String[] {
                                                                                                                          "0.1",
                                                                                                                          "1.2",
                                                                                                                          "2.3",
                                                                                                                          "4.4" });
      BigDecimal[] checkBigDecimal = new BigDecimal[] {
                                                       new BigDecimal("0.1"),
                                                       new BigDecimal("1.2"),
                                                       new BigDecimal("2.3"),
                                                       new BigDecimal("4.4") };
      assertEquals(checkBigDecimal.length, tBigDecimal.length);
      for (int i = 0; i < checkBigDecimal.length; i++) {
        assertEquals(checkBigDecimal[i], tBigDecimal[i]);
      }

      try {
        instanciatePojo.createInstanceArray("java.lang.BigDecimal", new String[] { "0.1", "1.2", "2.3", "4.4" });
        fail("Instanciation impossible, il aurait du avoir une exception.");
      } catch (Throwable t) {
        assertTrue(t instanceof InvalidArgumentException);
        assertEquals("Argument inconnue propertyType=java.lang.BigDecimal values=[0.1;1.2;2.3;4.4]", t.getMessage());
      }
    } catch (InvalidArgumentException e) {
      fail(ExceptionUtils.getFullStackTrace(e));
    }
  }

  @Test
  public void testCreateInstanceSimple() {
    try {
      InstanciatePojo instanciatePojo = new InstanciatePojo();
      assertEquals("abc", instanciatePojo.createInstanceSimple("java.lang.String", "abc"));

      
      assertEquals(false, instanciatePojo.createInstanceSimple("boolean", null));
      assertEquals((byte)0, instanciatePojo.createInstanceSimple("byte", null));
      assertEquals((short)0, instanciatePojo.createInstanceSimple("short", null));
      assertEquals(0, instanciatePojo.createInstanceSimple("int", null));
      assertEquals(0l, instanciatePojo.createInstanceSimple("long", null));
      assertEquals(0f, instanciatePojo.createInstanceSimple("float", null));
      assertEquals(0d, instanciatePojo.createInstanceSimple("double", null));
      assertEquals(null, instanciatePojo.createInstanceSimple("java.math.BigDecimal", null));
      assertEquals(null, instanciatePojo.createInstanceSimple("java.math.BigDecimal", ""));

      
      boolean b = true;
      assertEquals(b, instanciatePojo.createInstanceSimple("boolean", "yes"));
      assertEquals(new Boolean(true), instanciatePojo.createInstanceSimple("java.lang.Boolean", "tRuE"));
      assertEquals(false, instanciatePojo.createInstanceSimple("boolean", "0"));
      assertEquals(Boolean.FALSE, instanciatePojo.createInstanceSimple("java.lang.Boolean", "fAlSe"));

      byte by = 0;
      assertEquals(by, instanciatePojo.createInstanceSimple("byte", "0"));
      assertEquals(new Byte((byte) 0), instanciatePojo.createInstanceSimple("java.lang.Byte", "0"));
      by = 5;
      assertEquals(by, instanciatePojo.createInstanceSimple("byte", "5"));
      assertEquals(new Byte((byte) 5), instanciatePojo.createInstanceSimple("java.lang.Byte", "5"));

      int i = 0;
      assertEquals(i, instanciatePojo.createInstanceSimple("int", "0"));
      assertEquals(new Integer(0), instanciatePojo.createInstanceSimple("java.lang.Integer", "0"));
      i = 5;
      assertEquals(i, instanciatePojo.createInstanceSimple("int", "5"));
      assertEquals(new Integer(5), instanciatePojo.createInstanceSimple("java.lang.Integer", "5"));

      double d = 0;
      assertEquals(d, instanciatePojo.createInstanceSimple("double", "0"));
      assertEquals(new Double(0), instanciatePojo.createInstanceSimple("java.lang.Double", "0"));
      d = 456.789;
      assertEquals(d, instanciatePojo.createInstanceSimple("double", "456.789"));
      assertEquals(new Double(456.789), instanciatePojo.createInstanceSimple("java.lang.Double", "456.789"));

      float f = 0;
      assertEquals(f, instanciatePojo.createInstanceSimple("float", "0"));
      assertEquals(new Float(0), instanciatePojo.createInstanceSimple("java.lang.Float", "0"));
      f = 456.789f;
      assertEquals(f, instanciatePojo.createInstanceSimple("float", "456.789"));
      assertEquals(new Float(456.789), instanciatePojo.createInstanceSimple("java.lang.Float", "456.789"));

      long l = 0;
      assertEquals(l, instanciatePojo.createInstanceSimple("long", "0"));
      assertEquals(new Long(0), instanciatePojo.createInstanceSimple("java.lang.Long", "0"));
      l = 500;
      assertEquals(l, instanciatePojo.createInstanceSimple("long", "500"));
      assertEquals(new Long(500), instanciatePojo.createInstanceSimple("java.lang.Long", "500"));

      short s = 0;
      assertEquals(s, instanciatePojo.createInstanceSimple("short", "0"));
      assertEquals(new Short((short) 0), instanciatePojo.createInstanceSimple("java.lang.Short", "0"));
      s = 500;
      assertEquals(s, instanciatePojo.createInstanceSimple("short", "500"));
      assertEquals(new Short((short) 5), instanciatePojo.createInstanceSimple("java.lang.Short", "5"));

      BigDecimal bd = new BigDecimal("500.15");
      assertEquals(bd, instanciatePojo.createInstanceSimple("java.math.BigDecimal", "500.15"));
    } catch (InvalidArgumentException e) {
      fail(ExceptionUtils.getFullStackTrace(e));
    }
  }

  @Test
  public void testCreateInstance1() {
    try {
      Vector<String> v = new Vector<String>();
      v.add("monChampString");
      v.add("monTableauString");
      v.add("maMap$AA");
      v.add("maMap$CC");
      v.add("maMap$BB");
      expect(requestMock.getParameterNames()).andReturn(v.elements());
      expect(requestMock.getParameterValues("monChampString")).andReturn((String[]) new String[] { "maChaine de ComposeName" });
      expect(requestMock.getParameterValues("monTableauString")).andReturn(
          (String[]) new String[] { "maChaine-a", "maChaine-b", "maChaine-c" });
      expect(requestMock.getParameterValues("maMap$AA")).andReturn((String[]) new String[] { "aa" });
      expect(requestMock.getParameterValues("maMap$CC")).andReturn((String[]) new String[] { "cc" });
      expect(requestMock.getParameterValues("maMap$BB")).andReturn((String[]) new String[] { "bb" });

      /*
       * expect(requestMock.getParameter("monChampString")).andReturn((String)
       * "maChaine de ComposeName");
       * expect(requestMock.getParameterValues("monTableauString")).andReturn(
       * (String[]) new String[] { "maChaine-a", "maChaine-b", "maChaine-c" });
       * expect(requestMock.getParameter("maMap$AA")).andReturn((String) "aa");
       * expect(requestMock.getParameter("maMap$CC")).andReturn((String) "cc");
       * expect(requestMock.getParameter("maMap$BB")).andReturn((String) "bb");
       */
      replay(requestMock);
      // ClassAlltype
      InstanciatePojo instanciatePojo = new InstanciatePojo();
      ClassAlltype myObject = new ClassAlltype();
      instanciatePojo.createInstance(requestMock, myObject);
      assertEquals("maChaine de ComposeName", myObject.getMonChampString());
      assertArrayEquals(new String[] { "maChaine-a", "maChaine-b", "maChaine-c" }, myObject.getMonTableauString());
      assertEquals("aa", myObject.getMaMap().get("AA"));
    } catch (InvalidArgumentException e) {
      fail(ExceptionUtils.getFullStackTrace(e));
    }
  }

  
  
  @Test
  public void testPupolateObject() {
    try {
      List<String[]> listParam = new ArrayList<String[]>();
      listParam.add(new String[] { "monChampString", "maChaine-a" });
      listParam.add(new String[] { "monTableauString", "a1" });
      listParam.add(new String[] { "monTableauString", "a2" });
      listParam.add(new String[] { "maMap$k1", "map1" });
      listParam.add(new String[] { "maMap$k2", "map2" });
      listParam.add(new String[] { "maList", "l1" });
      listParam.add(new String[] { "maList", "l2" });
      listParam.add(new String[] { "maList", "l3" });
      
      InstanciatePojo biu = new InstanciatePojo();
      ClassAlltype myObject = new ClassAlltype();
      biu.pupolateObject(myObject, listParam);
      assertEquals("maChaine-a", myObject.getMonChampString());
      assertArrayEquals(new String[] { "a1", "a2" }, myObject.getMonTableauString());
      Map<String, String> map = new Hashtable<String, String>();
      map.put("k1", "map1");
      map.put("k2", "map2");
      assertEquals(map, myObject.getMaMap());
      List<String> list = new Vector<String>();
      list.add("l1");
      list.add("l2");
      list.add("l3");
      assertEquals(list, myObject.getMaList());
      System.out.println("" + ToStringBuilder.reflectionToString(myObject));
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testPupolateObjectChild() {
    List<String[]> list = new ArrayList<String[]>();
    list.add(new String[] { "composeName", "maChaine-a" });
    list.add(new String[] { "monTableauString", "a1" });
    list.add(new String[] { "monTableauString", "a2" });
    list.add(new String[] { "composition.monchampString", "champ fils" });
    list.add(new String[] { "parentName", "parentName^^" });
    list.add(new String[] { "maList", "l1" });
    list.add(new String[] { "maList", "l2" });
    list.add(new String[] { "maList", "l3" });
    list.add(new String[] { "composition.monchampInt", "421" });

    try {
      ClassCompose myObject = new ClassCompose();
      InstanciatePojo biu = new InstanciatePojo();
      biu.pupolateObject(myObject, list);

      assertEquals("maChaine-a", myObject.getComposeName());
      assertEquals("champ fils", myObject.getComposition().getMonchampString());
      assertEquals(421, myObject.getComposition().getMonchampInt());

      System.out.println("-----------------------");
      System.out.println(ToStringBuilder.reflectionToString(myObject));
      System.out.println(ToStringBuilder.reflectionToString(myObject.getComposition()));

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
}
