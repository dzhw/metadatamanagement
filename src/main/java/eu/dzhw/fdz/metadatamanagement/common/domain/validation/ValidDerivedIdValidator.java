package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Default validator implementation for {@link ValidDerivedId} validation.
 */
public class ValidDerivedIdValidator
    implements ConstraintValidator<ValidDerivedId, AbstractShadowableRdcDomainObject> {

  @Override
  public boolean isValid(AbstractShadowableRdcDomainObject abstractShadowableRdcDomainObject,
                         ConstraintValidatorContext constraintValidatorContext) {

    if (!abstractShadowableRdcDomainObject.isShadow()) {
      return true;
    }

    String masterId = abstractShadowableRdcDomainObject.getMasterId();
    String id = abstractShadowableRdcDomainObject.getId();

    if (!StringUtils.hasText(masterId) || !StringUtils.hasText(id)) {
      return false;
    }

    if (!id.startsWith(masterId)) {
      return false;
    }

    String idSuffix = id.substring(masterId.length());

    return idSuffix.isEmpty() || idSuffix.matches("^-[0-9]+\\.[0-9]+\\.[0-9]+$");
  }
}
