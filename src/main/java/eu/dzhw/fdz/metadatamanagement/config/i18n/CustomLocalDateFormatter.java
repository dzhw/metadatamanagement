package eu.dzhw.fdz.metadatamanagement.config.i18n;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Since jquery Datepicker needs both a ISO formatted date and a localized date we need this
 * formatter which converts a {@link LocalDate} into a String depending on the given {@link Locale}.
 * 
 * @author René Reitmann
 */
public class CustomLocalDateFormatter {

  private DateTimeFormatter germanFormatter;
  private DateTimeFormatter englishFormatter;

  public CustomLocalDateFormatter() {
    this.germanFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN);
    this.englishFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
  }

  /**
   * Convert the {@link LocalDate} into a String depending on the given {@link Locale}.
   * 
   * @param date The date to convert
   * @param locale Locale.GERMAN or Locale.ENGLISH
   * @return The formatted String
   */
  public String format(LocalDate date, Locale locale) {
    if (date == null) {
      return "";
    }
    if (locale.equals(Locale.ENGLISH)) {
      return englishFormatter.format(date);
    } else if (locale.equals(Locale.GERMAN)) {
      return germanFormatter.format(date);
    }
    throw new IllegalStateException("No supported locale for formatting date!");
  }

}
