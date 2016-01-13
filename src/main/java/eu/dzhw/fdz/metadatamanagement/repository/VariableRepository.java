package eu.dzhw.fdz.metadatamanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;

/**
 * Spring Data MongoDB repository for the Variable entity.
 */
public interface VariableRepository
    extends MongoRepository<Variable, String>, QueryDslPredicateExecutor<Variable> {

  /**
   * @param fdzProjectName A FDZ project name.
   * @return A list of deleted variables by a given fdz project name
   */
  List<Variable> deleteByFdzProjectName(String fdzProjectName);

  /**
   * @param surveyId An id of a service.
   * @return A list of deleted variables by a given survey id.
   */
  List<Variable> deleteBySurveyId(String surveyId);

}
