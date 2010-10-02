package org.jobjects.mvc.event;

/**
 * 
 * Erreur contenant le nom du champ et le message de l'erreur.
 * 
 * @author Mickael Patron
 * @version $Date: 2010/04/13 08:14:35 $
 * @version 1.95
 * 
 */
public class ErrorMessage {
  private String field = null;
  private String message = null;

  public ErrorMessage(String field, String message) {
    this.field = field;
    this.message = message;
  }

  public final String getField() {
    return field;
  }

  public final void setField(String field) {
    this.field = field;
  }

  public final String getMessage() {
    return message;
  }

  public final void setMessage(String message) {
    this.message = message;
  }

}
