package eu.dzhw.fdz.metadatamanagement.data.common.documents;

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

  // Public constants which are used in queries as fieldnames.
  public static final String STARTDATE_FIELD = "startDate";
  public static final String ENDDATE_FIELD = "endDate";

  /**
   * The start date of the range. The checked pattern is: {@code yyyy-MM-dd}
   */
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  /**
   * The end date of the range. The checked pattern is: {@code yyyy-MM-dd}
   */
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "DateRange [startDate=" + startDate + ", endDate=" + endDate + "]";
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
