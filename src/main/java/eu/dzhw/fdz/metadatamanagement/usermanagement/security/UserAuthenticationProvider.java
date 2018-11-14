package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Provides {@link UserDetails} of the currently authenticated user.
 */
public interface UserAuthenticationProvider {
  /**
   * Retrieve the {@link CustomUserDetails} of the currently authenticated user.
   */
  UserDetails getUserDetails();
}
