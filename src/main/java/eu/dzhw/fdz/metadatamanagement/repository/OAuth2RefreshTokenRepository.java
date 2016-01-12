package eu.dzhw.fdz.metadatamanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import eu.dzhw.fdz.metadatamanagement.domain.OAuth2AuthenticationRefreshToken;

/**
 * Spring Data MongoDB repository for the OAuth2AuthenticationRefreshToken entity.
 */
public interface OAuth2RefreshTokenRepository
    extends MongoRepository<OAuth2AuthenticationRefreshToken, String> {

  OAuth2AuthenticationRefreshToken findByTokenId(String tokenId);
}
