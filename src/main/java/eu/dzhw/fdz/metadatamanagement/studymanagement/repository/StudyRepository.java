package eu.dzhw.fdz.metadatamanagement.studymanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;

/**
 * Spring Data MongoDB repository for the data acquisitionProject entity.
 * 
 * @author Daniel Katzberg
 */
@RepositoryRestResource(path = "/studies")
public interface StudyRepository extends MongoRepository<Study, String>,
    QueryDslPredicateExecutor<Study> {
  
}
