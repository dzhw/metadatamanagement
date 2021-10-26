package eu.dzhw.fdz.metadatamanagement.usermanagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing users.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  @Value("${metadatamanagement.server.instance-index}")
  private Integer instanceId;

  /**
   * Update the user details.
   */
  public void updateUserInformation(String firstName, String lastName, String email,
      String langKey, boolean welcomeDialogDeactivated) {
    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
        .ifPresent(u -> {
          u.setFirstName(firstName);
          u.setLastName(lastName);
          u.setEmail(email);
          u.setLangKey(langKey);
          u.setWelcomeDialogDeactivated(welcomeDialogDeactivated);
          userRepository.save(u);
          log.debug("Changed Information for User: {}", u);
        });
  }

  /**
   * Change the password of the current user.
   * @param password the new password.
   */
  public void changePassword(String password) {
    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
        .ifPresent(u -> {
          String encryptedPassword = passwordEncoder.encode(password);
          u.setPassword(encryptedPassword);
          userRepository.save(u);
          log.debug("Changed password for User: {}", u);
        });
  }

  /**
   * Get currently logged in user.
   * @return the currently logged in user.
   */
  public Optional<User> getUserWithAuthorities() {
    return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
  }

  /**
   * Not activated users should be automatically deleted after 3 days.
   * This is scheduled to get fired everyday, at 01:00 (am).
   */
  @Scheduled(cron = "0 0 1 * * ?")
  public void removeNotActivatedUsers() {
    if (instanceId != 0) {
      return;
    }
    LocalDateTime now = LocalDateTime.now();
    List<User> users =
        userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
    for (User user : users) {
      log.debug("Deleting not activated user {}", user.getLogin());
      userRepository.delete(user);
    }
  }
}
