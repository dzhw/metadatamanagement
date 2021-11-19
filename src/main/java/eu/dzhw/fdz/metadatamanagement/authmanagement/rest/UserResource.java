package eu.dzhw.fdz.metadatamanagement.authmanagement.rest;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.authmanagement.common.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.common.dto.UserWithRolesDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidUserApiResponseException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class UserResource {

  private final UserApiService userApiService;

  /**
   * Get the "login" user with less details.
   */
  @RequestMapping(value = "/users/{login}/public", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.PUBLISHER})
  public ResponseEntity<UserWithRolesDto> getUserPublic(@PathVariable String login) {
    log.debug("REST request to get User (with roles) : {}", login);
    try {
      var user = userApiService.findOneWithAuthoritiesByLogin(login);
      return user.map(userDto -> ResponseEntity.ok()
          .cacheControl(CacheControl.noCache())
          .body(
              userDto
          )).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (InvalidUserApiResponseException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Search for privileged users.
   */
  @RequestMapping(value = "/users/findUserWithRole", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.PUBLISHER})
  public ResponseEntity<List<UserDto>> findUserWithRole(
      @RequestParam String login,
      @RequestParam String role
  ) {
    try {
      return ResponseEntity.ok()
          .cacheControl(CacheControl.noCache().mustRevalidate()).body(
              userApiService.findAllByLoginLikeOrEmailLikeAndByAuthoritiesContaining(
                  login,
                  login,
                  role
              )
          );
    } catch (InvalidUserApiResponseException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
