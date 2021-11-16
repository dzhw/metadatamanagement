package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AuthUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.AuthorityRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto.KeyAndPasswordDto;
import eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.UserService;

/**
 * Test class for the AccountResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see UserService
 */
public class AccountResourceTest extends AbstractTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authorityRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private AuthUserService authUserService;

  @Autowired
  private Validator validator;

  @Mock
  private UserService mockUserService;

  @Mock
  private MailService mockMailService;

  private MockMvc restUserMockMvc;

  private MockMvc restMvc;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mockMailService.sendActivationEmail(any(User.class))).thenReturn(null);

    AccountResource accountResource =
        new AccountResource(userRepository, userService, authUserService, mockMailService);

    AccountResource accountUserMockResource =
        new AccountResource(userRepository, mockUserService, authUserService, mockMailService);

    this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
    this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
  }

  @AfterEach
  public void cleanUp() {
    UnitTestUserManagementUtils.logout();
    // assert that all tests remove their users!
    assertThat(userRepository.count()).isEqualTo(4);
  }

  @Test
  public void testNonAuthenticatedUser() throws Exception {
    restUserMockMvc.perform(get("/api/authenticate").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().string(""));
  }

  @Test
  public void testAuthenticatedUser() throws Exception {
    restUserMockMvc.perform(get("/api/authenticate").with(request -> {
      request.setRemoteUser("test");
      return request;
    }).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().string("test"));
  }

  @Test
  public void testGetExistingAccount() throws Exception {

    User user = UnitTestUserManagementUtils.getDefaultUser();
    when(mockUserService.getUserWithAuthorities()).thenReturn(Optional.of(user));

    restUserMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.login").value("test"))
        .andExpect(jsonPath("$.firstName").value("john"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.email").value("john.doe@testmail.test"))
        .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.USER));
  }

  @Test
  public void testActivateAccount() throws Exception {

    // Arrange
    User user = UnitTestUserManagementUtils.getDefaultUser();
    user.setPassword("sdkgfsdkkgfsdglkfglsdjkagfjklsdgfhklsdglkfglksdgslkfgsdklj12");
    user.setActivationKey("testActivateTrue");
    user.setActivated(false);

    Set<ConstraintViolation<User>> constrains =
        new UnitTestUserManagementUtils<User>().checkAndPrintValidation(user, this.validator);
    assertThat(constrains.size(), is(0));

    // Act
    this.userRepository.save(user);

    // Assert
    this.restMvc
        .perform(get("/api/activate?key=testActivateTrue").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Delete
    this.userRepository.deleteById(user.getId());
  }

  @Test
  public void testRequestPasswordReset() throws Exception {
    // Arrange
    User user = UnitTestUserManagementUtils.getDefaultUser();
    user.setPassword("sdkgfsdkkgfsdglkfglsdjkagfjklsdgfhklsdglkfglksdgslkfgsdklj12");
    user.setActivationKey("testActivateTrue");
    user.setActivated(true);
    user = this.userRepository.save(user);

    // Act

    // Assert
    this.restMvc.perform(post("/api/account/reset-password/init").accept(MediaType.TEXT_PLAIN)
        .content(user.getEmail())).andExpect(status().isOk());

    this.userRepository.deleteById(user.getId());
  }


  @Test
  public void testSaveAccount() throws Exception {
    // Arrange
    Optional<User> userO = this.userRepository.findOneByLogin("admin");
    User user = userO.get();
    UserDto dto = new UserDto(user);
    UnitTestUserManagementUtils.login(user.getLogin(), user.getPassword());

    // Act

    // Assert
    this.restMvc.perform(post("/api/account").contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dto))).andExpect(status().isOk());
  }

  @Test
  public void testSaveAccountWithNoLogin() throws Exception {
    // Arrange
    Optional<User> userO = this.userRepository.findOneByLogin("admin");
    User user = userO.get();
    UserDto dto = new UserDto(user);

    // Act

    // Assert
    this.restMvc
        .perform(post("/api/account").contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().is5xxServerError());
  }

  @Test
  public void testRequestPasswordResetWithError() throws Exception {
    // Arrange


    User user = UnitTestUserManagementUtils.getDefaultUser();
    user.setPassword("sdkgfsdkkgfsdglkfglsdjkagfjklsdgfhklsdglkfglksdgslkfgsdklj12");
    user.setActivationKey("testActivateTrue");
    user.setLangKey("de");
    user.setActivated(false); // <- Only activated account can do a password reset

    this.userRepository.save(user);

    // Act

    // Assert
    this.restMvc.perform(post("/api/account/reset-password/init").accept(MediaType.TEXT_PLAIN)
        .content("john.doe@jhipter.com")).andExpect(status().is4xxClientError());

    this.userRepository.deleteById(user.getId());
  }

  @Test
  public void testFinishPasswordReset() throws Exception {

    // Arrange
    User user = UnitTestUserManagementUtils.getDefaultUser();
    user.setPassword("sdkgfsdkkgfsdglkfglsdjkagfjklsdgfhklsdglkfglksdgslkfgsdklj12");
    user.setActivationKey("testActivateTrue");
    user.setActivated(true);
    user.setResetKey("ActivationKey");
    user.setResetDate(LocalDateTime.now().minusHours(1L));
    this.userRepository.save(user);

    KeyAndPasswordDto dto = new KeyAndPasswordDto();
    dto.setKey(user.getResetKey());
    dto.setNewPassword("newPassword");

    // Act

    // Assert
    this.restMvc.perform(post("/api/account/reset-password/finish")
        .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isOk());

    this.userRepository.deleteById(user.getId());
  }

  @Test
  public void testFinishPasswordResetWithError() throws Exception {

    // Arrange
    User user = UnitTestUserManagementUtils.getDefaultUser();
    user.setPassword("sdkgfsdkkgfsdglkfglsdjkagfjklsdgfhklsdglkfglksdgslkfgsdklj12");
    user.setActivationKey("testActivateTrue");
    user.setActivated(true);
    user.setResetKey("ActivationKey");
    user.setResetDate(LocalDateTime.now().minusHours(1L));
    this.userRepository.save(user);

    KeyAndPasswordDto dto = new KeyAndPasswordDto();
    dto.setKey(user.getResetKey());
    dto.setNewPassword("abc");// <- password is too short.

    // Act

    // Assert
    this.restMvc
        .perform(post("/api/account/reset-password/finish").contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().is4xxClientError());

    this.userRepository.deleteById(user.getId());
  }

  @Test
  public void testChangePassword() throws Exception {
    // Arrange

    // Act

    // Assert
    this.restMvc.perform(
        post("/api/account/change-password").accept(MediaType.APPLICATION_JSON).content("password"))
        .andExpect(status().isOk());
  }

  @Test
  public void testChangePasswordError() throws Exception {
    // Arrange

    // Act

    // Assert
    this.restMvc
        .perform(
            post("/api/account/change-password").accept(MediaType.APPLICATION_JSON).content("no"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void testGetUnknownAccount() throws Exception {
    when(mockUserService.getUserWithAuthorities()).thenReturn(Optional.empty());

    restUserMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());
  }

  @Test
  public void testRegisterValid() throws Exception {
    UserDto u = new UserDto("joe", // login
        "password", // password
        "Joe", // firstName
        "Shmoe", // lastName
        "joe@example.com", // e-mail
        true, // activated
        "en", // langKey
        new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)), false);

    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

    Optional<User> user = userRepository.findOneByLogin("joe");
    assertThat(user.isPresent()).isTrue();

    userRepository.deleteByEmail(u.getEmail());
  }

  @Test
  public void testRegisterInvalidLogin() throws Exception {
    UserDto u = new UserDto("funky-log!n", // login <-- invalid
        "password", // password
        "Funky", // firstName
        "One", // lastName
        "funky@example.com", // e-mail
        true, // activated
        "en", // langKey
        new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)), false);

    restUserMockMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isBadRequest());

    Optional<User> user = userRepository.findOneByEmail("funky@example.com");
    assertThat(user.isPresent()).isFalse();
  }

  @Test
  public void testRegisterInvalidEmail() throws Exception {
    UserDto u = new UserDto("bob", // login
        "password", // password
        "Bob", // firstName
        "Green", // lastName
        "invalid", // e-mail <-- invalid
        true, // activated
        "en", // langKey
        new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)), false);

    restUserMockMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isBadRequest());

    Optional<User> user = userRepository.findOneByLogin("bob");
    assertThat(user.isPresent()).isFalse();
  }

  @Test
  public void testRegisterDuplicateLogin() throws Exception {
    // Good
    UserDto u = new UserDto("alice", // login
        "password", // password
        "Alice", // firstName
        "Something", // lastName
        "alice@example.com", // e-mail
        true, // activated
        "en", // langKey
        new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)), false);

    // Duplicate login, different e-mail
    UserDto dup = new UserDto(u.getLogin(), u.getPassword(), u.getLogin(), u.getLastName(),
        "alicejr@example.com", true, u.getLangKey(), u.getAuthorities(), false);

    // Good user
    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

    // Duplicate login
    restMvc
        .perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dup)))
        .andExpect(status().is4xxClientError());

    Optional<User> userDup = userRepository.findOneByEmail("alicejr@example.com");
    assertThat(userDup.isPresent()).isFalse();

    userRepository.deleteByEmail(u.getEmail());
  }

  @Test
  public void testRegisterDuplicateEmail() throws Exception {
    // Good
    UserDto u = new UserDto("john", // login
        "password", // password
        "John", // firstName
        "Doe", // lastName
        "john@example.com", // e-mail
        true, // activated
        "en", // langKey
        new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)), false);

    // Duplicate e-mail, different login
    UserDto dup = new UserDto("johnjr", u.getPassword(), u.getLogin(), u.getLastName(),
        u.getEmail(), true, u.getLangKey(), u.getAuthorities(), false);

    // Good user
    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

    // Duplicate e-mail
    restMvc
        .perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dup)))
        .andExpect(status().is4xxClientError());

    Optional<User> userDup = userRepository.findOneByLogin("johnjr");
    assertThat(userDup.isPresent()).isFalse();

    userRepository.deleteByEmail(u.getEmail());
  }

  @Test
  public void testRegisterAdminIsIgnored() throws Exception {
    UserDto u = new UserDto("badguy", // login
        "password", // password
        "Bad", // firstName
        "Guy", // lastName
        "badguy@example.com", // e-mail
        true, // activated
        "en", // langKey
        new HashSet<>(Arrays.asList(AuthoritiesConstants.ADMIN)), // <-- only admin should be able
                                                                  // to
        false // do that
    );

    restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

    Optional<User> userDup = userRepository.findOneByLogin("badguy");
    assertThat(userDup.isPresent()).isTrue();
    assertThat(userDup.get().getAuthorities()).hasSize(1)
        .containsExactly(authorityRepository.findById(AuthoritiesConstants.USER).get());

    userRepository.deleteByEmail(u.getEmail());
  }
}
