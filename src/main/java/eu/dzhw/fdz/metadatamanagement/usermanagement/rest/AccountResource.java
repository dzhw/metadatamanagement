package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto.KeyAndPasswordDto;
import eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class AccountResource {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private MailService mailService;

  /**
   * Register the user.
   */
  @RequestMapping(value = "/register", method = RequestMethod.POST,
      produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDto userDto,
      HttpServletRequest request) {
    return userRepository.findOneByLogin(userDto.getLogin())
        .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
        .orElseGet(() -> userRepository.findOneByEmail(userDto.getEmail()).map(
            user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
            .orElseGet(() -> {
              User user = userService.createUserInformation(userDto.getLogin(),
                  userDto.getPassword(), userDto.getFirstName(), userDto.getLastName(),
                  userDto.getEmail().toLowerCase(), userDto.getLangKey());
              mailService.sendActivationEmail(user);
              return new ResponseEntity<>(HttpStatus.CREATED);
            }));
  }

  /**
   * Activate the registered user.
   */
  @RequestMapping(value = "/activate", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key,
      HttpServletRequest request) {
    return userService.activateRegistration(key).map(user -> {
      List<User> admins =
          userRepository.findAllByAuthoritiesContaining(new Authority(AuthoritiesConstants.ADMIN));
      mailService.sendNewAccountActivatedMail(admins, user);
      return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body("");
    }).orElse(new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * Check if the user is authenticated, and return its login.
   */
  @RequestMapping(value = "/authenticate", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> isAuthenticated(HttpServletRequest request) {
    log.debug("REST request to check if the current user is authenticated");
    return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(request.getRemoteUser());
  }

  /**
   * Get the current user.
   */
  @RequestMapping(value = "/account", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> getAccount() {
    return Optional.ofNullable(userService.getUserWithAuthorities()).map(
        user -> ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(new UserDto(user)))
        .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * Update the current user information.
   */
  @RequestMapping(value = "/account", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> saveAccount(@RequestBody UserDto userDto) {
    return userRepository.findOneByLogin(userDto.getLogin())
        .filter(u -> u.getLogin().equals(SecurityUtils.getCurrentUserLogin())).map(u -> {
          userService.updateUserInformation(userDto.getFirstName(), userDto.getLastName(),
              userDto.getEmail(), userDto.getLangKey(), userDto.isWelcomeDialogDeactivated());
          return new ResponseEntity<String>(HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * Changes the current user's password.
   */
  @RequestMapping(value = "/account/change-password", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> changePassword(@RequestBody String password) {
    if (!checkPasswordLength(password)) {
      return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
    }
    userService.changePassword(password);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * User wants to reset his password.
   */
  @RequestMapping(value = "/account/reset-password/init", method = RequestMethod.POST,
      produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<?> requestPasswordReset(@RequestBody String mail,
      HttpServletRequest request) {
    return userService.requestPasswordReset(mail).map(user -> {
      mailService.sendPasswordResetMail(user);
      return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
    }).orElse(new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST));
  }

  /**
   * User has given a new password after reset.
   */
  @RequestMapping(value = "/account/reset-password/finish", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordDto keyAndPassword) {
    if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
      return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
    }
    return userService
        .completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
        .map(user -> new ResponseEntity<String>(HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  private boolean checkPasswordLength(String password) {
    return !StringUtils.isEmpty(password) && password.length() >= UserDto.PASSWORD_MIN_LENGTH
        && password.length() <= UserDto.PASSWORD_MAX_LENGTH;
  }
}
