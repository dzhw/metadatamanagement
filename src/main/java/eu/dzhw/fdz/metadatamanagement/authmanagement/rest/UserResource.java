package eu.dzhw.fdz.metadatamanagement.authmanagement.rest;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto.UserWithRolesDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
   * Set the value of the deactivated welcome dialog flag for the
   * currently logged in user.
   *
   * @param deactivatedWelcomeDialog the value to which the deactivated welcome dialog will be set
   * @return A HTTP response
   */
  @PatchMapping(value = "/users/deactivatedWelcomeDialog")
  @Secured({
      AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.PUBLISHER
      })
  public ResponseEntity<Void> patchDeactivatedWelcomeDialog(
      @RequestParam boolean deactivatedWelcomeDialog
  ) {
    var login = SecurityUtils.getCurrentUserLogin();
    log.debug("REST request to set the shown status of the user: {}", login);

    userApiService.patchDeactivatedWelcomeDialogById(login, deactivatedWelcomeDialog);

    return ResponseEntity.ok().build();
  }

  /**
   * Get the "login" user with less details.
   */
  @GetMapping(value = "/users/{login}/public", produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured({
      AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.PUBLISHER
      })
  public ResponseEntity<UserWithRolesDto> getUserPublic(@PathVariable String login) {
    log.debug("REST request to get User (with roles) : {}", login);
    var user = userApiService.findOneWithAuthoritiesByLogin(login);
    return user.map(userDto -> ResponseEntity.ok()
        .cacheControl(CacheControl.noCache())
        .body(
            userDto
        )).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Search for privileged users.
   */
  @GetMapping(value = "/users/findUserWithRole", produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured({
      AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.PUBLISHER
      })
  public ResponseEntity<List<UserDto>> findUserWithRole(
      @RequestParam String login,
      @RequestParam String role
  ) {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noCache().mustRevalidate()).body(
            userApiService.findAllByLoginLikeOrEmailLikeAndByAuthoritiesContaining(
                login,
                login,
                role
            )
        );
  }
}
