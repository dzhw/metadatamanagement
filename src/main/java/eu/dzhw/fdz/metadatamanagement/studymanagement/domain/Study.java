package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.List;

import javax.validation.Valid;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

/**
 * The study domain object represents a study. A study can has more than one release. 
 * Every {@link DataAcquisitionProject} has exact one Study.
 * 
 * @author Daniel Katzberg
 *
 */
public class Study {
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "data-acquisition-project."
          + "error.data-acquisition-project.survey-series.i18n-string-size")
  private I18nString surveySeries;

  /* Nested Objects */
  @Valid
  private List<Release> releases;
  
  
  /* GETTER / SETTER */
  public List<Release> getReleases() {
    return releases;
  }

  public void setReleases(List<Release> releases) {
    this.releases = releases;
  }
  
  public I18nString getSurveySeries() {
    return surveySeries;
  }

  public void setSurveySeries(I18nString surveySeries) {
    this.surveySeries = surveySeries;
  }
  
}
