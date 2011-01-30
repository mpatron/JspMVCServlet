package org.jobjects.mvc.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PrimitiveTypeEnumTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testGetName() {
    assertEquals("boolean", PrimitiveTypeEnum.BOOLEAN.getTypeName());
    assertEquals("long", PrimitiveTypeEnum.LONG.getTypeName());
    assertEquals("int", PrimitiveTypeEnum.INT.getTypeName());
  }

  @Test
  public void testGetType() {
    assertEquals(Boolean.TYPE, PrimitiveTypeEnum.BOOLEAN.getType());
    assertEquals(Long.TYPE, PrimitiveTypeEnum.LONG.getType());
    assertEquals(Integer.TYPE, PrimitiveTypeEnum.INT.getType());
  }

  @Test
  public void testIsPrimitif() {
    assertTrue("int est un type primitif", PrimitiveTypeEnum.isPrimitif("int"));
    assertFalse("Integer n'est pas un type primitif", PrimitiveTypeEnum.isPrimitif("Integer"));
    assertFalse("Toto n'est pas un type primitif", PrimitiveTypeEnum.isPrimitif("toto"));
  }

}
