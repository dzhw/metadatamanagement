/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class PeriodTest {

  @Test
  public void testHashCode() {

    // Arrange
    Period period = new PeriodBuilder().build();

    // Act
    int hashCodeWithoutStartEnd = period.hashCode();
    period.setStart(LocalDate.now()
      .minusDays(2));
    int hashCodeWithoutEnd = period.hashCode();
    period.setEnd(LocalDate.now());
    int hashCode = period.hashCode();

    // Assert
    assertThat(hashCodeWithoutStartEnd, not(0));
    assertThat(hashCodeWithoutEnd, not(0));
    assertThat(hashCode, not(0));
    assertThat(hashCode, not(hashCodeWithoutEnd));
    assertThat(hashCode, not(hashCodeWithoutStartEnd));
    assertThat(hashCodeWithoutEnd, not(hashCodeWithoutStartEnd));

  }

  @Test
  public void testEquals() {

    // Arrange
    LocalDate startTime = LocalDate.now()
      .minusDays(2);
    LocalDate endTime = LocalDate.now()
      .minusDays(1);
    LocalDate startTime2 = LocalDate.now()
      .minusDays(1);
    LocalDate endTime2 = LocalDate.now();
    Period period1 = new PeriodBuilder().build();
    Period period2 = new PeriodBuilder().build();

    // Act
    boolean checkNull = period1.equals(null);
    boolean checkClass = period1.equals(new Object());
    boolean checkSame = period1.equals(period1);
    boolean checkSameButDifferentNoStartNoEnd = period1.equals(period2);
    period2.setEnd(endTime);
    boolean checkSameButDifferentNoStartNoEndPart = period1.equals(period2);
    period1.setEnd(endTime);
    boolean checkSameButDifferentNoStart = period1.equals(period2);
    period2.setStart(startTime);
    boolean checkSameButDifferentNoStartPart = period1.equals(period2);
    period1.setStart(startTime);
    boolean checkSameButDifferent = period1.equals(period2);
    period2.setStart(startTime2);
    boolean checkSameButDifferentStart = period1.equals(period2);
    period2.setStart(startTime);
    period2.setEnd(endTime2);
    boolean checkSameButDifferentEnd = period1.equals(period2);

    // Assert
    assertThat(checkNull, is(false));
    assertThat(checkClass, is(false));
    assertThat(checkSame, is(true));
    assertThat(checkSameButDifferentNoStartNoEnd, is(true));
    assertThat(checkSameButDifferentNoStartNoEndPart, is(false));
    assertThat(checkSameButDifferentNoStart, is(true));
    assertThat(checkSameButDifferentNoStartPart, is(false));
    assertThat(checkSameButDifferent, is(true));
    assertThat(checkSameButDifferentStart, is(false));
    assertThat(checkSameButDifferentEnd, is(false));
  }

}
