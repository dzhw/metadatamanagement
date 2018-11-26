package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import javax.validation.constraints.NotEmpty;

/**
 * Provides login name and role checks of the currently authenticated user.
 */
public interface UserInformationProvider {
  /**
   * Retrieve the username of the currently authenticated user.
   */
  String getUserLogin();

  /**
   * Checks if the currently authenticated user has the provided role.
   */
  boolean isUserInRole(@NotEmpty String role);
}
