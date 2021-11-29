package eu.dzhw.fdz.metadatamanagement.authmanagement.config;

import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Primary
@Component
public class MockJwtDecoder implements JwtDecoder {

  @Override
  public Jwt decode(String token) throws JwtException {
    return Jwt.withTokenValue("test").header("alg", "rs256").build();
  }
}
