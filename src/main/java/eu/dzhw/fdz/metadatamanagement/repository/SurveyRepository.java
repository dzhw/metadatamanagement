package eu.dzhw.fdz.metadatamanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.projections.CompleteSurveyProjection;

/**
 * Spring Data MongoDB repository for the Survey entity.
 * 
 * @author Daniel Katzberg
 */
@RepositoryRestResource(path = "/surveys", excerptProjection = CompleteSurveyProjection.class)
@RepositoryEventHandler
public interface SurveyRepository
    extends MongoRepository<Survey, String>, QueryDslPredicateExecutor<Survey> {

  @HandleAfterDelete
  @RestResource(exported = false)
  Long deleteByFdzProject(FdzProject fdzProject);

  Survey findOneByFdzId(@Param("fdzId") String fdzId);
}
