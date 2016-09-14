package eu.dzhw.fdz.metadatamanagement.surveymanagement.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * Spring Data MongoDB repository for the Survey entity.
 * 
 * @author Daniel Katzberg
 */
@RepositoryRestResource(path = "/surveys")
public interface SurveyRepository
    extends MongoRepository<Survey, String>, QueryDslPredicateExecutor<Survey> {

  @RestResource(exported = false)
  List<Survey> deleteByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  
  List<Survey> findByDataAcquisitionProjectId(@Param("id") String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Slice<Survey> findBy(Pageable pageable);
  
  List<Survey> findByIdIn(@Param("ids") Collection<String> ids);
}
