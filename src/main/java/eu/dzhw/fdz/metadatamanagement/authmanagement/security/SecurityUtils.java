package eu.dzhw.fdz.metadatamanagement.authmanagement.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

  private SecurityUtils() {}

  /**
   * Get the login of the current user.
   */
  public static String getCurrentUserLogin() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    return authentication != null ? authentication.getName() : null;
  }

  /**
   * If the current user has a specific authority (security role). The name of this method comes
   * from the isUserInRole() method in the Servlet API.
   */
  public static boolean isUserInRole(String authority) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    return authentication != null && authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals(authority));
  }

  /**
   * Check whether the user doing the current request has been authenticated anonymously.
   * @return true if the user has been authenticated anonymously.
   */
  public static boolean isUserAnonymous() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication == null || authentication instanceof AnonymousAuthenticationToken;
  }
}
