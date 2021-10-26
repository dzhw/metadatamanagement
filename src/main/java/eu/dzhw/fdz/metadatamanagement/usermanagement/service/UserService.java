package eu.dzhw.fdz.metadatamanagement.usermanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
