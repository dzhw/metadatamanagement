package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import static java.util.Objects.nonNull;
import static org.springframework.security.oauth2.common.util.SerializationUtils.deserialize;
import static org.springframework.security.oauth2.common.util.SerializationUtils.serialize;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthenticationAccessToken;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthenticationRefreshToken;

/**
 * A MongoDB implementation of the TokenStore.
 */
public class MongoDbTokenStore implements TokenStore {

  private final OAuth2AccessTokenRepository oauth2AccessTokenRepository;

  private final OAuth2RefreshTokenRepository oauth2RefreshTokenRepository;

  private AuthenticationKeyGenerator authenticationKeyGenerator;

  /**
   * Create the token store.
   * 
   * @param oauth2AccessTokenRepository the repo holding the access tokens
   * @param oauth2RefreshTokenRepository the repo holding the refresh tokens
   * @param authenticationKeyGenerator the authentication key generator
   */
  public MongoDbTokenStore(final OAuth2AccessTokenRepository oauth2AccessTokenRepository,
      final OAuth2RefreshTokenRepository oauth2RefreshTokenRepository,
      final AuthenticationKeyGenerator authenticationKeyGenerator) {
    this.oauth2AccessTokenRepository = oauth2AccessTokenRepository;
    this.oauth2RefreshTokenRepository = oauth2RefreshTokenRepository;
    this.authenticationKeyGenerator = authenticationKeyGenerator;
  }

  @Override
  public OAuth2Authentication readAuthentication(final OAuth2AccessToken token) {
    return readAuthentication(token.getValue());
  }

  @Override
  public OAuth2Authentication readAuthentication(final String token) {
    final String tokenId = extractTokenKey(token);

    final OAuth2AuthenticationAccessToken mongoOAuth2AccessToken =
        oauth2AccessTokenRepository.findByTokenId(tokenId);

    if (nonNull(mongoOAuth2AccessToken)) {
      try {
        return deserializeAuthentication(mongoOAuth2AccessToken.getAuthentication());
      } catch (IllegalArgumentException e) {
        removeAccessToken(token);
      }
    }

    return null;
  }

  @Override
  public void storeAccessToken(final OAuth2AccessToken token,
      final OAuth2Authentication authentication) {
    String refreshToken = null;
    if (nonNull(token.getRefreshToken())) {
      refreshToken = token.getRefreshToken().getValue();
    }

    if (nonNull(readAccessToken(token.getValue()))) {
      removeAccessToken(token.getValue());
    }

    final String tokenKey = extractTokenKey(token.getValue());

    final OAuth2AuthenticationAccessToken oAuth2AccessToken = new OAuth2AuthenticationAccessToken(
        tokenKey,
        serializeAccessToken(token), authenticationKeyGenerator.extractKey(authentication),
        authentication.isClientOnly() ? null : authentication.getName(),
        authentication.getOAuth2Request().getClientId(), serializeAuthentication(authentication),
        extractTokenKey(refreshToken));

    oauth2AccessTokenRepository.save(oAuth2AccessToken);
  }

  @Override
  public OAuth2AccessToken readAccessToken(final String tokenValue) {
    final String tokenKey = extractTokenKey(tokenValue);
    final OAuth2AuthenticationAccessToken mongoOAuth2AccessToken =
        oauth2AccessTokenRepository.findByTokenId(tokenKey);
    if (nonNull(mongoOAuth2AccessToken)) {
      try {
        return deserializeAccessToken(mongoOAuth2AccessToken.getToken());
      } catch (IllegalArgumentException e) {
        removeAccessToken(tokenValue);
      }
    }
    return null;
  }

  @Override
  public void removeAccessToken(final OAuth2AccessToken token) {
    removeAccessToken(token.getValue());
  }
  
  private void removeAccessToken(final String tokenValue) {
    final String tokenKey = extractTokenKey(tokenValue);
    oauth2AccessTokenRepository.deleteByTokenId(tokenKey);
  }

  @Override
  public void storeRefreshToken(final OAuth2RefreshToken refreshToken,
      final OAuth2Authentication oauth2Authentication) {
    final String tokenKey = extractTokenKey(refreshToken.getValue());
    final byte[] token = serializeRefreshToken(refreshToken);
    final byte[] authentication = serializeAuthentication(oauth2Authentication);

    final OAuth2AuthenticationRefreshToken oAuth2RefreshToken =
        new OAuth2AuthenticationRefreshToken(tokenKey, token, authentication);

    oauth2RefreshTokenRepository.save(oAuth2RefreshToken);
  }

  @Override
  public OAuth2RefreshToken readRefreshToken(final String tokenValue) {
    final String tokenKey = extractTokenKey(tokenValue);
    final OAuth2AuthenticationRefreshToken mongoOAuth2RefreshToken =
        oauth2RefreshTokenRepository.findByTokenId(tokenKey);

    if (nonNull(mongoOAuth2RefreshToken)) {
      try {
        return deserializeRefreshToken(mongoOAuth2RefreshToken.getToken());
      } catch (IllegalArgumentException e) {
        removeRefreshToken(tokenValue);
      }
    }

    return null;
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(final OAuth2RefreshToken token) {
    return readAuthenticationForRefreshToken(token.getValue());
  }
  
  /**
   * Read the refresh token.
   * 
   * @param value a refresh tokens value
   * @return the authentication originally used to grant the refresh token
   */
  public OAuth2Authentication readAuthenticationForRefreshToken(final String value) {
    final String tokenId = extractTokenKey(value);

    final OAuth2AuthenticationRefreshToken mongoOAuth2RefreshToken =
        oauth2RefreshTokenRepository.findByTokenId(tokenId);

    if (nonNull(mongoOAuth2RefreshToken)) {
      try {
        return deserializeAuthentication(mongoOAuth2RefreshToken.getAuthentication());
      } catch (IllegalArgumentException e) {
        removeRefreshToken(value);
      }
    }

    return null;
  }

  @Override
  public void removeRefreshToken(final OAuth2RefreshToken token) {
    removeRefreshToken(token.getValue());
  }
  
  private void removeRefreshToken(final String token) {
    final String tokenId = extractTokenKey(token);
    oauth2RefreshTokenRepository.deleteByTokenId(tokenId);
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(final OAuth2RefreshToken refreshToken) {
    removeAccessTokenUsingRefreshToken(refreshToken.getValue());
  }
  
  private void removeAccessTokenUsingRefreshToken(final String refreshToken) {
    final String tokenId = extractTokenKey(refreshToken);
    oauth2AccessTokenRepository.deleteByRefreshTokenId(tokenId);
  }

  @Override
  public OAuth2AccessToken getAccessToken(final OAuth2Authentication authentication) {
    OAuth2AccessToken accessToken = null;

    String key = authenticationKeyGenerator.extractKey(authentication);

    final OAuth2AuthenticationAccessToken oAuth2AccessToken =
        oauth2AccessTokenRepository.findByAuthenticationId(key);

    if (oAuth2AccessToken != null) {
      accessToken = deserializeAccessToken(oAuth2AccessToken.getToken());
    }

    if (accessToken != null && !key.equals(
        authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
      removeAccessToken(accessToken.getValue());
      // Keep the store consistent (maybe the same user is represented by this authentication but
      // the details have
      // changed)
      storeAccessToken(accessToken, authentication);
    }
    return accessToken;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId,
      String userName) {
    final List<OAuth2AuthenticationAccessToken> oAuth2AccessTokens =
        oauth2AccessTokenRepository.findByUsernameAndClientId(userName, clientId);
    return transformToOAuth2AccessTokens(oAuth2AccessTokens);
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(final String clientId) {
    final List<OAuth2AuthenticationAccessToken> oAuth2AccessTokens =
        oauth2AccessTokenRepository.findByClientId(clientId);
    return transformToOAuth2AccessTokens(oAuth2AccessTokens);
  }
  
  /**
   * Remove access and refresh tokens of the given user.
   * @param username The login of the user.
   */
  public void removeTokensByUsername(String username) {
    oauth2AccessTokenRepository.findByUsername(username).stream().forEach(token -> {
      oauth2RefreshTokenRepository.deleteByTokenId(token.getRefreshToken());
      oauth2AccessTokenRepository.delete(token);
    });
  }

  protected String extractTokenKey(final String value) {
    if (Objects.isNull(value)) {
      return null;
    }
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(
          "MD5 algorithm not available.  Fatal (should be in the JDK).");
    }

    try {
      byte[] bytes = digest.digest(value.getBytes("UTF-8"));
      return String.format("%032x", new BigInteger(1, bytes));
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(
          "UTF-8 encoding not available.  Fatal (should be in the JDK).");
    }
  }

  protected byte[] serializeAccessToken(final OAuth2AccessToken token) {
    return serialize(token);
  }

  protected byte[] serializeRefreshToken(final OAuth2RefreshToken token) {
    return serialize(token);
  }

  protected byte[] serializeAuthentication(final OAuth2Authentication authentication) {
    return serialize(authentication);
  }

  protected OAuth2AccessToken deserializeAccessToken(final byte[] token) {
    return deserialize(token);
  }

  protected OAuth2RefreshToken deserializeRefreshToken(final byte[] token) {
    return deserialize(token);
  }

  protected OAuth2Authentication deserializeAuthentication(final byte[] authentication) {
    return deserialize(authentication);
  }

  private Collection<OAuth2AccessToken> transformToOAuth2AccessTokens(
      final List<OAuth2AuthenticationAccessToken> oauth2AccessTokens) {
    return oauth2AccessTokens.stream().filter(Objects::nonNull)
        .map(token -> SerializationUtils.<OAuth2AccessToken>deserialize(token.getToken()))
        .collect(Collectors.toList());
  }
}
