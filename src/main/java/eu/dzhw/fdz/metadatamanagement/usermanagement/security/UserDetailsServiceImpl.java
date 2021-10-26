package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.Set;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AuthUserService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final AuthUserService authUserService;

  @Override
  public UserDetails loadUserByUsername(final String login) {
    log.debug("Authenticating {}", login);
    String lowercaseLogin = login.toLowerCase(LocaleContextHolder.getLocale());
    var userFromDatabase = authUserService.findOneByLoginOrEmail(lowercaseLogin, login);
    return userFromDatabase.map(user -> {
      if (!user.isActivated()) {
        throw new UserNotActivatedException("User " + login + " was not activated");
      }
      Set<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
          .map(authority -> new SimpleGrantedAuthority(authority))
          .collect(Collectors.toSet());
      return new CustomUserDetails("1", user.getLogin(), user.getPassword(),
          grantedAuthorities, true, true, true, true);
    }).orElseThrow(() -> new UsernameNotFoundException(
        "User " + lowercaseLogin + " was not found in the " + "database"));
  }
}
