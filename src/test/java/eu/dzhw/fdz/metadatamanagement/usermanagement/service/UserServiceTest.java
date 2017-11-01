package eu.dzhw.fdz.metadatamanagement.usermanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.util.RandomUtil;

/**
 * Test class for the UserResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see UserService
 */
public class UserServiceTest extends AbstractTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @After
  public void after() {
    // Log the user out, if any user was login by a test method
    UnitTestUserManagementUtils.logout();
  }

  @Test
  public void assertThatUserMustExistToResetPassword() {
    Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
    assertThat(maybeUser.isPresent()).isFalse();

    maybeUser = userService.requestPasswordReset("admin@localhost");
    assertThat(maybeUser.isPresent()).isTrue();

    assertThat(maybeUser.get()
      .getEmail()).isEqualTo("admin@localhost");
    assertThat(maybeUser.get()
      .getResetDate()).isNotNull();
    assertThat(maybeUser.get()
      .getResetKey()).isNotNull();
  }

  @Test
  public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
    User user = userService.createUserInformation("johndoe", "johndoe", "John", "Doe",
        "john.doe@localhost", "en-US");
    Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
    assertThat(maybeUser.isPresent()).isFalse();
    userRepository.delete(user);
  }

  @Test
  public void assertThatResetKeyMustNotBeOlderThan24Hours() {
    User user = userService.createUserInformation("johndoe", "johndoe", "John", "Doe",
        "john.doe@localhost", "en-US");

    LocalDateTime daysAgo = LocalDateTime.now()
      .minusHours(25);
    String resetKey = RandomUtil.generateResetKey();
    user.setActivated(true);
    user.setResetDate(daysAgo);
    user.setResetKey(resetKey);

    userRepository.save(user);

    Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());

    assertThat(maybeUser.isPresent()).isFalse();

    userRepository.delete(user);
  }

  @Test
  public void assertThatResetKeyMustBeValid() {
    User user = userService.createUserInformation("johndoe", "johndoe", "John", "Doe",
        "john.doe@localhost", "en-US");

    LocalDateTime daysAgo = LocalDateTime.now()
      .minusHours(25);
    user.setActivated(true);
    user.setResetDate(daysAgo);
    user.setResetKey("1234");
    userRepository.save(user);
    Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
    assertThat(maybeUser.isPresent()).isFalse();
    userRepository.delete(user);
  }

  @Test  
  public void assertThatUserCanResetPassword() {
    User user = userService.createUserInformation("johndoe", "johndoe", "John", "Doe",
        "john.doe@localhost", "en-US");
    String oldPassword = user.getPassword();
    LocalDateTime daysAgo = LocalDateTime.now()
      .minusHours(2);
    String resetKey = RandomUtil.generateResetKey();
    user.setActivated(true);
    user.setResetDate(daysAgo);
    user.setResetKey(resetKey);
    userRepository.save(user);
    Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
    assertThat(maybeUser.isPresent()).isTrue();
    assertThat(maybeUser.get()
      .getResetDate()).isNull();
    assertThat(maybeUser.get()
      .getResetKey()).isNull();
    assertThat(maybeUser.get()
      .getPassword()).isNotEqualTo(oldPassword);

    userRepository.delete(user);
  }

  @Test
  public void testFindNotActivatedUsersByCreationDateBefore() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();
    User user1 = User.builder().login("user1login")
      .password(this.passwordEncoder.encode("User1Password"))
      .activated(false)
      .email("user1@dzhw.eu")
      .build();
    user1.setCreatedDate(now.minusDays(4));
    User user2 = User.builder().login("user2login")
      .password(this.passwordEncoder.encode("User2Password"))
      .activated(false)
      .email("user2@dzhw.eu")
      .build();

    // Update User 1
    user1 = this.userRepository.save(user1);
    user1.setCreatedDate(now.minusDays(5));
    user1 = this.userRepository.save(user1);
    
    // Update User 2
    user2 = this.userRepository.save(user2);
    user2.setCreatedDate(now.minusDays(5));
    user2 = this.userRepository.save(user2);

    this.userRepository.save(user2);

    // Act
    List<User> usersBefore =
        userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
    userService.removeNotActivatedUsers();
    List<User> usersAfter =
        userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));

    // Assert
    assertThat(usersBefore.size(), is(2));
    assertThat(usersAfter.size(), is(0));

  }

  @Test
  public void testGetUserWithAuthorities() {
    // Arrange
    UnitTestUserManagementUtils.login("admin", "admin");

    // Act
    User user = this.userService.getUserWithAuthorities();

    // Assert
    assertThat(user.getAuthorities()
      .size(), is(3));
    assertThat(user.getEmail(), is("admin@localhost"));
    assertThat(user.getId(), is("user-2"));
  }

  @Test
  public void testGetUserWithAuthoritiesWithId() {
    // Arrange

    // Act
    User user = this.userService.getUserWithAuthorities("user-2");

    // Assert
    assertThat(user.getAuthorities()
      .size(), is(3));
    assertThat(user.getEmail(), is("admin@localhost"));
    assertThat(user.getId(), is("user-2"));
  }

  @Test
  public void testChangePassword() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();
    User user = User.builder().login("user1login")
      .password(this.passwordEncoder.encode("User1Password"))
      .activated(false)
      .build();
    user.setCreatedDate(now.minusDays(4));
    user = this.userRepository.save(user);
    UnitTestUserManagementUtils.login("user1login", "User1Password");

    // Act
    this.userService.changePassword("User2Password");
    UnitTestUserManagementUtils.logout();
    UnitTestUserManagementUtils.login("user1login", "User2Password");

    // Assert
    assertThat(this.userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
      .get()
      .getId(), is(user.getId()));

    this.userRepository.delete(user.getId());
    UnitTestUserManagementUtils.logout();
  }
}
