package eu.dzhw.fdz.metadatamanagement.questionmanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.AtomicQuestion;

/**
 * Spring Data Mongo DB Repository for the Atomic Questions.
 * 
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "/atomic_questions")
public interface AtomicQuestionRepository
    extends MongoRepository<AtomicQuestion, String>, QueryDslPredicateExecutor<AtomicQuestion> {

  @RestResource(exported = false)
  List<AtomicQuestion> deleteByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  List<AtomicQuestion> deleteByVariableId(String variableId);

  @RestResource(exported = false)
  List<AtomicQuestion> deleteByQuestionnaireId(String questionnaireId);

  @RestResource(exported = false)
  List<AtomicQuestion> findByDataAcquisitionProjectId(String dataAcquisitionProjectId);
}
