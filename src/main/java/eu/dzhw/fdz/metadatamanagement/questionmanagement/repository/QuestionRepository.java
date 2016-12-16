package eu.dzhw.fdz.metadatamanagement.questionmanagement.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;

/**
 * The Repository for {@link Question} domain object. The data will be insert with a REST API and
 * save in a mongo db.
 */
@RepositoryRestResource(path = "/questions")
public interface QuestionRepository
    extends MongoRepository<Question, String>, QueryDslPredicateExecutor<Question> {

  @RestResource(exported = false)
  List<Question> deleteByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  List<Question> findByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  List<Question> findByInstrumentIdAndNumber(String instrumentId, String number);

  @RestResource(exported = false)
  Slice<Question> findBy(Pageable pageable);
  
  @RestResource(exported = false)
  Slice<Question> findByInstrumentId(String instrumentId, Pageable pageable);
  
}
