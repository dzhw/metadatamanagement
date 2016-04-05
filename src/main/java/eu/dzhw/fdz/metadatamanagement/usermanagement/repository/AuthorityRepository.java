package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}
