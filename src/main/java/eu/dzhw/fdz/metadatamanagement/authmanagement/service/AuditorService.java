package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import javax.validation.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.authmanagement.common.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidResponseException;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.common.config.audit.AuditorStore;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * Provides login name and role checks of the currently authenticated user.
 */
@Service
@RequiredArgsConstructor
public class AuditorService {

  private final UserApiService userApiService;

  private final AuditorStore auditorStore;

  /**
   * Retrieve the username of the currently authenticated user.
   */
  public String getUserLogin() {
    return SecurityUtils.getCurrentUserLogin();
  }

  /**
   * Checks if the currently authenticated user has the provided role.
   *
   * @param role the role against which
   * @return is the user in the role?
   */
  public boolean isUserInRole(@NotEmpty String role) {
    return SecurityUtils.isUserInRole(role);
  }

  /**
   * Checks if the currently authenticated user has been authenticated anonymously.
   *
   * @return is user anonymous?
   */
  public boolean isUserAnonymous() {
    return SecurityUtils.isUserAnonymous();
  }

  /**
   * Find a user based on their login and set them to the "on behalf" auditor.
   *
   * @param login a value which will be used to search for a user via an equivalency check on the
   *              user's login
   * @return info about the user with the provided {@code login}
   */
  public UserDto findAndSetOnBehalfAuditor(String login) {
    if (!StringUtils.hasLength(login)) {
      return null;
    }

    try {
      var user = userApiService.findOneByLogin(login);
      if (user.isPresent()) {
        auditorStore.setAuditor(login);

        return user.get();
      }

      throw new IllegalArgumentException("User not found: " + login);
    } catch (InvalidResponseException e) {
      throw new IllegalArgumentException("Could not find user " + login);
    }
  }
}