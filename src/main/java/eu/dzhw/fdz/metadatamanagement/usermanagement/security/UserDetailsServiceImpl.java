package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(final String login) {
    log.debug("Authenticating {}", login);
    String lowercaseLogin = login.toLowerCase(LocaleContextHolder.getLocale());
    Optional<User> userFromDatabase = userRepository.findOneByLogin(lowercaseLogin);
    return userFromDatabase.map(user -> {
      if (!user.isActivated()) {
        throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
      }
      Set<GrantedAuthority> grantedAuthorities = user.getAuthorities()
          .stream()
          .map(authority -> new SimpleGrantedAuthority(authority.getName()))
          .collect(Collectors.toSet());
      return new CustomUserDetails(user.getId(), lowercaseLogin, user.getPassword(),
          grantedAuthorities, true, true, true, true);
    })
      .orElseThrow(() -> new UsernameNotFoundException(
          "User " + lowercaseLogin + " was not found in the " + "database"));
  }
}
