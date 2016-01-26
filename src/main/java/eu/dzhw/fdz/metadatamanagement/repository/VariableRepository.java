package eu.dzhw.fdz.metadatamanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;

/**
 * Spring Data MongoDB repository for the Variable entity.
 */
@RepositoryRestResource(path = "/variables")
public interface VariableRepository
    extends MongoRepository<Variable, String>, QueryDslPredicateExecutor<Variable> {
  
  @RestResource(exported = false)
  List<Variable> deleteBySurvey(Survey survey);
  
  @RestResource(exported = false)
  List<Variable> deleteByFdzProject(FdzProject fdzProject);
}
