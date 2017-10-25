package eu.dzhw.fdz.metadatamanagement.usermanagement.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.AuthorityRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing users.
 */
@Service
@Slf4j
public class UserService {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authorityRepository;

  /**
   * Activate the user.
   */
  public Optional<User> activateRegistration(String key) {
    log.debug("Activating user for activation key {}", key);
    return userRepository.findOneByActivationKey(key)
        .map(user -> {
        // activate given user for the registration key.
          user.setActivated(true);
          user.setActivationKey(null);
          userRepository.save(user);
          log.debug("Activated user: {}", user);
          return user;
        });
  }

  /**
   * Set new password after password reset.
   */
  public Optional<User> completePasswordReset(String newPassword, String key) {
    log.debug("Reset user password for reset key {}", key);

    return userRepository.findOneByResetKey(key)
      .filter(user -> {
        LocalDateTime oneDayAgo = LocalDateTime.now()
            .minusHours(24);
        return user.getResetDate()
          .isAfter(oneDayAgo);
      })
      .map(user -> {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetKey(null);
        user.setResetDate(null);
        userRepository.save(user);
        return user;
      });
  }

  /**
   * User with given email wants to reset his password.
   */
  public Optional<User> requestPasswordReset(String mail) {
    return userRepository.findOneByEmail(mail)
      .filter(user -> user.isActivated())
      .map(user -> {
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(LocalDateTime.now());
        userRepository.save(user);
        return user;
      });
  }

  /**
   * Create the user with the given details.
   */
  public User createUserInformation(String login, String password, String firstName,
      String lastName, String email, String langKey) {

    User newUser = new User();
    String encryptedPassword = passwordEncoder.encode(password);
    newUser.setLogin(login);
    // new user gets initially a generated password
    newUser.setPassword(encryptedPassword);
    newUser.setFirstName(firstName);
    newUser.setLastName(lastName);
    newUser.setEmail(email);
    newUser.setLangKey(langKey);
    // new user is not active
    newUser.setActivated(false);
    // new user gets registration key
    newUser.setActivationKey(RandomUtil.generateActivationKey());
    Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
    Set<Authority> authorities = new HashSet<>();
    authorities.add(authority);
    newUser.setAuthorities(authorities);
    userRepository.save(newUser);
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * Update the user details.
   */
  public void updateUserInformation(String firstName, String lastName, String email,
      String langKey) {
    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
        .ifPresent(u -> {
          u.setFirstName(firstName);
          u.setLastName(lastName);
          u.setEmail(email);
          u.setLangKey(langKey);
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
   * Find the user by login.
   */
  public Optional<User> getUserWithAuthoritiesByLogin(String login) {
    return userRepository.findOneByLogin(login)
      .map(u -> {
        u.getAuthorities()
          .size();
        return u;
      });
  }

  /**
   * Find the user by id.
   */
  public User getUserWithAuthorities(String id) {
    User user = userRepository.findOne(id);
    int size = user.getAuthorities()
        .size(); // eagerly load the association
    log.debug("user.getAuthorities().size() = " + size);
    return user;
  }

  /**
   * Get currently logged in user.
   * @return the currently logged in user.
   */
  public User getUserWithAuthorities() {
    User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
        .get();
    int size = user.getAuthorities()
        .size(); // eagerly load the association
    log.debug("user.getAuthorities().size() = " + size);
    return user;
  }

  /**
   * Not activated users should be automatically deleted after 3 days.
   * <p/>
   * <p>
   * This is scheduled to get fired everyday, at 01:00 (am).
   * </p>
   */
  @Scheduled(cron = "0 0 1 * * ?")
  public void removeNotActivatedUsers() {
    LocalDateTime now = LocalDateTime.now();
    List<User> users =
        userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
    for (User user : users) {
      log.debug("Deleting not activated user {}", user.getLogin());
      userRepository.delete(user);
    }
  }
}
