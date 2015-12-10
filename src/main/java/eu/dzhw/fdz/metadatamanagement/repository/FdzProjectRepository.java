package eu.dzhw.fdz.metadatamanagement.repository;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the FdzProject entity.
 */
public interface FdzProjectRepository extends MongoRepository<FdzProject,String> {

}
