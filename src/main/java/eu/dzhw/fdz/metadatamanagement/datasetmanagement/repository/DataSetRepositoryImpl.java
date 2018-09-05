package eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;

/**
 * Implementation of custom methods on the data_sets collection.
 * 
 * @author René Reitmann
 */
@Component
public class DataSetRepositoryImpl implements DataSetRepositoryCustom {
  private static final String COLLECTION = "data_sets";

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public List<String> findAllAccessWays(String studyId) {
    List<String> result = new ArrayList<>();
    mongoTemplate
        .getCollection(COLLECTION).distinct("subDataSets.accessWay",
            new BasicDBObject("studyId", studyId), String.class)
        .into(result);
    return result;
  }

}
