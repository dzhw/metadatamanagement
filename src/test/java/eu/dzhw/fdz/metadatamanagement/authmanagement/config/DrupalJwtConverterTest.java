package eu.dzhw.fdz.metadatamanagement.authmanagement.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DrupalJwtConverterTest extends AbstractTest {

  @Autowired
  private DrupalJwtConverter jwtConverter;

  @Test
  public void tokenJwtDrupalJwtConverterTest() throws Exception {
    var rsaKey = new RSAKeyGenerator(2048)
        .keyID(UUID.randomUUID().toString())
        .generate();

    var signer = new RSASSASigner(rsaKey);

    var claimsSet = new JWTClaimsSet.Builder()
        .audience(UUID.randomUUID().toString())
        .subject("1")
        .issueTime(new Date())
        .notBeforeTime(new Date())
        .expirationTime(Date.from(Instant.now().plusSeconds(60)))
        .claim("scope", List.of("admin", "user"))
        .build();

    var signedJWT = new SignedJWT(
      new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
      claimsSet
    );

    signedJWT.sign(signer);

    var decoder = NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();

    var jwt = decoder.decode(signedJWT.serialize());

    var authenticationToken = jwtConverter.convert(jwt);

    assertTrue(authenticationToken.isAuthenticated());
    assertTrue(
      authenticationToken.getAuthorities().contains(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
    );
    assertTrue(
      authenticationToken.getAuthorities().contains(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN))
    );
  }

  @Test
  public void SpringJwtDrupalJwtConverterTest() {
    var jwt = Jwt.withTokenValue("test token")
        .header("alg", "RS256")
        .header("typ", "JWT")
        .subject("1")
        .expiresAt(Instant.now().plusSeconds(60))
        .claim("scope", List.of("admin", "user"))
        .build();

    var authenticationToken = jwtConverter.convert(jwt);

    assertTrue(authenticationToken.isAuthenticated());
    assertTrue(
        authenticationToken.getAuthorities().contains(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
    );
    assertTrue(
      authenticationToken.getAuthorities().contains(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN))
    );
  }
}