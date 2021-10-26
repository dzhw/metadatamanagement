package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class AccountResource {

  private final UserRepository userRepository;

  private final UserService userService;

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

  private boolean checkPasswordLength(String password) {
    return !StringUtils.isEmpty(password) && password.length() >= UserDto.PASSWORD_MIN_LENGTH
        && password.length() <= UserDto.PASSWORD_MAX_LENGTH;
  }
}
