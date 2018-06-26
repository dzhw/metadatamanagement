package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthenticationAccessToken;

/**
 * Spring Data MongoDB repository for the OAuth2AuthenticationAccessToken entity.
 */
@RepositoryRestResource(exported = false)
public interface OAuth2AccessTokenRepository
    extends MongoRepository<OAuth2AuthenticationAccessToken, String>, 
      OAuth2AccessTokenRepositoryCustom {
  List<OAuth2AuthenticationAccessToken> findByUsername(String username);
}
