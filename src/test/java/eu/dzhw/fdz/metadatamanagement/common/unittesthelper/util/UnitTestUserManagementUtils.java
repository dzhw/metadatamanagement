/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;

/**
 * This class has helper / utils methods for unit tests.
 *
 * @author Daniel Katzberg
 *
 */
@Slf4j
public class UnitTestUserManagementUtils<T> {
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
    var authorities = Set.of(AuthoritiesConstants.USER);

    return User.builder().login("test")
      .firstName("john")
      .lastName("Doe")
      .langKey("de")
      .email("john.doe@testmail.test")
      .authorities(authorities)
      .activated(false)
      .build();
  }

  /**
   * This helper methods log a account into the application by username and password
   *
   * @param login
   * @param password
   */
  public static void login(String login, String password) {
    // Empty Context (no user is logged)
    SecurityContext securityContextEmpty = SecurityContextHolder.createEmptyContext();

    // Login (!)
    securityContextEmpty
      .setAuthentication(new UsernamePasswordAuthenticationToken(login, password));

    // Set Context with logged user.
    SecurityContextHolder.setContext(securityContextEmpty);
  }

  /**
   * This helper methods log a account into the application by customuserdetails and password
   *
   * @param login
   * @param password
   */
  public static void login(CustomUserDetails login, String password) {
    // Empty Context (no user is logged)
    SecurityContext securityContextEmpty = SecurityContextHolder.createEmptyContext();

    // Login (!)
    securityContextEmpty
      .setAuthentication(new UsernamePasswordAuthenticationToken(login, password));

    // Set Context with logged user.
    SecurityContextHolder.setContext(securityContextEmpty);
  }

  /**
   * This helper methods log a account into the application by customuserdetails and password
   *
   * @param login
   * @param password
   */
  public static void logout() {
    // Logout all user
    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
  }
}
