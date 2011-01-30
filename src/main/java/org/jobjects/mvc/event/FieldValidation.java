package org.jobjects.mvc.event;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberRange;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * Class utilitaire pour la validation.
 * <ul>
 * <li>valideBigDecimal : validation d'un nombre réel avec ensemble de
 * définition.</li>
 * <li>valideDate : validation d'une date.</li>
 * <li>valideSelectIn : validation du valeur dans une liste de valeur contenue
 * dans un enum.</li>
 * <li>valideSelectIn : validation du valeur dans une liste de valeur contenue
 * dans une liste de string.</li>
 * <li>valideString : validation d'une chaine de caractère.</li>
 * </ul>
 * 
 * @author Mickael Patron
 * @version $Date: 2010/04/13 08:14:35 $
 * @version 1.95
 * 
 */
public final class FieldValidation {

  private static final String MANDATORY = "FieldValidation.MANDATORY";
  private static final String MINIMUM = "FieldValidation.MINIMUM";
  private static final String MAXIMUM = "FieldValidation.MAXIMUM";
  private static final String FORMAT = "FieldValidation.FORMAT";
  private static final String INLIST = "FieldValidation.INLIST";
  private static final String FLOAT_RANGE = "FieldValidation.FLOAT_RANGE";
  private static final String FORMAT_FLOAT = "FieldValidation.FORMAT_FLOAT";

  /*
   * Il a été décidé en septembre 2009 de rester en commons-lang 2.1. Cette
   * méthode une recopie du code source de commons-lang 2.4.
   */
  private static int length(String str) {
    return str == null ? 0 : str.length();
  }

  protected FieldValidation() {
  }

  public static ErrorMessages valideString(final String fieldName,
                                           final String value,
                                           final boolean mandatory,
                                           final int minLength,
                                           final int maxLength) {
    final ErrorMessages errorMessages = new ErrorMessages();
    if (StringUtils.isEmpty(value) && mandatory) {
      final ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MANDATORY));
      errorMessages.getErrorMessages().add(errorMessage);
    } else {
      if (!StringUtils.isEmpty(value)) {
        if (length(value) < minLength) {
          // if (StringUtils.trim(value).length() < minLength) {
          // commons-lang-2.5
          final ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MINIMUM,
              new Object[] { minLength }));
          errorMessages.getErrorMessages().add(errorMessage);
        }
        if (length(value) > maxLength) {
          // if (StringUtils.trim(value).length() > maxLength) {
          // commons-lang-2.5
          final ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MAXIMUM,
              new Object[] { maxLength }));
          errorMessages.getErrorMessages().add(errorMessage);
        }
      }
    }
    return errorMessages;
  }

  public static ErrorMessages valideDate(String fieldName, String value, boolean mandatory, String format) {
    ErrorMessages errorMessages = new ErrorMessages();
    if (StringUtils.isEmpty(value) && mandatory) {
      ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MANDATORY));
      errorMessages.getErrorMessages().add(errorMessage);
    } else {
      if (!StringUtils.isEmpty(value)) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
          parseFullString(sdf, value);
        } catch (ParseException e) {
          ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(FORMAT, new Object[] { format }));
          errorMessages.getErrorMessages().add(errorMessage);
        }
      }
    }
    return errorMessages;
  }

  private static Date parseFullString(final DateFormat df, final String input) throws ParseException {
    ParsePosition pos = new ParsePosition(0); // On commence le parsing au
    // début
    // de la chaine
    df.setLenient(false);
    Date date = df.parse(input, pos); // pas de ParseException
    if (date == null) {
      // Erreur lors du parsing ==> Exception
      throw new ParseException(input, pos.getErrorIndex());
    }
    if (pos.getIndex() < input.length()) {
      // La chaine n'a pas été lu dans sa totalité ==> Exception
      throw new ParseException(input, pos.getIndex() + 1);
    }
    return date;
  }

  public static ErrorMessages valideSelectIn(String fieldName, String value, boolean mandatory, Collection<String> values) {
    ErrorMessages errorMessages = new ErrorMessages();
    if (StringUtils.isEmpty(value) && mandatory) {
      ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MANDATORY));
      errorMessages.getErrorMessages().add(errorMessage);
    } else {
      if (!StringUtils.isEmpty(value)) {
        if ((values != null) && (values.size() != 0)) {
          if (!values.contains(value)) {
            ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance()
                .getMessage(INLIST, new Object[] { value }));
            errorMessages.getErrorMessages().add(errorMessage);
          }
        } else {
          ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(INLIST, new Object[] { value }));
          errorMessages.getErrorMessages().add(errorMessage);
        }
      }
    }
    return errorMessages;
  }

  public static ErrorMessages valideSelectIn(String fieldName, String[] values, boolean mandatory, Collection<String> references) {
    ErrorMessages errorMessages = new ErrorMessages();
    if (((values == null) || (values.length == 0)) && mandatory) {
      ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MANDATORY));
      errorMessages.getErrorMessages().add(errorMessage);
    } else {
      if ((values != null) && (values.length != 0)) {
        for (int i = 0; i < values.length; i++) {
          errorMessages.getErrorMessages().addAll(valideSelectIn(fieldName, values[i], mandatory, references).getErrorMessages());
        }
      }
    }
    return errorMessages;
  }

  public static <T extends Enum<T>> ErrorMessages valideSelectIn(String fieldName, String value, boolean mandatory, Class<T> values) {
    ArrayList<String> stringList = new ArrayList<String>();
    EnumSet<T> ee = EnumSet.allOf(values);
    for (Enum<T> enum1 : ee) {
      stringList.add(enum1.name());
    }
    return valideSelectIn(fieldName, value, mandatory, stringList);
  }

  public static ErrorMessages valideBigDecimal(String fieldName, String value, boolean mandatory, BigDecimal min, BigDecimal max) {
    ErrorMessages errorMessages = new ErrorMessages();
    if (StringUtils.isEmpty(value) && mandatory) {
      ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MANDATORY));
      errorMessages.getErrorMessages().add(errorMessage);
    } else {
      if (!StringUtils.isEmpty(value)) {
        boolean valided = false;
        try {
          new BigDecimal(value);
          valided = true;
        } catch (Exception e) {
          // Ne rien faire.
        }

        if (valided) {
          NumberRange numberRange = new NumberRange(min, max);
          if (!numberRange.containsNumber(NumberUtils.createBigDecimal(value))) {
            ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(FLOAT_RANGE,
                new Object[] { value, min, max }));
            errorMessages.getErrorMessages().add(errorMessage);
          }
        } else {
          ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(FORMAT_FLOAT,
              new Object[] { value }));
          errorMessages.getErrorMessages().add(errorMessage);
        }
      }
    }
    return errorMessages;
  }

}
