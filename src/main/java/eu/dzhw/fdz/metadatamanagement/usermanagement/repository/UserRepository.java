package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;

/**
 * Spring Data MongoDB repository for the User entity.
 */
@RepositoryRestResource(exported = false)
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findOneByLogin(String login);
}
