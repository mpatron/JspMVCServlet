package org.jobjects.mvc.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ErrorMessageTest {

  @Test
  public void testErrorMessage() {
    ErrorMessage em = new ErrorMessage("aa", "bb");
    assertEquals("attendu aa", "aa", em.getField());
  }

  @Test
  public void testGetField() {
    ErrorMessage em = new ErrorMessage("aa", "bb");
    assertEquals("attendu aa", "aa", em.getField());
  }

  @Test
  public void testSetField() {
    ErrorMessage em = new ErrorMessage("aa", "bb");
    em.setField("cc");
    assertEquals("attendu cc", "cc", em.getField());
  }

  @Test
  public void testGetMessage() {
    ErrorMessage em = new ErrorMessage("aa", "bb");
    assertEquals("attendu bb", "bb", em.getMessage());
  }

  @Test
  public void testSetMessage() {
    ErrorMessage em = new ErrorMessage("aa", "bb");
    em.setMessage("cc");
    assertEquals("attendu cc", "cc", em.getMessage());
  }

}
