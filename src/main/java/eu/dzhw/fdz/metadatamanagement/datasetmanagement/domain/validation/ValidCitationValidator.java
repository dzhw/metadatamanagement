package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Default implementation of {@link ValidCitation}.
 */
public class ValidCitationValidator implements ConstraintValidator<ValidCitation, I18nString> {
   public void initialize(ValidCitation constraint) {
   }

   public boolean isValid(I18nString i18nString, ConstraintValidatorContext context) {
      if(i18nString == null) {
         return true;
      } else {
         return StringUtils.hasText(i18nString.getDe()) || StringUtils.hasText(i18nString.getEn());
      }
   }
}
