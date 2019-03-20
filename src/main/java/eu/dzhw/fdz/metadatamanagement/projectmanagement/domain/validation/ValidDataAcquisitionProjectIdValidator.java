package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates project ids by valid pattern. Shadow copies must additionally provide a version suffix.
 */
public class ValidDataAcquisitionProjectIdValidator
    implements ConstraintValidator<ValidDataAcquisitionProjectId, DataAcquisitionProject> {

  private static final String ID_PATTERN = "^[a-z0-9]*$";
  private static final String SHADOW_COPY_ID_PATTERN = "^[a-z0-9]*-[0-9]+\\.[0-9]+\\.[0-9]+$";

  @Override
  public void initialize(ValidDataAcquisitionProjectId constraint) {
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DataAcquisitionProject dataAcquisitionProject,
                         ConstraintValidatorContext context) {
    String id = dataAcquisitionProject.getId();

    if (StringUtils.isEmpty(id)) {
      return false;
    }

    if (dataAcquisitionProject.isShadow()) {
      return id.matches(SHADOW_COPY_ID_PATTERN);
    } else {
      return id.matches(ID_PATTERN);
    }
  }
}
