package eu.dzhw.fdz.metadatamanagement.config.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import eu.dzhw.fdz.metadatamanagement.domain.OAuth2AuthenticationAccessToken;
import eu.dzhw.fdz.metadatamanagement.domain.OAuth2AuthenticationRefreshToken;
import eu.dzhw.fdz.metadatamanagement.repository.OAuth2AccessTokenRepository;
import eu.dzhw.fdz.metadatamanagement.repository.OAuth2RefreshTokenRepository;

/**
 * A MongoDB implementation of the TokenStore.
 */
public class MongoDbTokenStore implements TokenStore {

  private final OAuth2AccessTokenRepository oauth2AccessTokenRepository;

  private final OAuth2RefreshTokenRepository oauth2RefreshTokenRepository;

  private AuthenticationKeyGenerator authenticationKeyGenerator =
      new DefaultAuthenticationKeyGenerator();

  public MongoDbTokenStore(final OAuth2AccessTokenRepository oauth2AccessTokenRepository,
      final OAuth2RefreshTokenRepository oauth2RefreshTokenRepository) {
    this.oauth2AccessTokenRepository = oauth2AccessTokenRepository;
    this.oauth2RefreshTokenRepository = oauth2RefreshTokenRepository;
  }

  @Override
  public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
    return readAuthentication(token.getValue());
  }

  @Override
  public OAuth2Authentication readAuthentication(String tokenId) {
    return oauth2AccessTokenRepository.findByTokenId(tokenId)
      .getAuthentication();
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
    OAuth2AuthenticationAccessToken oauth2AuthenticationAccessToken =
        new OAuth2AuthenticationAccessToken(token, authentication,
            authenticationKeyGenerator.extractKey(authentication));
    oauth2AccessTokenRepository.save(oauth2AuthenticationAccessToken);
  }

  @Override
  public OAuth2AccessToken readAccessToken(String tokenValue) {
    OAuth2AuthenticationAccessToken token = oauth2AccessTokenRepository.findByTokenId(tokenValue);
    if (token == null) {
      return null;
    }
    return token.getoAuth2AccessToken();
  }

  @Override
  public void removeAccessToken(OAuth2AccessToken token) {
    OAuth2AuthenticationAccessToken accessToken =
        oauth2AccessTokenRepository.findByTokenId(token.getValue());
    if (accessToken != null) {
      oauth2AccessTokenRepository.delete(accessToken);
    }
  }

  @Override
  public void storeRefreshToken(OAuth2RefreshToken refreshToken,
      OAuth2Authentication authentication) {
    oauth2RefreshTokenRepository
      .save(new OAuth2AuthenticationRefreshToken(refreshToken, authentication));
  }

  @Override
  public OAuth2RefreshToken readRefreshToken(String tokenValue) {
    return oauth2RefreshTokenRepository.findByTokenId(tokenValue)
      .getoAuth2RefreshToken();
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
    return oauth2RefreshTokenRepository.findByTokenId(token.getValue())
      .getAuthentication();
  }

  @Override
  public void removeRefreshToken(OAuth2RefreshToken token) {
    oauth2RefreshTokenRepository
      .delete(oauth2RefreshTokenRepository.findByTokenId(token.getValue()));
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
    oauth2AccessTokenRepository
      .delete(oauth2AccessTokenRepository.findByRefreshToken(refreshToken.getValue()));
  }

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
    OAuth2AuthenticationAccessToken token = oauth2AccessTokenRepository
        .findByAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
    return token == null ? null : token.getoAuth2AccessToken();
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
    List<OAuth2AuthenticationAccessToken> tokens =
        oauth2AccessTokenRepository.findByClientId(clientId);
    return extractAccessTokens(tokens);
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId,
      String userName) {
    List<OAuth2AuthenticationAccessToken> tokens =
        oauth2AccessTokenRepository.findByClientIdAndUserName(clientId, userName);
    return extractAccessTokens(tokens);
  }

  private Collection<OAuth2AccessToken> extractAccessTokens(
      List<OAuth2AuthenticationAccessToken> tokens) {
    List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
    for (OAuth2AuthenticationAccessToken token : tokens) {
      accessTokens.add(token.getoAuth2AccessToken());
    }
    return accessTokens;
  }
}
