package eu.dzhw.fdz.metadatamanagement.variablemanagement.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Spring Data MongoDB repository for the Variable entity.
 */
@RepositoryRestResource(path = "/variables")
public interface VariableRepository
    extends MongoRepository<Variable, String>, QueryDslPredicateExecutor<Variable> {
  @RestResource(exported = false)
  List<Variable> deleteByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  List<Variable> findByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  Slice<Variable> findBySurveyIdsContaining(String surveyId, Pageable pageable);
  
  @RestResource(exported = false)
  List<Variable> findByDataSetId(String dataSetId);
  
  @RestResource(exported = false)
  Slice<Variable> findBy(Pageable pageable);
  
  @RestResource(exported = false)
  List<Variable> findByIdIn(Collection<String> ids);
  
  @RestResource(exported = false)
  Slice<Variable> findByIdIn(Collection<String> ids, Pageable pageable);
  
  @RestResource(exported = false)
  List<Variable> findByNameAndDataSetId(String name, String dataSetId);
  
  @RestResource(exported = false)
  List<Variable> findByIndexInDataSetAndDataSetId(Integer indexInDataSet, String dataSetId);
}