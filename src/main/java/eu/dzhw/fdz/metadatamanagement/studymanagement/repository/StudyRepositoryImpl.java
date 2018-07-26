package eu.dzhw.fdz.metadatamanagement.studymanagement.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Implementation of custom methods on the studies collection.
 * 
 * @author René Reitmann
 */
@Component
public class StudyRepositoryImpl implements StudyRepositoryCustom {
  private static final String COLLECTION = "studies";
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Override
  public List<I18nString> findAllStudySerieses() {
    List<I18nString> result = new ArrayList<>();
    mongoTemplate.getCollection(COLLECTION).distinct("studySeries", BsonDocument.class)
      .map(document -> I18nString.builder()
          .de(document.get("de").asString().getValue())
          .en(document.get("en").asString().getValue()).build())
      .into(result);
    return result;
  }

}
