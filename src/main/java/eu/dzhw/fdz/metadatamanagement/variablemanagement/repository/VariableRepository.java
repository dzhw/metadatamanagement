package eu.dzhw.fdz.metadatamanagement.variablemanagement.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
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
  List<Variable> findByDataSetIdsContaining(String dataSetId);
  
  @RestResource(exported = false)
  List<Variable> findBySurveyIdsContaining(String surveyId);

  @RestResource(exported = false)
  List<Variable> findByDataAcquisitionProjectIdAndName(String dataAcquisitionProjectId,
      String name);
  
  @RestResource(exported = false)
  Slice<Variable> findBy(Pageable pageable);
  
  List<Variable> findByIdIn(@Param("ids") Collection<String> ids);
}
