package eu.dzhw.fdz.metadatamanagement.usermanagement.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;

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

  @AfterEach
  public void after() {
    // Log the user out, if any user was login by a test method
    UnitTestUserManagementUtils.logout();
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

    this.userRepository.deleteById(user.getId());
    UnitTestUserManagementUtils.logout();
  }
}
