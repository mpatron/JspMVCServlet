package org.jobjects.mvc.event;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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

  protected FieldValidation() {
  }

  public static ErrorMessages valideString(final String fieldName, final String value, final boolean mandatory, final int minLength,
      final int maxLength) {
    final ErrorMessages errorMessages = new ErrorMessages();
    if (StringUtils.isBlank(value) && mandatory) {
      final ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MANDATORY));
      errorMessages.getErrorMessages().add(errorMessage);
    } else {
      if (!StringUtils.isBlank(value)) {
        if (StringUtils.trim(value).length() < minLength) {
          final ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MINIMUM,
              new Object[] { minLength }));
          errorMessages.getErrorMessages().add(errorMessage);
        }
        if (StringUtils.trim(value).length() > maxLength) {
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
    if (StringUtils.isBlank(value) && mandatory) {
      ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MANDATORY));
      errorMessages.getErrorMessages().add(errorMessage);
    } else {
      if (!StringUtils.isBlank(value)) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
          sdf.parse(value);
        } catch (ParseException e) {
          ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(FORMAT, new Object[] { format }));
          errorMessages.getErrorMessages().add(errorMessage);
        }
      }
    }
    return errorMessages;
  }

  public static ErrorMessages valideSelectIn(String fieldName, String value, boolean mandatory, Collection<String> values) {
    ErrorMessages errorMessages = new ErrorMessages();
    if (StringUtils.isBlank(value) && mandatory) {
      ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MANDATORY));
      errorMessages.getErrorMessages().add(errorMessage);
    } else {
      if (!StringUtils.isBlank(value)) {
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
    if (StringUtils.isBlank(value) && mandatory) {
      ErrorMessage errorMessage = new ErrorMessage(fieldName, PropertyMessage.getInstance().getMessage(MANDATORY));
      errorMessages.getErrorMessages().add(errorMessage);
    } else {
      if (!StringUtils.isBlank(value)) {
        if (NumberUtils.isNumber(value)) {
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
