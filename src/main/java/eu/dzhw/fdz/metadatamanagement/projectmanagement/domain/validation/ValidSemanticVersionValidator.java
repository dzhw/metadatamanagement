package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import com.github.zafarkhaja.semver.ParseException;
import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;
import lombok.RequiredArgsConstructor;

/**
 * Ensure that a {@link Release} contains a valid semantic version
 * which can only be increased.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class ValidSemanticVersionValidator implements 
    ConstraintValidator<ValidSemanticVersion, DataAcquisitionProject> {
  
  private final DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidSemanticVersion constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DataAcquisitionProject project, 
      ConstraintValidatorContext context) {
    if (project.getRelease() == null) {
      return true;
    }
    String version = project.getRelease().getVersion();
    if (StringUtils.isEmpty(version)) {
      return true;
    }
    if (!version.matches("^[0-9]+\\.[0-9]+\\.[0-9]+$")) {
      return false;
    }
    try {
      Version currentVersion = Version.valueOf(version);
      Version lastVersion = null;
      Release lastRelease = dataAcquisitionProjectVersionsService.findLastRelease(project.getId());
      if (lastRelease != null) {
        lastVersion = Version.valueOf(lastRelease.getVersion());
        return currentVersion.greaterThanOrEqualTo(lastVersion);
      } else {
        return true;
      }
    } catch (ParseException ex) {
      return false;
    }
  }
}
