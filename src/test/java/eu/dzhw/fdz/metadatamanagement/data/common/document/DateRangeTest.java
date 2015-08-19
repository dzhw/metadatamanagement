/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.document;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class DateRangeTest {

  @Test
  public void testHashCode() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().build();

    // Act

    // Assert
    assertEquals(961, dateRange.hashCode());
  }

  @Test
  public void testEquals() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().build();
    DateRange dateRange2 = new DateRangeBuilder().build();

    LocalDate startDate = LocalDate.now();
    LocalDate endDate = LocalDate.now().plusDays(2);

    // Act
    boolean checkSame = dateRange.equals(dateRange);
    boolean checkNull = dateRange.equals(null);
    boolean checkOtherClass = dateRange.equals(new Object());
    dateRange2.setEndDate(endDate);
    boolean checkEndDateOther = dateRange.equals(dateRange2);
    dateRange.setEndDate(startDate);
    boolean checkEndDateBoth = dateRange.equals(dateRange2);
    dateRange.setEndDate(endDate);
    boolean checkEndDateBothSame = dateRange.equals(dateRange2);
    dateRange.setEndDate(null);
    dateRange2.setEndDate(null);
    dateRange2.setStartDate(startDate);
    boolean checkStartDateOther = dateRange.equals(dateRange2);
    dateRange.setStartDate(endDate);
    boolean checkStartDateBoth = dateRange.equals(dateRange2);
    dateRange.setStartDate(startDate);
    boolean checkStartDateBothSame = dateRange.equals(dateRange2);

    // Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkEndDateOther);
    assertEquals(false, checkEndDateBoth);
    assertEquals(true, checkEndDateBothSame);
    assertEquals(false, checkStartDateOther);
    assertEquals(false, checkStartDateBoth);
    assertEquals(true, checkStartDateBothSame);
  }

}
