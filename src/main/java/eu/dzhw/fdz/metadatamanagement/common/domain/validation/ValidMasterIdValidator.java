package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Default validator implementation of {@link ValidMasterId}.
 */
public class ValidMasterIdValidator
    implements ConstraintValidator<ValidMasterId, AbstractShadowableRdcDomainObject> {

  private String pattern;

  @Override
  public void initialize(ValidMasterId constraintAnnotation) {
    this.pattern = constraintAnnotation.pattern();
  }

  @Override
  public boolean isValid(AbstractShadowableRdcDomainObject abstractShadowableRdcDomainObject,
                         ConstraintValidatorContext constraintValidatorContext) {
    if (abstractShadowableRdcDomainObject.isShadow()) {
      return true;
    } else {
      String masterId = abstractShadowableRdcDomainObject.getMasterId();
      return StringUtils.hasText(masterId) && masterId.matches(pattern);
    }
  }
}
