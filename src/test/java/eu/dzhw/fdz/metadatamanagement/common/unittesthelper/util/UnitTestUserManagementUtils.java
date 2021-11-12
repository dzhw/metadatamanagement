package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * This class has helper / utils methods for unit tests.
 *
 * @author Daniel Katzberg
 *
 */
@Slf4j
public class UnitTestUserManagementUtils<T> {

  /**
   *
   *
   * @param login
   */
  public static void generateJwt(String login, final String... roles) {
    var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(
        new JwtAuthenticationToken(
            Jwt.withTokenValue("test").header("alg", "RS256").subject(login).build(),
            Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        )
    );
    SecurityContextHolder.setContext(context);
  }
}
