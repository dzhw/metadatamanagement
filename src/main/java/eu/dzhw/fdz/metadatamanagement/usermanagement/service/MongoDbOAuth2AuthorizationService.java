package eu.dzhw.fdz.metadatamanagement.usermanagement.service;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthorizationDocument;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.OAuth2AuthorizationRepository;

import org.springframework.stereotype.Service;

@Service
public class MongoDbOAuth2AuthorizationService implements OAuth2AuthorizationService {

  private final OAuth2AuthorizationRepository repository;
  private final RegisteredClientRepository registeredClientRepository;

  public MongoDbOAuth2AuthorizationService(OAuth2AuthorizationRepository repository,
      RegisteredClientRepository registeredClientRepository) {
    this.repository = repository;
    this.registeredClientRepository = registeredClientRepository;
  }

  @Override
  public void save(OAuth2Authorization authorization) {
    repository.save(toDocument(authorization));
  }

  @Override
  public void remove(OAuth2Authorization authorization) {
    repository.deleteById(authorization.getId());
  }

  @Override
  public OAuth2Authorization findById(String id) {
    return repository.findById(id).map(this::toAuthorization).orElse(null);
  }

  @Override
  public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
    Optional<OAuth2AuthorizationDocument> doc;
    if (tokenType == null) {
      doc = repository.findByAccessTokenValue(token)
          .or(() -> repository.findByRefreshTokenValue(token))
          .or(() -> repository.findByState(token));
    } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
      doc = repository.findByAccessTokenValue(token);
    } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
      doc = repository.findByRefreshTokenValue(token);
    } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
      doc = repository.findByState(token);
    } else {
      doc = Optional.empty();
    }
    return doc.map(this::toAuthorization).orElse(null);
  }

  // replaces removeTokensByUsername — called from UserResource
  public void removeByPrincipalName(String principalName) {
    repository.deleteByPrincipalName(principalName);
  }

  private OAuth2AuthorizationDocument toDocument(OAuth2Authorization authorization) {
    OAuth2AuthorizationDocument doc = new OAuth2AuthorizationDocument();
    doc.setId(authorization.getId());
    doc.setRegisteredClientId(authorization.getRegisteredClientId());
    doc.setPrincipalName(authorization.getPrincipalName());
    doc.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
    doc.setAuthorizedScopes(authorization.getAuthorizedScopes());
    Authentication principal = authorization.getAttribute(Principal.class.getName());
    if (principal != null) {
      Set<String> principalAuthorities = new HashSet<>();
      for (GrantedAuthority authority : principal.getAuthorities()) {
        if (authority != null && authority.getAuthority() != null) {
          principalAuthorities.add(authority.getAuthority());
        }
      }
      doc.setPrincipalAuthorities(principalAuthorities);
    }
    Map<String, Object> attributes = new HashMap<>(authorization.getAttributes());
    attributes.remove(Principal.class.getName());
    doc.setAttributes(attributes);
    doc.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

    OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
    if (accessToken != null) {
      doc.setAccessTokenValue(accessToken.getToken().getTokenValue());
      doc.setAccessTokenIssuedAt(accessToken.getToken().getIssuedAt());
      doc.setAccessTokenExpiresAt(accessToken.getToken().getExpiresAt());
      doc.setAccessTokenInvalidated(accessToken.isInvalidated());
    }

    OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);
    if (refreshToken != null) {
      doc.setRefreshTokenValue(refreshToken.getToken().getTokenValue());
      doc.setRefreshTokenIssuedAt(refreshToken.getToken().getIssuedAt());
      doc.setRefreshTokenExpiresAt(refreshToken.getToken().getExpiresAt());
      doc.setRefreshTokenInvalidated(refreshToken.isInvalidated());
    }
    return doc;
  }

  private OAuth2Authorization toAuthorization(OAuth2AuthorizationDocument doc) {
    RegisteredClient registeredClient = registeredClientRepository.findById(doc.getRegisteredClientId());
    OAuth2Authorization.Builder builder = OAuth2Authorization
        .withRegisteredClient(registeredClient)
        .id(doc.getId())
        .principalName(doc.getPrincipalName())
        .authorizationGrantType(new AuthorizationGrantType(doc.getAuthorizationGrantType()))
        .authorizedScopes(doc.getAuthorizedScopes() == null ? Set.of() : doc.getAuthorizedScopes())
        .attributes(attrs -> attrs.putAll(doc.getAttributes()));

    Set<GrantedAuthority> authorities = doc.getPrincipalAuthorities() == null ? Set.of()
        : doc.getPrincipalAuthorities().stream()
            .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
            .collect(Collectors.toCollection(HashSet::new));
    builder.attribute(Principal.class.getName(), UsernamePasswordAuthenticationToken.authenticated(
        doc.getPrincipalName(), null, authorities));

    if (doc.getAccessTokenValue() != null) {
      OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
          doc.getAccessTokenValue(), doc.getAccessTokenIssuedAt(), doc.getAccessTokenExpiresAt(),
          doc.getAuthorizedScopes() == null ? Set.of() : doc.getAuthorizedScopes());
      builder.token(accessToken, metadata -> {
        if (doc.isAccessTokenInvalidated()) {
          metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true);
        }
      });
    }
    if (doc.getRefreshTokenValue() != null) {
      OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
          doc.getRefreshTokenValue(), doc.getRefreshTokenIssuedAt(), doc.getRefreshTokenExpiresAt());
      builder.token(refreshToken, metadata -> {
        if (doc.isRefreshTokenInvalidated()) {
          metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true);
        }
      });
    }
    return builder.build();
  }
}

