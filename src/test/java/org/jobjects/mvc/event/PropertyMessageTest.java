/**
 * 
 */
package org.jobjects.mvc.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * @author patronmi
 * 
 */
public class PropertyMessageTest {
  protected static Logger logger = Logger.getLogger("org.jobjects.mvc.event.PropertyMessageTest");

  /**
   * Test method for
   * {@link org.jobjects.mvc.event.PropertyMessage#getInstance()}.
   */
  @Test
  public void testGetInstance() {
    try {
      assertTrue("Instance singleton ne peut être nul.", PropertyMessage.getInstance() != null);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  /**
   * Test method for
   * {@link org.jobjects.mvc.event.PropertyMessage#getMessage(java.lang.String, java.lang.Object[])}
   * .
   */
  @Test
  public void testGetMessageStringObjectArray() {
    try {
      assertEquals("Recupération du fichier properties.", "La valeur n'est pas au format toto.",
          PropertyMessage.getInstance().getMessage("FieldValidation.FORMAT", new String[] { "toto" }));
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  /**
   * Test method for
   * {@link org.jobjects.mvc.event.PropertyMessage#getMessage(java.lang.String)}
   * .
   */
  @Test
  public void testGetMessageString() {
    try {
      assertEquals("Recupération du fichier properties.", "La valeur est interdite.",
          PropertyMessage.getInstance().getMessage("FieldValidation.FORBIDEN"));
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

}
