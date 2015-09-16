package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.ValidDateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.SearchValidationGroup.Search;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The date range has two {@code LocalDate} object for representing a time range. The the
 * {@code DateOrderValidator} is used to check the order of the range.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.data.common.documents.builders")
@ValidDateRange(groups = {Create.class, Edit.class, Search.class})
public class DateRange {

  // Public constants which are used in queries as fieldnames.
  public static final String STARTDATE_FIELD = "startDate";
  public static final String ENDDATE_FIELD = "endDate";

  /**
   * The start date of the range.
   */
  @NotNull(groups = {Create.class, Edit.class})
  private LocalDate startDate;

  /**
   * The end date of the range.
   */
  @NotNull(groups = {Create.class, Edit.class})
  private LocalDate endDate;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("startDate", startDate).add("endDate", endDate)
        .toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(startDate, endDate);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      DateRange that = (DateRange) object;
      return Objects.equal(this.startDate, that.startDate)
          && Objects.equal(this.endDate, that.endDate);
    }
    return false;
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
