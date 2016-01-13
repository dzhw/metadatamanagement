package eu.dzhw.fdz.metadatamanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;

/**
 * Spring Data MongoDB repository for the Survey entity.
 * 
 * @author Daniel Katzberg
 */
public interface SurveyRepository
    extends MongoRepository<Survey, String>, QueryDslPredicateExecutor<Survey> {

  /**
   * @param fdzProjectName A FDZ project name.
   * @return A list of deleted surveys by a given fdz project name
   */
  List<Survey> deleteByFdzProjectName(String fdzProjectName);
}
