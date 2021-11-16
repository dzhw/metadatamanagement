package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;

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
   * Check if a user is authenticated.
   *
   * @return true if the user is authenticated, false otherwise
   */
  public static boolean isAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Collection<? extends GrantedAuthority> authorities =
        securityContext.getAuthentication().getAuthorities();
    if (authorities != null) {
      for (GrantedAuthority authority : authorities) {
        if (authority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Return the current user id, or throws an exception, if the user is not authenticated yet.
   *
   * @return the current user id
   */
  public static String getCurrentUserId() {
    return getCurrentUser().getId();
  }

  /**
   * Return the current user, or throws an exception, if the user is not authenticated yet.
   *
   * @return the current user
   */
  public static CustomUserDetails getCurrentUser() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
      return (CustomUserDetails) authentication.getPrincipal();
    }
    throw new IllegalStateException("User not found!");
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

  public static boolean isUserInRole(String authority, User user) {
    return user.getAuthorities().stream()
        .anyMatch(userAuthority -> userAuthority.equals(authority));
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
