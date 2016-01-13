package eu.dzhw.fdz.metadatamanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;

/**
 * Spring Data MongoDB repository for the FdzProject entity.
 */
public interface FdzProjectRepository
    extends MongoRepository<FdzProject, String>, QueryDslPredicateExecutor<FdzProject> {

}
