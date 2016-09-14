package eu.dzhw.fdz.metadatamanagement.questionmanagement.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
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

  List<Question> findByDataAcquisitionProjectId(@Param("id") String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  List<Question> findBySurveyId(String surveyId);

  @RestResource(exported = false)
  Slice<Question> findBy(Pageable pageable);
  
  List<Question> findBySuccessorsContaining(@Param("id") String id);
  
  List<Question> findByIdIn(@Param("ids") Collection<String> ids);
}
