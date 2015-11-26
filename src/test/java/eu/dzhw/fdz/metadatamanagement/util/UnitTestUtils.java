/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.util;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import eu.dzhw.fdz.metadatamanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.domain.builders.UserBuilder;
import eu.dzhw.fdz.metadatamanagement.security.AuthoritiesConstants;

/**
 * This class has helper / utils methods for unit tests.
 * 
 * @author Daniel Katzberg
 *
 */
public class UnitTestUtils<T> {

  private final Logger log = LoggerFactory.getLogger(UnitTestUtils.class);

  /**
   * @param object The object for validation.
   * @param validator The validator for the validation
   * @return a set of validations.
   */
  public Set<ConstraintViolation<T>> checkAndPrintValidation(T object, Validator validator) {
    Set<ConstraintViolation<T>> set = validator.validate(object);
    for (ConstraintViolation<T> constrain : set) {
      log.warn("Property: " + object.getClass()
        .getSimpleName() + "." + constrain.getPropertyPath() + " | Message: "
          + constrain.getMessage() + " | Invalid Value: " + constrain.getInvalidValue());
    }

    return set;
  }

  /**
   * Creates a default user with filled basic fields,
   * 
   * @return
   */
  public static User getDefaultUser() {
    Set<Authority> authorities = new HashSet<>();
    Authority authority = new Authority();
    authority.setName(AuthoritiesConstants.USER);
    authorities.add(authority);

    return new UserBuilder().withLogin("test")
      .withFirstName("john")
      .withLastName("Doe")
      .withLangKey("de")
      .withEmail("john.doe@testmail.test")
      .withAuthorities(authorities)
      .withActivated(false)
      .build();
  }

  /**
   * This helper methods
   * 
   * @param login
   * @param password
   */
  public static void login(String login, String password) {
    //Empty Context (no user is logged)
    SecurityContext securityContextEmpty = SecurityContextHolder.createEmptyContext();
    
    //Login (!)
    securityContextEmpty
      .setAuthentication(new UsernamePasswordAuthenticationToken(login, password));
    
    //Set Context with logged user.
    SecurityContextHolder.setContext(securityContextEmpty);
  }

}
