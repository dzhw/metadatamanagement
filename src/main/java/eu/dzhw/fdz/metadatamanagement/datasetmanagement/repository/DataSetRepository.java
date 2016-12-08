package eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;

/**
 * The Repository for {@link DataSet} domain object. The data will be insert with a REST API and
 * save in a mongo db.
 * 
 * @author Daniel Katzberg *
 */
@RepositoryRestResource(path = "/data-sets")
public interface DataSetRepository
    extends MongoRepository<DataSet, String>, QueryDslPredicateExecutor<DataSet> {

  @RestResource(exported = false)
  List<DataSet> deleteByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  List<DataSet> findByDataAcquisitionProjectId(@Param("id") String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Slice<DataSet> findBy(Pageable pageable);

  @RestResource(exported = false)
  List<DataSet> findBySurveyIdsContaining(String surveyId);
  
  @RestResource(exported = false)
  List<DataSet> findByVariableIdsContaining(String variableId);
  
}
