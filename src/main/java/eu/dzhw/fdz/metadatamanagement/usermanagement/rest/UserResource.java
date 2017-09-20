package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.common.rest.util.PaginationUtil;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.AuthorityRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto.ManagedUserDto;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.UserService;

/**
 * REST controller for managing users.
 *
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * </p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and
 * Authority, and send everything to the client side: there would be no DTO, a lot less code, and an
 * outer-join which would be good for performance.
 * </p>
 * <p>
 * We use a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all the
 * time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not a real issue as we
 * have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer
 * join (which will get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * </p>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 * </p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

  private final Logger log = LoggerFactory.getLogger(UserResource.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authorityRepository;

  @Autowired
  private UserService userService;

  /**
   * PUT /users -> Updates an existing User.
   */
  @RequestMapping(value = "/users", method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.ADMIN)
  public ResponseEntity<ManagedUserDto> updateUser(@RequestBody ManagedUserDto managedUserDto)
      throws URISyntaxException {
    log.debug("REST request to update User : {}", managedUserDto);
    return Optional.of(userRepository.findOne(managedUserDto.getId()))
      .map(user -> {
        user.setLogin(managedUserDto.getLogin());
        user.setFirstName(managedUserDto.getFirstName());
        user.setLastName(managedUserDto.getLastName());
        user.setEmail(managedUserDto.getEmail());
        user.setActivated(managedUserDto.isActivated());
        user.setLangKey(managedUserDto.getLangKey());
        Set<Authority> authorities = user.getAuthorities();
        authorities.clear();
        managedUserDto.getAuthorities()
          .stream()
          .forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
        userRepository.save(user);
        return ResponseEntity.ok()
          .body(new ManagedUserDto(userRepository.findOne(managedUserDto.getId())));
      })
      .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * GET /users -> get all users.
   */
  @RequestMapping(value = "/users", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.ADMIN)
  public ResponseEntity<List<ManagedUserDto>> getAllUsers(Pageable pageable)
      throws URISyntaxException {
    Page<User> page = userRepository.findAll(pageable);
    List<ManagedUserDto> managedUserDtos = page.getContent()
        .stream()
        .map(user -> new ManagedUserDto(user))
        .collect(Collectors.toList());
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
    return new ResponseEntity<>(managedUserDtos, headers, HttpStatus.OK);
  }

  /**
   * GET /users/:login -> get the "login" user.
   */
  @RequestMapping(value = "/users/{login}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.ADMIN)
  public ResponseEntity<ManagedUserDto> getUser(@PathVariable String login) {
    log.debug("REST request to get User : {}", login);
    return userService.getUserWithAuthoritiesByLogin(login)
      .map(user -> new ManagedUserDto(user))
      .map(managedUserDTO -> new ResponseEntity<>(managedUserDTO, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
