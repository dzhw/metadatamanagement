package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthorizationDocument;

@RepositoryRestResource (exported = false)
public interface OAuth2AuthorizationRepository
    extends MongoRepository<OAuth2AuthorizationDocument, String> {

  Optional<OAuth2AuthorizationDocument> findByAccessTokenValue(String accessTokenValue);
  Optional<OAuth2AuthorizationDocument> findByRefreshTokenValue(String refreshTokenValue);
  Optional<OAuth2AuthorizationDocument> findByState(String state);
  void deleteByPrincipalName(String principalName);
}
