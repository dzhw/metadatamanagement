package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

/**
 * Custom implementation for {@link DataAcquisitionProjectRepository}.
 */
@Component
class DataAcquisitionProjectRepositoryCustomImpl implements DataAcquisitionProjectRepositoryCustom {

  private MongoTemplate mongoTemplate;

  /**
   * Creates a new {@link DataAcquisitionProjectRepositoryCustomImpl} instance.
   */
  public DataAcquisitionProjectRepositoryCustomImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Page<DataAcquisitionProject> findAllMastersByIdLikeAndPublisherIdOrderByIdAsc(
    String projectId, String dataProviderId, Pageable pageable) {
    List<String> dataProviderIdValues = Collections.singletonList(dataProviderId);
    Criteria criteria = where("configuration.dataProviders")
        .in(dataProviderIdValues)
        .and("_id")
        .regex(projectId, "i")
        .and("shadow").is(false);

    Query query = query(criteria).with(Sort.by(Sort.Direction.ASC, "_id")).with(pageable);
    List<DataAcquisitionProject> list = mongoTemplate.find(query, DataAcquisitionProject.class);

    return PageableExecutionUtils.getPage(list, pageable,
      () -> mongoTemplate.count(query, DataAcquisitionProject.class));
  }

  @Override
  public Optional<DataAcquisitionProject> findByProjectIdAndDataProviderId(String projectId,
                                                                           String dataProviderId) {
    List<String> dataProviderIdValues = Collections.singletonList(dataProviderId);
    Criteria criteria = where("configuration.dataProviders")
        .in(dataProviderIdValues)
        .and("_id")
        .is(projectId);

    DataAcquisitionProject project = mongoTemplate.findOne(
        query(criteria),
        DataAcquisitionProject.class
    );
    return Optional.ofNullable(project);
  }
}
