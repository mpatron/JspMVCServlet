package org.jobjects.mvc.event;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Liste des erreurs. Cette liste est produit par la validation.
 * 
 * @author Mickael Patron
 * @version $Date: 2010/04/13 08:14:35 $
 * @version 1.95
 * 
 */
public class ErrorMessages {
  private Set<ErrorMessage> errorMessages = new HashSet<ErrorMessage>();

  public ErrorMessages() {
  }

  public final Set<ErrorMessage> getErrorMessages() {
    return errorMessages;
  }

  public final Set<String> getMessagesByField(String fieldName) {
    Set<String> returnValue = new HashSet<String>();
    for (ErrorMessage errorMessage : errorMessages) {
      if (StringUtils.equals(fieldName, errorMessage.getField())) {
        returnValue.add(errorMessage.getMessage());
      }
    }
    return returnValue;
  }
}
