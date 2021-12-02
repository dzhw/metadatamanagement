package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.common.config.audit.AuditorStore;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * Provides login name and role checks of the currently authenticated user.
 */
@Service
@RequiredArgsConstructor
public class AuditorService implements AutoCloseable {

  private final UserApiService userApiService;

  private final AuditorStore auditorStore;

  /**
   * Find a user based on their login and set them to the "on behalf" auditor.
   *
   * @param login a value which will be used to search for a user via an equivalency check on the
   *              user's login
   * @return info about the user with the provided {@code login}
   */
  public UserDto findAndSetOnBehalfAuditor(String login) {
    if (!StringUtils.hasText(login)) {
      return null;
    }

    var user = userApiService.findOneByLogin(login);
    if (user.isPresent()) {
      auditorStore.setAuditor(login);

      return user.get();
    }

    throw new IllegalArgumentException("User not found: " + login);
  }

  @Override
  public void close() {
    auditorStore.clear();
  }
}
