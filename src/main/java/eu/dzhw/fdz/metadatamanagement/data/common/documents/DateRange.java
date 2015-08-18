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

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
    result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    DateRange other = (DateRange) obj;
    if (this.endDate == null) {
      if (other.endDate != null) {
        return false;
      }
    } else if (!this.endDate.equals(other.endDate)) {
      return false;
    }
    if (this.startDate == null) {
      if (other.startDate != null) {
        return false;
      }
    } else if (!this.startDate.equals(other.startDate)) {
      return false;
    }
    return true;
  }
}
