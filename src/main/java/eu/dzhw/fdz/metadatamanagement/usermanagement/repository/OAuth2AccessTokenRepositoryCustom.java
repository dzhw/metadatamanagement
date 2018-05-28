package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthenticationAccessToken;

/**
 * Repository for {@link MongoDbTokenStore}.
 * 
 * @author René Reitmann
 */
public interface OAuth2AccessTokenRepositoryCustom {
  OAuth2AuthenticationAccessToken findByTokenId(String tokenId);
  
  boolean deleteByTokenId(String tokenId);

  boolean deleteByRefreshTokenId(String refreshTokenId);

  OAuth2AuthenticationAccessToken findByAuthenticationId(String key);

  List<OAuth2AuthenticationAccessToken> findByUsernameAndClientId(String username, String clientId);

  List<OAuth2AuthenticationAccessToken> findByClientId(String clientId);
}
