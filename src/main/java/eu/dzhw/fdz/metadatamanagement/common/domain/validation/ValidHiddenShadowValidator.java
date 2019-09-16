package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

/**
 * Default validator implementation for {@link ValidHiddenShadow} validation.
 */
public class ValidHiddenShadowValidator
    implements ConstraintValidator<ValidHiddenShadow, AbstractShadowableRdcDomainObject> {

  @Override
  public boolean isValid(AbstractShadowableRdcDomainObject abstractShadowableRdcDomainObject,
      ConstraintValidatorContext constraintValidatorContext) {

    if (!abstractShadowableRdcDomainObject.isHidden()) {
      return true;
    }

    if (abstractShadowableRdcDomainObject.isShadow()
        && StringUtils.hasText(abstractShadowableRdcDomainObject.getSuccessorId())) {
      return true;
    }

    return false;
  }
}
