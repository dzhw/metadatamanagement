package eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of custom methods on the data_sets collection.
 * 
 * @author René Reitmann
 */
@Component
@RequiredArgsConstructor
public class DataSetRepositoryImpl implements DataSetRepositoryCustom {
  private static final String COLLECTION = "data_sets";

  private final MongoTemplate mongoTemplate;

  @Override
  public List<String> findAllAccessWays(String dataPackageId) {
    List<String> result = new ArrayList<>();
    mongoTemplate
        .getCollection(COLLECTION).distinct("subDataSets.accessWay",
            new BasicDBObject("dataPackageId", dataPackageId), String.class)
        .into(result);
    return result;
  }

}
