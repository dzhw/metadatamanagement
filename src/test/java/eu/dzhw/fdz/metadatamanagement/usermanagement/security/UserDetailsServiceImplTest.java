/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.security;


import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;

/**
 * @author Daniel Katzberg
 *
 */
public class UserDetailsServiceImplTest extends AbstractTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private Validator validator;

  private User user;

  private UnitTestUserManagementUtils<User> testUtils = new UnitTestUserManagementUtils<>();

  @Before
  public void before() {
    this.user = UnitTestUserManagementUtils.getDefaultUser();
    this.user.setActivated(true);
    this.user.setPassword("sdkgfsdkkgfsdglkfglsdjkagfjklsdgfhklsdglkfglksdgslkfgsdklj12");
    this.user = this.userRepository.save(this.user);
  }

  @After
  public void after() {
    this.userRepository.delete(this.user);
  }

  @Test
  public void testLoadUserByUsername() {
    // Arrange
    Set<ConstraintViolation<User>> constrains =
        this.testUtils.checkAndPrintValidation(this.user, this.validator);

    // Act
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(this.user.getLogin());

    // Assert
    assertThat(constrains.size(), is(0));
    assertThat(userDetails, not(nullValue()));
    assertThat(userDetails.getUsername(), is(this.user.getLogin()));
  }


  @Test(expected = UserNotActivatedException.class)
  public void testLoadUserByUsernameNotActivated() {

    // Arrange
    this.user.setActivated(false);
    this.testUtils.checkAndPrintValidation(this.user, this.validator);
    this.user = this.userRepository.save(this.user);

    // Act
    this.userDetailsService.loadUserByUsername(user.getLogin());

    // Assert
    // No Assertion, because of the deletetion
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testLoadUserByUsernameNotFound() {

    // Arrange
    this.user.setLogin("unknown");
    this.testUtils.checkAndPrintValidation(this.user, this.validator);

    // Act
    this.userDetailsService.loadUserByUsername(user.getLogin());

    // Assert
    // No Assertion, because of the deletetion
  }

}
