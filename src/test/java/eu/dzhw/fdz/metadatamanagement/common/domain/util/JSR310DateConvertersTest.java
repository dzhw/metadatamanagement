/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.domain.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.Date;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.domain.util.Jsr310DateConverters;

/**
 * @author Daniel Katzberg
 *
 */
public class JSR310DateConvertersTest {

  @Test
  public void testConvertToDate() {
    // Arrange
    Jsr310DateConverters.LocalDateToDateConverter localDateToDateConverter =
        Jsr310DateConverters.LocalDateToDateConverter.INSTANCE;
    Jsr310DateConverters.DateToLocalDateConverter dateToLocalDateConverter =
        Jsr310DateConverters.DateToLocalDateConverter.INSTANCE;

    // Act
    LocalDate localDate = LocalDate.now();
    Date date = localDateToDateConverter.convert(localDate);
    LocalDate localDateCheck = dateToLocalDateConverter.convert(date);

    // Assert
    assertThat(date, not(nullValue()));
    assertThat(localDate.toString(), is(localDateCheck.toString()));
  }
}
