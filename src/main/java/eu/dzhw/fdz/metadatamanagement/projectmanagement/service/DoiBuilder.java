package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;

/**
 * Generate DOIs which will be sent to DARA on release of a {@link DataAcquisitionProject}.
 * 
 * @author René Reitmann
 */
@Component
public class DoiBuilder {
  @Autowired
  private Environment environment;

  /**
   * Create a doi for releases >= 1.0.0.
   * 
   * @param study the study which gets the doi
   * @param release the release
   * @return a doi (if required)
   */
  public String buildStudyDoi(StudySubDocumentProjection study, Release release) {
    if (release != null && study != null
        && Version.valueOf(release.getVersion()).lessThanOrEqualTo(Version.valueOf("1.0.0"))) {
      if (environment.acceptsProfiles(Constants.SPRING_PROFILE_PROD)) {
        return "10.21249/DZHW:" + study.getDataAcquisitionProjectId() + ":" 
            + release.getVersion();
      } else {
        return "10.5072/DZHW:" + study.getDataAcquisitionProjectId() + ":" 
            + release.getVersion();
      }
    }
    return null;
  }
}
