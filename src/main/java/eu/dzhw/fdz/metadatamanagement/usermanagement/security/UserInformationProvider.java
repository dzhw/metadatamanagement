package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import javax.validation.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.authmanagement.domain.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidResponseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Provides login name and role checks of the currently authenticated user.
 */
@Service
@RequiredArgsConstructor
public class UserInformationProvider {

  private final UserApiService userApiService;

  /**
   * Retrieve the username of the currently authenticated user.
   */
  public String getUserLogin() {
    return SecurityUtils.getCurrentUserLogin();
  }

  /**
   * Checks if the currently authenticated user has the provided role.
   */
  public boolean isUserInRole(@NotEmpty String role) {
    return SecurityUtils.isUserInRole(role);
  }

  /**
   * Checks if the currently authenticated user has been authenticated anonymously.
   */
  public boolean isUserAnonymous() {
    return SecurityUtils.isUserAnonymous();
  }

  /**
   * Switches the security context to the given user or logs the current user out.
   *
   * @param login the username to login, if null the security context will be cleared
   * @return the user information of the logged in user
   */
  public UserDto switchToUser(String login) {
    if (StringUtils.isEmpty(login)) {
      SecurityContextHolder.getContext().setAuthentication(null);
      return null;
    }
    Optional<UserDto> user;
    try {
      user = userApiService.findOneByLogin(login);
    } catch (InvalidResponseException e) {
      throw new IllegalArgumentException("Could not find user " + login);
    }
    if (user.isPresent()) {
      var userInstance = user.get();
      // switch to on behalf user for correct modification names
      /*Set<GrantedAuthority> grantedAuthorities = userInstance.getAuthorities().stream()
          .map(authority -> new SimpleGrantedAuthority(authority))
          .collect(Collectors.toSet());
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userInstance.getLogin(), userInstance.getPassword(), grantedAuthorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);*/
      return userInstance;
    } else {
      throw new IllegalArgumentException("User not found: " + login);
    }
  }
}
