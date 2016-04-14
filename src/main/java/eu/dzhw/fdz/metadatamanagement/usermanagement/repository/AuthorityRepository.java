package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
@RepositoryRestResource(exported = false)
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}
