package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.DeleteResult;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthenticationRefreshToken;

/**
 * The implementation of the custom repo methods.
 * 
 * @author René Reitmann
 */
@Component
public class OAuth2RefreshTokenRepositoryImpl implements OAuth2RefreshTokenRepositoryCustom {
  public static final String ID = "_id";
  private MongoTemplate mongoTemplate;

  public OAuth2RefreshTokenRepositoryImpl(final MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public OAuth2AuthenticationRefreshToken findByTokenId(final String tokenId) {
    final Query query = Query.query(Criteria.where(ID).is(tokenId));
    return mongoTemplate.findOne(query, OAuth2AuthenticationRefreshToken.class);
  }

  @Override
  public boolean deleteByTokenId(final String tokenId) {
    final Query query = Query.query(Criteria.where(ID).is(tokenId));
    final DeleteResult deleteResult =
        mongoTemplate.remove(query, OAuth2AuthenticationRefreshToken.class);
    return deleteResult.wasAcknowledged();
  }
}
