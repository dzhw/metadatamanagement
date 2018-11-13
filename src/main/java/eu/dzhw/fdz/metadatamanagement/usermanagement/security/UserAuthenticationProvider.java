package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserAuthenticationProvider {
  /**
   * Retrieve the {@link CustomUserDetails} of the currently authenticated user.
   *
   * @return
   */
  UserDetails getUserDetails();
}
