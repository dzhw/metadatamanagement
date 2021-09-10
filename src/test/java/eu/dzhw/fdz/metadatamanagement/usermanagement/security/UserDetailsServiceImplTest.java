/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.security;


import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

  @BeforeEach
  public void before() {
    this.user = UnitTestUserManagementUtils.getDefaultUser();
    this.user.setActivated(true);
    this.user.setPassword("sdkgfsdkkgfsdglkfglsdjkagfjklsdgfhklsdglkfglksdgslkfgsdklj12");
    this.user = this.userRepository.save(this.user);
  }

  @AfterEach
  public void after() {
    this.userRepository.deleteById(this.user.getId());
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

  @Test
  public void testLoadUserByUsernameNotActivated() {
	Assertions.assertThrows(UserNotActivatedException.class, () -> {
		// Arrange
		this.user.setActivated(false);
		this.testUtils.checkAndPrintValidation(this.user, this.validator);
		this.user = this.userRepository.save(this.user);
		
		// Act
		this.userDetailsService.loadUserByUsername(user.getLogin());
		
		// Assert
		// No Assertion, because of the deletetion		
	});
  }

  @Test
  public void testLoadUserByUsernameNotFound() {
	Assertions.assertThrows(UsernameNotFoundException.class, () -> {		
		// Arrange
		this.user.setLogin("unknown");
		this.testUtils.checkAndPrintValidation(this.user, this.validator);
		
		// Act
		this.userDetailsService.loadUserByUsername(user.getLogin());
		
		// Assert
		// No Assertion, because of the deletetion
	});
  }

}
