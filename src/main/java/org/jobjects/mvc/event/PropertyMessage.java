package org.jobjects.mvc.event;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Classe permettant chargenat les erreurs standards.
 * 
 * @author Mickael Patron
 * @version $Date: 2010/04/13 08:14:35 $
 * @version 1.95
 * 
 */
public final class PropertyMessage {
  private static PropertyMessage instance = null;
  private final transient ResourceBundle prb = ResourceBundle.getBundle(getClass().getPackage()
      .getName()
      + ".FieldValidationMessages");

  private PropertyMessage() {
//    propertyResourceBundle = ResourceBundle.getBundle(getClass().getPackage()
//        .getName()
//        + ".FieldValidationMessages");
  }

  public static PropertyMessage getInstance() {
    synchronized (PropertyMessage.class) {
      if (instance == null) {
        instance = new PropertyMessage();
      }
    }
    return instance;
  }

  public String getMessage(final String key, final Object[] arguments) {
    final String chaine = prb.getString(key);
    final MessageFormat messageFormat = new MessageFormat(chaine);
    return messageFormat.format(arguments);
  }

  public String getMessage(final String key) {
    return prb.getString(key);
  }
}
