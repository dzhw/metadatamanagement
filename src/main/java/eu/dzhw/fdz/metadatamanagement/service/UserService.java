package eu.dzhw.fdz.metadatamanagement.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.dzhw.fdz.metadatamanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.repository.AuthorityRepository;
import eu.dzhw.fdz.metadatamanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.repository.search.UserSearchRepository;
import eu.dzhw.fdz.metadatamanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.service.util.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  @Inject
  private PasswordEncoder passwordEncoder;

  @Inject
  private UserRepository userRepository;

  @Inject
  private UserSearchRepository userSearchRepository;

  @Inject
  private AuthorityRepository authorityRepository;

  /**
   * This method handels the activation of registrations.
   * @param key The key of the registration.
   * @return A representation of the user (if successful activation).
   */
  public Optional<User> activateRegistration(String key) {
    log.debug("Activating user for activation key {}", key);
    userRepository.findOneByActivationKey(key).map(user -> {
      // activate given user for the registration key.
      user.setActivated(true);
      user.setActivationKey(null);
      userRepository.save(user);
      userSearchRepository.save(user);
      log.debug("Activated user: {}", user);
      return user;
    });
    return Optional.empty();
  }

  /**
   * This method handles the complete password reset.
   * @param newPassword the new password.
   * @param key the key for the password reset.
   * @return return the depending user.
   */
  public Optional<User> completePasswordReset(String newPassword, String key) {
    log.debug("Reset user password for reset key {}", key);

    return userRepository.findOneByResetKey(key).filter(user -> {
      ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
      return user.getResetDate().isAfter(oneDayAgo);
    }).map(user -> {
      user.setPassword(passwordEncoder.encode(newPassword));
      user.setResetKey(null);
      user.setResetDate(null);
      userRepository.save(user);
      return user;
    });
  }

  /**
   * This method handles the request of a password reset.
   * @param mail the email address for the password reset.
   * @return returns the user which started the password reset.
   */
  public Optional<User> requestPasswordReset(String mail) {
    return userRepository.findOneByEmail(mail).filter(user -> user.getActivated()).map(user -> {
      user.setResetKey(RandomUtil.generateResetKey());
      user.setResetDate(ZonedDateTime.now());
      userRepository.save(user);
      return user;
    });
  }

  /**
   * This method creates the detailed user information.
   * @param login the login of a user.
   * @param password the password of a user.
   * @param firstName the first name of a user.
   * @param lastName the last name of a user.
   * @param email the email of a user.
   * @param langKey the laguage key.
   * @return the created user representation.
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
    Authority authority = authorityRepository.findOne("ROLE_USER");
    Set<Authority> authorities = new HashSet<>();
    authorities.add(authority);
    newUser.setAuthorities(authorities);
    userRepository.save(newUser);
    userSearchRepository.save(newUser);
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This method updates some fields of the user.
   * @param firstName the first name of the user.
   * @param lastName the last namne of the user.
   * @param email the email of a user.
   * @param langKey the language key of a user.
   */
  public void updateUserInformation(String firstName, String lastName, String email,
      String langKey) {
    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
      u.setFirstName(firstName);
      u.setLastName(lastName);
      u.setEmail(email);
      u.setLangKey(langKey);
      userRepository.save(u);
      userSearchRepository.save(u);
      log.debug("Changed Information for User: {}", u);
    });
  }

  /**
   * This method handles the password change.
   * @param password the password of a user.
   */
  public void changePassword(String password) {
    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
      String encryptedPassword = passwordEncoder.encode(password);
      u.setPassword(encryptedPassword);
      userRepository.save(u);
      log.debug("Changed password for User: {}", u);
    });
  }

  /**
   * @param login the login of a user.
   * @return the succsessfully logged user.
   */
  @Transactional(readOnly = true)
  public Optional<User> getUserWithAuthoritiesByLogin(String login) {
    return userRepository.findOneByLogin(login).map(u -> {
      u.getAuthorities().size();
      return u;
    });
  }

  /**
   * @param id the id of an user.
   * @return a user with authorities by an given id
   */
  @Transactional(readOnly = true)
  public User getUserWithAuthorities(Long id) {
    User user = userRepository.findOne(id);
    int size = user.getAuthorities().size(); // eagerly load the association
    log.debug("Size of user authorities: " + size);
    return user;
  }

  /**
   * @return returns the current user with authorities.
   */
  @Transactional(readOnly = true)
  public User getUserWithAuthorities() {
    User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
    int size = user.getAuthorities().size(); // eagerly load the association
    log.debug("Size of user authorities: " + size);
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
    ZonedDateTime now = ZonedDateTime.now();
    List<User> users =
        userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
    for (User user : users) {
      log.debug("Deleting not activated user {}", user.getLogin());
      userRepository.delete(user);
      userSearchRepository.delete(user);
    }
  }
}
