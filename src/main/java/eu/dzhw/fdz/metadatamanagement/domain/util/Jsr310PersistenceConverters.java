package eu.dzhw.fdz.metadatamanagement.domain.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.DateToLocalDateTimeConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.DateToZonedDateTimeConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.LocalDateTimeToDateConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.ZonedDateTimeToDateConverter;

/**
 * A collection of static local classes for converting time and dates of the JSR 310 for the
 * persistence.
 * 
 */
public final class Jsr310PersistenceConverters {

  private Jsr310PersistenceConverters() {}

  /**
   * Local class for the local date conversion.
   */
  @Converter(autoApply = true)
  public static class LocalDateConverter implements AttributeConverter<LocalDate, java.sql.Date> {

    @Override
    public java.sql.Date convertToDatabaseColumn(LocalDate date) {
      return date == null ? null : java.sql.Date.valueOf(date);
    }

    @Override
    public LocalDate convertToEntityAttribute(java.sql.Date date) {
      return date == null ? null : date.toLocalDate();
    }
  }

  /**
   * Local class for the zone date time conversion.
   */
  @Converter(autoApply = true)
  public static class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
      return ZonedDateTimeToDateConverter.INSTANCE.convert(zonedDateTime);
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Date date) {
      return DateToZonedDateTimeConverter.INSTANCE.convert(date);
    }
  }

  /**
   * Local class for the local date time conversion.
   */
  @Converter(autoApply = true)
  public static class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
      return LocalDateTimeToDateConverter.INSTANCE.convert(localDateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date date) {
      return DateToLocalDateTimeConverter.INSTANCE.convert(date);
    }
  }
}
