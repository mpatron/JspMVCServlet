package org.jobjects.mvc.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ErrorMessagesTest {

  private ErrorMessages errorMessages= new ErrorMessages();
  
  @Before
  public void setUp() throws Exception {
    errorMessages.getErrorMessages().add(new ErrorMessage("field1", "message11"));
    errorMessages.getErrorMessages().add(new ErrorMessage("field1", "message12"));
    errorMessages.getErrorMessages().add(new ErrorMessage("field1", "message13"));
    errorMessages.getErrorMessages().add(new ErrorMessage("field2", "message2"));
  }

  @Test
  public void testErrorMessages() {
    assertNotNull(errorMessages);
  }

  @Test
  public void testGetErrorMessages() {
    assertEquals(4, errorMessages.getErrorMessages().size());
  }

  @Test
  public void testGetMessagesByField() {
    assertEquals(3, errorMessages.getMessagesByField("field1").size());
    assertEquals("message11", errorMessages.getMessagesByField("field1").iterator().next());
  }

}
