package eu.dzhw.fdz.metadatamanagement.repository;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Survey entity.
 */
public interface SurveyRepository extends MongoRepository<Survey,String> {

}
