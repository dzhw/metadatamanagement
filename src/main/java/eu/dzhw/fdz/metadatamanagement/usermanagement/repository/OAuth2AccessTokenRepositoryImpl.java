package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.DeleteResult;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthenticationAccessToken;

/**
 * The implementation of the custom repo methods.
 * 
 * @author René Reitmann
 */
@Component
public class OAuth2AccessTokenRepositoryImpl implements OAuth2AccessTokenRepositoryCustom {
  public static final String ID = "_id";
  private final MongoTemplate mongoTemplate;

  public OAuth2AccessTokenRepositoryImpl(final MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public OAuth2AuthenticationAccessToken findByTokenId(final String tokenId) {
    final Query query = Query.query(Criteria.where(ID).is(tokenId));
    return mongoTemplate.findOne(query, OAuth2AuthenticationAccessToken.class);
  }

  @Override
  public boolean deleteByTokenId(final String tokenId) {
    final Query query = Query.query(Criteria.where(ID).is(tokenId));
    final DeleteResult deleteResult =
        mongoTemplate.remove(query, OAuth2AuthenticationAccessToken.class);
    return deleteResult.wasAcknowledged();
  }

  @Override
  public boolean deleteByRefreshTokenId(String refreshTokenId) {
    final Query query = Query.query(Criteria.where("refreshToken").is(refreshTokenId));
    final DeleteResult deleteResult =
        mongoTemplate.remove(query, OAuth2AuthenticationAccessToken.class);
    return deleteResult.wasAcknowledged();
  }

  @Override
  public OAuth2AuthenticationAccessToken findByAuthenticationId(String key) {
    final Query query = Query.query(Criteria.where("authenticationId").is(key));
    return mongoTemplate.findOne(query, OAuth2AuthenticationAccessToken.class);
  }

  @Override
  public List<OAuth2AuthenticationAccessToken> findByUsernameAndClientId(final String username,
      final String clientId) {
    final Query query = Query.query(Criteria.where("username").is(username)
        .andOperator(Criteria.where("clientId").is(clientId)));
    return mongoTemplate.find(query, OAuth2AuthenticationAccessToken.class);
  }

  @Override
  public List<OAuth2AuthenticationAccessToken> findByClientId(final String clientId) {
    final Query query = Query.query(Criteria.where("clientId").is(clientId));
    return mongoTemplate.find(query, OAuth2AuthenticationAccessToken.class);
  }
}
