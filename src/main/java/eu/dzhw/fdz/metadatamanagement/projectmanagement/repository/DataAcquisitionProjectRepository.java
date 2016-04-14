package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

/**
 * Spring Data MongoDB repository for the data acquisitionProject entity.
 * 
 * @author Daniel Katzberg
 */
@RepositoryRestResource(path = "/data-acquisition-projects")
public interface DataAcquisitionProjectRepository
    extends MongoRepository<DataAcquisitionProject, String>,
    QueryDslPredicateExecutor<DataAcquisitionProject> {
}
