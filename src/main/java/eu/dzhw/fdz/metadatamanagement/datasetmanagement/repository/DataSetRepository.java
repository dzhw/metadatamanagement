package eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository;

import java.util.Collection;
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
  
  @RestResource(exported = false)
  List<DataSet> findByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Slice<DataSet> findBy(Pageable pageable);

  @RestResource(exported = false)
  List<DataSet> findBySurveyIdsContaining(String surveyId);
  
  List<DataSet> findByIdIn(@Param("ids") Collection<String> ids);
  
  List<DataSet> findByVariableIdsContaining(@Param("id") String variableId);
}
