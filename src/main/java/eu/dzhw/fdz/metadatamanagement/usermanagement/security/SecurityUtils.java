package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.Collection;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

  private SecurityUtils() {}

  /**
   * Get the login of the current user.
   */
  public static String getCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    String userName = null;
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof UserDetails) {
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        userName = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
        userName = (String) authentication.getPrincipal();
      }
    }
    return userName;
  }

  /**
   * If the current user has a specific authority (security role). The name of this method comes
   * from the isUserInRole() method in the Servlet API.
   */
  public static boolean isUserInRole(String authority) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
      return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
    }
    return false;
  }

  /**
   * Check whether the user doing the current request has been authenticated anonymously.
   * @return true if the user has been authenticated anonymously.
   */
  public static boolean isUserAnonymous() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return true;
    }
    return false;
  }
}
