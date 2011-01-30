/**
 * 
 */
package org.jobjects.mvc.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * @author patronmi
 * 
 */
public class FieldValidationTest {
  protected static final Logger logger = Logger.getLogger("org.jobjects.mvc.event.FieldValidationTest");

  // @Test
  // public void testFieldValidation() {
  // FieldValidation fv = new FieldValidation();
  // fv.hashCode();
  // }

  /**
   * Test method for
   * {@link org.jobjects.mvc.event.FieldValidation#valideString(java.lang.String, java.lang.String, boolean, int, int)}
   * .
   */
  @Test
  public void testValideString() {
    try {
      ErrorMessages errorMessages = FieldValidation.valideString("fieldName", "déjàlà€______________", false, 4, 10);
      assertNotNull("Liste des messages n'est jamais nul.", errorMessages.getErrorMessages());
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());
      for (ErrorMessage errorMessage : errorMessages.getErrorMessages()) {
        assertEquals("conservation du nom du paramètre.", "fieldName", errorMessage.getField());
      }

      errorMessages = FieldValidation.valideString("fieldName", null, false, 4, 10);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideString("fieldName", "a", false, 4, 10);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideString("fieldName", "", false, 4, 10);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideString("fieldName", null, true, 4, 10);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideString("fieldName", "", true, 4, 10);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  /**
   * Test method for
   * {@link org.jobjects.mvc.event.FieldValidation#valideDate(java.lang.String, java.lang.String, boolean, java.lang.String)}
   * .
   */
  @Test
  public void testValideDate() {
    try {
      ErrorMessages errorMessages = FieldValidation.valideDate("fieldName", "2010/12/25", false, "yyyy/MM/dd");
      assertNotNull("Liste des messages n'est jamais nul.", errorMessages.getErrorMessages());
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());
      for (ErrorMessage errorMessage : errorMessages.getErrorMessages()) {
        assertEquals("conservation du nom du paramètre.", "fieldName", errorMessage.getField());
      }

      errorMessages = FieldValidation.valideDate("fieldName", null, false, "yyyy/MM/dd");
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideDate("fieldName", "a", false, "yyyy/MM/dd");
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideDate("fieldName", "", false, "yyyy/MM/dd");
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideDate("fieldName", null, true, "yyyy/MM/dd");
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideDate("fieldName", "", true, "yyyy/MM/dd");
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideDate("fieldName", "2010/12/35", false, "yyyy/MM/dd");
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideDate("fieldName", "2f10/12/25", false, "yyyy/MM/dd");
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideDate("fieldName", "25/12/2sfkldnflsdn#10", false, "dd/MM/yyyy");
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  /**
   * Test method for
   * {@link org.jobjects.mvc.event.FieldValidation#valideSelectIn(java.lang.String, java.lang.String, boolean, java.util.Collection)}
   * .
   */
  @Test
  public void testValideSelectInStringStringBooleanCollectionOfString() {
    try {
      Collection<String> values = new ArrayList<String>();
      values.add("AA");
      values.add("BBB");
      values.add("CC");

      ErrorMessages errorMessages = FieldValidation.valideSelectIn("fieldName", "AA", false, values);
      assertNotNull("Liste des messages n'est jamais nul.", errorMessages.getErrorMessages());
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());
      for (ErrorMessage errorMessage : errorMessages.getErrorMessages()) {
        assertEquals("conservation du nom du paramètre.", "fieldName", errorMessage.getField());
      }

      String chaineNull = null;
      errorMessages = FieldValidation.valideSelectIn("fieldName", chaineNull, false, values);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", "a", false, values);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", "", false, values);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", chaineNull, true, values);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", "", true, values);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      /*
       * Test sur liste vide.
       */
      values = new ArrayList<String>();
      errorMessages = FieldValidation.valideSelectIn("fieldName", "a", false, values);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", chaineNull, false, values);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      /*
       * Test sur liste nul.
       */
      values = null;
      errorMessages = FieldValidation.valideSelectIn("fieldName", "a", false, values);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", chaineNull, false, values);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  /**
   * Test method for
   * {@link org.jobjects.mvc.event.FieldValidation#valideSelectIn(java.lang.String, java.lang.String[], boolean, java.util.Collection)}
   * .
   */
  @Test
  public void testValideSelectInStringStringArrayBooleanCollectionOfString() {
    try {
      Collection<String> references = new ArrayList<String>();
      references.add("AA");
      references.add("BBB");
      references.add("CC");

      String[] stringArrayNull = null;

      ErrorMessages errorMessages = FieldValidation.valideSelectIn("fieldName", new String[] { "AA", "CC" }, false, references);
      assertNotNull("Liste des messages n'est jamais nul.", errorMessages.getErrorMessages());
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());
      for (ErrorMessage errorMessage : errorMessages.getErrorMessages()) {
        assertEquals("conservation du nom du paramètre.", "fieldName", errorMessage.getField());
      }

      errorMessages = FieldValidation.valideSelectIn("fieldName", stringArrayNull, false, references);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", new String[] { "a" }, false, references);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", new String[1], false, references);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", stringArrayNull, true, references);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", new String[5], true, references);
      assertEquals("Un seul message d'erreur.", 5, errorMessages.getErrorMessages().size());

      /*
       * Test sur liste vide.
       */
      references = new ArrayList<String>();
      errorMessages = FieldValidation.valideSelectIn("fieldName", new String[] { "a" }, false, references);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", stringArrayNull, false, references);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      /*
       * Test sur liste nul.
       */
      references = null;
      errorMessages = FieldValidation.valideSelectIn("fieldName", new String[] { "a" }, false, references);
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", stringArrayNull, false, references);
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  /**
   * Test method for
   * {@link org.jobjects.mvc.event.FieldValidation#valideSelectIn(java.lang.String, java.lang.String, boolean, java.lang.Class)}
   * .
   */
  @Test
  public void testValideSelectInStringStringBooleanClass() {
    try {
      ErrorMessages errorMessages = FieldValidation.valideSelectIn("fieldName", "AA", false, MyEnums.class);
      assertNotNull("Liste des messages n'est jamais nul.", errorMessages.getErrorMessages());
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", "AA", true, MyEnums.class);
      assertNotNull("Liste des messages n'est jamais nul.", errorMessages.getErrorMessages());
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", "BBB", true, MyEnums.class);
      assertNotNull("Liste des messages n'est jamais nul.", errorMessages.getErrorMessages());
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideSelectIn("fieldName", "DD", true, MyEnums.class);
      assertEquals("UN message d'erreur.", 1, errorMessages.getErrorMessages().size());

      for (ErrorMessage errorMessage : errorMessages.getErrorMessages()) {
        assertEquals("conservation du nom du paramètre.", "fieldName", errorMessage.getField());
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

  /**
   * Test method for
   * {@link org.jobjects.mvc.event.FieldValidation#valideBigDecimal(java.lang.String, java.lang.String, boolean, java.math.BigDecimal, java.math.BigDecimal)}
   * .
   */
  @Test
  public void testValideBigDecimal() {
    try {
      ErrorMessages errorMessages = FieldValidation.valideBigDecimal("fieldName", "5.42", true, new BigDecimal(0), new BigDecimal(4));
      assertNotNull("Liste des messages n'est jamais nul.", errorMessages.getErrorMessages());
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());
      for (ErrorMessage errorMessage : errorMessages.getErrorMessages()) {
        assertEquals("conservation du nom du paramètre.", "fieldName", errorMessage.getField());
      }

      errorMessages = FieldValidation.valideBigDecimal("fieldName", null, false, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", "a", false, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", "", false, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", null, true, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", "", true, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", "", true, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Un seul message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", "3.62", false, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Pas de message d'erreur.", 0, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", "4.62", false, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Un de message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", "-0.62", false, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Un de message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", "62l", true, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Un de message d'erreur.", 1, errorMessages.getErrorMessages().size());

      errorMessages = FieldValidation.valideBigDecimal("fieldName", "0x55", true, new BigDecimal(0), new BigDecimal(4));
      assertEquals("Un de message d'erreur.", 1, errorMessages.getErrorMessages().size());

    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erreur interne non prévue.", e);
      fail();
    }
  }

}
