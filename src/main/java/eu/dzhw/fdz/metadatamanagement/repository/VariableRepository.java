package eu.dzhw.fdz.metadatamanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.projections.CompleteVariableProjection;

/**
 * Spring Data MongoDB repository for the Variable entity.
 */
@RepositoryRestResource(path = "/variables", excerptProjection = CompleteVariableProjection.class)
@RepositoryEventHandler
public interface VariableRepository
    extends MongoRepository<Variable, String>, QueryDslPredicateExecutor<Variable> {
  
  @HandleAfterDelete
  Long deleteBySurvey(Survey survey);
  
  @HandleAfterDelete
  Long deleteByFdzProject(FdzProject fdzProject);
  
  Variable findOneByFdzId(@Param("fdzId") String fdzId);
}
