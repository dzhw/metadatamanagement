package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AuthUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.common.rest.util.PaginationUtil;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.MongoDbTokenStore;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto.ManagedUserDto;
import eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
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

  private final UserRepository userRepository;

  private final MongoDbTokenStore tokenStore;

  private final AuthUserService authUserService;

  /**
   * Get all users.
   */
  @RequestMapping(value = "/users", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured(AuthoritiesConstants.ADMIN)
  public ResponseEntity<List<ManagedUserDto>> getAllUsers(Pageable pageable)
      throws URISyntaxException {
    var page = userRepository.findAll(pageable);
    List<ManagedUserDto> managedUserDtos = page.getContent().stream()
        .map(user -> new ManagedUserDto(user)).collect(Collectors.toList());
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
    headers.setCacheControl(CacheControl.noStore());
    return new ResponseEntity<>(managedUserDtos, headers, HttpStatus.OK);
  }

  /**
   * Get the "login" user.
   */
  @RequestMapping(value = "/users/{login}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured(AuthoritiesConstants.ADMIN)
  public ResponseEntity<ManagedUserDto> getUser(@PathVariable String login) {
    log.debug("REST request to get User : {}", login);
    return authUserService.findOneByLogin(login).map(user -> new ManagedUserDto(user))
        .map(managedUserDTO -> ResponseEntity.ok().cacheControl(CacheControl.noCache())
            .body(managedUserDTO))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Get the "login" user with less details.
   */
  @RequestMapping(value = "/users/{login}/public", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.PUBLISHER})
  public ResponseEntity<UserDto> getUserPublic(@PathVariable String login) {
    log.debug("REST request to get User : {}", login);
    return authUserService.findOneByLogin(login).map(user -> new UserDto(user))
        .map(userDTO -> ResponseEntity.ok().cacheControl(CacheControl.noCache().mustRevalidate())
            .body(userDTO))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }


  /**
   * Search for privileged users.
   */
  @RequestMapping(value = "/users/findUserWithRole", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.PUBLISHER})
  public ResponseEntity<List<UserDto>> findUserWithRole(@RequestParam String login,
      @RequestParam String role) {
    return new ResponseEntity<>(authUserService.findAllByLoginLikeOrEmailLike(login, login).stream()
        .filter(user -> SecurityUtils.isUserInRole(role, user)).map(user -> new UserDto(user))
        .collect(Collectors.toList()), HttpStatus.OK);
  }

}
