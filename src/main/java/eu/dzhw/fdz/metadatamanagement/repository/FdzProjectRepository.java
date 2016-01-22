package eu.dzhw.fdz.metadatamanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;

/**
 * Spring Data MongoDB repository for the FdzProject entity.
 */
@RepositoryRestResource(path = "/fdz_projects")
public interface FdzProjectRepository
    extends MongoRepository<FdzProject, String>, QueryDslPredicateExecutor<FdzProject> {
}
