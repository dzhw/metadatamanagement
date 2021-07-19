package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import javax.validation.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;

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

  /**
   * Checks if the currently authenticated user has been authenticated anonymously.
   */
  boolean isUserAnonymous();

  /**
   * Switches the security context to the given user or logs the current user out.
   * 
   * @param login the username to login, if null the security context will be cleared
   * @return the user information of the logged in user
   */
  User switchToUser(String login);
}
