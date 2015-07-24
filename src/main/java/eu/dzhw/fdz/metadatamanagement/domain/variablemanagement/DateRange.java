package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * The date range has two {@code LocalDate} object for representing a time range. The the
 * {@code DateOrderValidator} is used to check the order of the range.
 * 
 * @author Daniel Katzberg
 *
 */
public class DateRange {

  /**
   * The start date of the survey. The checked pattern is: {@code yyyy-MM-dd}
   */
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  /**
   * The end date of the survey. The checked pattern is: {@code yyyy-MM-dd}
   */
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  /**
   * Output is a summarize of the start and end date. Example:
   * {@code DateRange [StartDate=2015-07-22, EndDate=2015-07-24]}
   * 
   * @return A String which will summarizes the object date range.
   */
  @Override
  public String toString() {
    return "DateRange [StartDate=" + this.startDate.toString() + ", EndDate="
        + this.endDate.toString() + "]";
  }

  /* GETTER / SETTER */
  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }
}
