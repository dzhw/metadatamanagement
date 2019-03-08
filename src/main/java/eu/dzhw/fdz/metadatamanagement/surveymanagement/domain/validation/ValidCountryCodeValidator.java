package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Default implementation of {@link ValidCountryCode}.
 */
public class ValidCountryCodeValidator implements ConstraintValidator<ValidCountryCode, String> {

  private static final List<String> COUNTRIES;

  static {
    COUNTRIES = Collections.unmodifiableList(Arrays.asList(Locale.getISOCountries()));
  }

  @Override
  public boolean isValid(String countryCode, ConstraintValidatorContext
      constraintValidatorContext) {

    if (StringUtils.isEmpty(countryCode)) {
      return false;
    }

    return COUNTRIES.contains(countryCode);
  }
}
