package eu.dzhw.fdz.metadatamanagement.domain;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The release includes all additional information for a release of a data acquisition project. It
 * is not a regular domain object with a own id, because it is additional for the Data Aquisition
 * Project.
 * 
 * @author Daniel Katzberg
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class Release {
  
  @NotEmpty(message = "{error.release.version.notEmpty}")
  @Size(max = StringLengths.SMALL, message = "{error.release.version.size}")
  @Pattern(regexp = Patterns.NUMERIC_WITH_DOT, message = "{error.release.version.pattern}")
  private String version;

  @Size(max = StringLengths.MEDIUM, message = "{error.release.doi.size}")
  private String doi;

  @NotNull(message = "{error.release.date.notNull}")
  private ZonedDateTime date;

  @I18nStringSize(max = StringLengths.LARGE, message = "{error.release.notes.i18nStringSize}")
  private I18nString notes;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("version", version)
      .add("doi", doi)
      .add("date", date)
      .add("notes", notes)
      .toString();
  }

  /* GETTER / SETTER */
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public ZonedDateTime getDate() {
    return date;
  }

  public void setDate(ZonedDateTime date) {
    this.date = date;
  }

  public I18nString getNotes() {
    return notes;
  }

  public void setNotes(I18nString notes) {
    this.notes = notes;
  }
}
