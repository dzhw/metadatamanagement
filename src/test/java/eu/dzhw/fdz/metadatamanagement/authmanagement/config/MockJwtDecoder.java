package eu.dzhw.fdz.metadatamanagement.authmanagement.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class MockJwtDecoder implements JwtDecoder {
  private final RSAKey key;
  private final RSASSASigner signer;

  private JwtDecoder jwtDecoder;

  public MockJwtDecoder() throws JOSEException {
    key = new RSAKeyGenerator(2048).keyID("test").generate();
    signer = new RSASSASigner(key);

    this.jwtDecoder = NimbusJwtDecoder.withPublicKey(key.toRSAPublicKey()).build();
  }

  @Override
  public Jwt decode(String token) throws JwtException {
    return this.jwtDecoder.decode(token);
  }

  public String generateTokenForUser(User user) throws JOSEException {
    var claims = new JWTClaimsSet.Builder()
        .subject(user.getName())
        .issuer("http://localhost:8082")
        .issueTime(Date.from(Instant.now()))
        .expirationTime(Date.from(Instant.now().plusSeconds(5)))
        .notBeforeTime(Date.from(Instant.now()))
        .claim("scope", user.getRoles())
        .build();

    var jwt = new SignedJWT(
        new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(key.getKeyID()).build(),
        claims
    );

    jwt.sign(signer);

    return jwt.serialize();
  }
}
