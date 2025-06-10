package eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper;

import java.util.regex.Pattern;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

  private static final String DATACITE_PREFIX_TEST = "10.83079";
  private static final String DATACITE_PREFIX_PROD = "10.21249";

  /**
   * Create a doi for releases to DataCite.
   *
   * @param dataAcquisitionProjectId the project which gets the doi
   * @param release the release
   * @return a doi (if required)
   */
  public String buildDataOrAnalysisPackageDoiForDataCite(String dataAcquisitionProjectId, Release release) {
    if (release != null && StringUtils.hasText(dataAcquisitionProjectId)
      // && Version.valueOf(release.getVersion()).greaterThanOrEqualTo(Version.valueOf("1.0.0"))
    ) {
      if (environment.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_PROD))) {
        return DATACITE_PREFIX_PROD + "/DZHW:" + stripVersionSuffix(dataAcquisitionProjectId) + ":"
          + release.getVersion();
      } else {
        return DATACITE_PREFIX_TEST + "/DZHW:" + stripVersionSuffix(dataAcquisitionProjectId) + ":"
          + release.getVersion();
      }
    }
    return null;
  }

  /**
   * Returns the DOI prefix according to the current environment.
   * @return the doi prefix
   */
  public String getDoiPrefixForDataCite() {
    if (environment.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_PROD))) {
      return DATACITE_PREFIX_PROD;
    } else {
      return DATACITE_PREFIX_TEST;
    }
  }

  public String getDoiSuffixForDataCite(String dataAcquisitionProjectId, Release release) {
    String doi = this.buildDataOrAnalysisPackageDoiForDataCite(dataAcquisitionProjectId, release);
    return doi.split("/")[1];
  }

  private String stripVersionSuffix(String dataAcquisitionProjectId) {
    if (StringUtils.hasText(dataAcquisitionProjectId)) {
      return VERSION_SUFFIX.matcher(dataAcquisitionProjectId).replaceFirst("");
    } else {
      return dataAcquisitionProjectId;
    }
  }
}
