package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthenticationRefreshToken;

/**
 * Repository for {@link MongoDbTokenStore}.
 * 
 * @author René Reitmann
 */
public interface OAuth2RefreshTokenRepositoryCustom {
  OAuth2AuthenticationRefreshToken findByTokenId(String tokenId);

  boolean deleteByTokenId(String tokenId);
}
