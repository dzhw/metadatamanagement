package eu.dzhw.fdz.metadatamanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.domain.Questionnaire;

/**
 * The Repository for the Questionnaires.
 * 
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "/questionnaires")
public interface QuestionnaireRepository
    extends MongoRepository<Questionnaire, String>, QueryDslPredicateExecutor<Questionnaire> {

  @RestResource(exported = false)
  List<Questionnaire> deleteByDataAcquisitionProjectId(String dataAcquisitionProjectId);
}
