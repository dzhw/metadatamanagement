package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;

/**
 * Spring Data MongoDB repository for the User entity.
 */
@RepositoryRestResource(exported = false)
public interface UserRepository extends MongoRepository<User, String> {

  List<User> findAllByActivatedIsFalseAndCreatedDateBefore(LocalDateTime dateTime);

  Optional<User> findOneByResetKey(String resetKey);

  Optional<User> findOneByEmail(String email);

  Optional<User> findOneByLogin(String login);

  void deleteByEmail(String email);
}
