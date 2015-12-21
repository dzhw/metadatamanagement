package eu.dzhw.fdz.metadatamanagement.repository;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Survey entity.
 * 
 * @author Daniel Katzberg
 */
public interface SurveyRepository extends MongoRepository<Survey,String> {
  
  /**
   * @param fdzProjectName A FDZ project name.
   * @return A list of deleted surveys by a given fdz project name
   */
  List<Survey> deleteByFdzProjectName(String fdzProjectName);

  /**
   * @param fdzProjectName A FDZ project name.
   * @return Returns all Surveys with a given fdz project name.
   */
  List<Survey> findByFdzProjectName(String fdzProjectName);
  
}
