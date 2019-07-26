package eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Pattern;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import lombok.RequiredArgsConstructor;

/**
 * Generate DOIs which will be sent to DARA on release of a {@link DataAcquisitionProject}.
 *
 * @author René Reitmann
 */
@Component
@RequiredArgsConstructor
public class DoiBuilder {
  
  private final Environment environment;

  private static final Pattern VERSION_SUFFIX = Pattern.compile("-[0-9]+\\.[0-9]+\\.[0-9]+$");

  /**
   * Create a doi for releases greater than or equal to 1.0.0.
   *
   * @param study the study which gets the doi
   * @param release the release
   * @return a doi (if required)
   */
  public String buildStudyDoi(StudySubDocumentProjection study, Release release) {
    if (release != null && study != null
        && Version.valueOf(release.getVersion()).greaterThanOrEqualTo(Version.valueOf("1.0.0"))) {
      if (environment.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_PROD))) {
        return "10.21249/DZHW:" + stripVersionSuffix(study.getDataAcquisitionProjectId()) + ":"
            + release.getVersion();
      } else {
        if (LocalDate.now().isAfter(LocalDate.of(2019, Month.MAY, 31))) {
          return "10.17889/DZHW:" + stripVersionSuffix(study.getDataAcquisitionProjectId()) + ":"
              + release.getVersion();
        }
        return "10.5072/DZHW:" + stripVersionSuffix(study.getDataAcquisitionProjectId()) + ":"
            + release.getVersion();
      }
    }
    return null;
  }

  private String stripVersionSuffix(String dataAcquisitionProjectId) {
    if (StringUtils.hasText(dataAcquisitionProjectId)) {
      return VERSION_SUFFIX.matcher(dataAcquisitionProjectId).replaceFirst("");
    } else {
      return dataAcquisitionProjectId;
    }
  }
}
