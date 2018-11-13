package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link UserAuthenticationProvider}
 */
@Service
class UserAuthenticationProviderImpl implements UserAuthenticationProvider {

  @Override
  public UserDetails getUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new IllegalStateException("Expected to have an authenticated user"); //TODO Might have a better way to handle an expected authentication
    }

    Object details = authentication.getPrincipal();

    if (details instanceof UserDetails) {
      return (UserDetails) details;
    } else {
      throw new IllegalStateException("Expected user details of authentication to be an instance of " + UserDetails.class.getName() + " but was " + (details != null ? details.getClass() : null));
    }
  }
}
