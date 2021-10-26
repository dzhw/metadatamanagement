package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.authmanagement.domain.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A Service which will fetch user related info from a specified Authentication Server.
 *
 * @author Jan Schwoerer
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthUserService {

  /**
   * TODO implement logic
   *
   * TODO For now usermanagement.domain.User will be used. An authmanagement version of this
   * class will be created once the usermanagement User can be removed without compile issues.
   *
   * @param authority an authority which will be used to search for all users which have said
   *                  authority
   * @return A list of Users which have the provided authority
   */
  public List<AuthUser> findAllByAuthoritiesContaining(String authority) {
    throw new IllegalStateException("Not implemented!");
  }

  /**
   * TODO implement logic and add documentation
   *
   * @param logins the logins used to search for Users
   * @return all users that have a "login" in the logins parameter
   */
  public List<AuthUser> findAllByLoginIn(Set<String> logins) {
    throw new IllegalStateException("Not implemented!");
  }

  /**
   * TODO implement logic and add documentation
   *
   * TODO For now usermanagement.domain.User will be used. An authmanagement version of this
   * class will be created once the usermanagement User can be removed without compile issues.
   *
   * @param login a login which should be associated to a User
   * @return the user with the provided login
   */
  public Optional<AuthUser> findOneByLogin(String login) {
    throw new IllegalStateException("Not implemented!");
  }
}
