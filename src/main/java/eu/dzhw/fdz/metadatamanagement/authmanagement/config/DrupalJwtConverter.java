package eu.dzhw.fdz.metadatamanagement.authmanagement.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * An extension to the {@link JwtAuthenticationConverter} which changes
 * the authorities' converter to a custom converter.
 */
@Component
public final class DrupalJwtConverter extends JwtAuthenticationConverter {

  /**
   * Construct the Converter and set the {@link GrantedAuthority} Converter to a custom converter.
   */
  public DrupalJwtConverter() {
    super.setJwtGrantedAuthoritiesConverter(new DrupalJwtGrantedAuthoritiesConverter());
  }

  /**
   * A custom {@link GrantedAuthority} converter which specifically handles a JWT from the
   * Drupal identity provider.
   */
  private static final class DrupalJwtGrantedAuthoritiesConverter
      implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String PREFIX = "ROLE_";

    private static final String CLAIM_NAME = "scope";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
      return getAuthorities(jwt)
          .stream()
          .map(
              authority -> new SimpleGrantedAuthority(PREFIX + authority.toUpperCase(Locale.GERMAN))
          )
          .collect(Collectors.toList());
    }

    private List<String> getAuthorities(Jwt jwt) {
      var authorities = jwt.getClaimAsStringList(CLAIM_NAME);

      return authorities != null ? authorities : List.of();
    }
  }
}
