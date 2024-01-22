package eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper;

import java.util.regex.Pattern;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
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
   * @param dataAcquisitionProjectId the project which gets the doi
   * @param release the release
   * @return a doi (if required)
   */
  public String buildDataOrAnalysisPackageDoi(String dataAcquisitionProjectId, Release release) {
    if (release != null && StringUtils.hasText(dataAcquisitionProjectId)
       // && Version.valueOf(release.getVersion()).greaterThanOrEqualTo(Version.valueOf("1.0.0"))
    ) {
      if (environment.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_PROD))) {
        return "10.21249/DZHW:" + stripVersionSuffix(dataAcquisitionProjectId) + ":"
            + release.getVersion();
      } else {
        return "10.17889/DZHW:" + stripVersionSuffix(dataAcquisitionProjectId) + ":"
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
