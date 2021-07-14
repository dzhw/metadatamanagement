package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * Implementation for {@link UserInformationProvider}.
 */
@Service
@RequiredArgsConstructor
class UserInformationProviderImpl implements UserInformationProvider {
  private final UserService userService;
  
  @Override
  public String getUserLogin() {
    return SecurityUtils.getCurrentUserLogin();
  }

  @Override
  public boolean isUserInRole(@NotEmpty String role) {
    return SecurityUtils.isUserInRole(role);
  }

  @Override
  public boolean isUserAnonymous() {
    return SecurityUtils.isUserAnonymous();
  }

  @Override
  public User switchToUser(String login) {
    if (StringUtils.isEmpty(login)) {
      SecurityContextHolder.getContext().setAuthentication(null);
      return null;
    }
    Optional<User> user = userService.getUserWithAuthoritiesByLogin(login);
    if (user.isPresent()) {
      User userInstance = user.get();
      // switch to on behalf user for correct modification names
      Set<GrantedAuthority> grantedAuthorities = userInstance.getAuthorities().stream()
          .map(authority -> new SimpleGrantedAuthority(authority.getName()))
          .collect(Collectors.toSet());
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userInstance.getLogin(), userInstance.getPassword(), grantedAuthorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return userInstance;
    } else {
      throw new IllegalArgumentException("User not found: " + login);
    }
  }
}
