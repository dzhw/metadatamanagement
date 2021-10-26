package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.authmanagement.domain.AuthUser;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AuthUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Implementation for {@link UserInformationProvider}.
 */
@Service
@RequiredArgsConstructor
class UserInformationProviderImpl implements UserInformationProvider {

  private final AuthUserService authUserService;

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
  public AuthUser switchToUser(String login) {
    if (StringUtils.isEmpty(login)) {
      SecurityContextHolder.getContext().setAuthentication(null);
      return null;
    }
    var user = authUserService.findOneByLogin(login);
    if (user.isPresent()) {
      var userInstance = new AuthUser(user.get());
      // switch to on behalf user for correct modification names
      Set<GrantedAuthority> grantedAuthorities = userInstance.getAuthorities().stream()
          .map(authority -> new SimpleGrantedAuthority(authority))
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
