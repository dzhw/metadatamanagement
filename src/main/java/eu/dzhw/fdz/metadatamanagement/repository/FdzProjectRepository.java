package eu.dzhw.fdz.metadatamanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.projections.CompleteFdzProjectProjection;

/**
 * Spring Data MongoDB repository for the FdzProject entity.
 */
@RepositoryRestResource(path = "/fdz_projects",
    excerptProjection = CompleteFdzProjectProjection.class)
public interface FdzProjectRepository
    extends MongoRepository<FdzProject, String>, QueryDslPredicateExecutor<FdzProject> {

  FdzProject findOneByName(@Param("name") String name);

}
