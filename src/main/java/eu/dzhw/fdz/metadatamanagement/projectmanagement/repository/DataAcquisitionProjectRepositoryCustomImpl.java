package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class DataAcquisitionProjectRepositoryCustomImpl implements DataAcquisitionProjectRepositoryCustom {

  private MongoTemplate mongoTemplate;

  public DataAcquisitionProjectRepositoryCustomImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public List<DataAcquisitionProject> findAllByIdLikeAndPublisherId(String projectId, String publisherId) {
    List<String> dataProviderIdValues = Collections.singletonList(publisherId);
    Criteria criteria = where("configuration.dataProviders")
      .in(dataProviderIdValues)
      .and("_id")
      .regex(projectId, "i");

    return mongoTemplate.find(query(criteria), DataAcquisitionProject.class);
  }
}
