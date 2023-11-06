package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;

/**
 * Spring Data MongoDB repository for the User entity.
 */
@RepositoryRestResource(exported = false)
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findOneByActivationKey(String activationKey);

  List<User> findAllByActivatedIsFalseAndCreatedDateBefore(LocalDateTime dateTime);

  List<User> findAllByAuthoritiesContaining(Authority authority);

  List<User> findAllByLoginLikeOrEmailLike(String login, String email);

  List<User> findAllByLoginIn(Set<String> userLoginNames);

  Optional<User> findOneByResetKey(String resetKey);

  Optional<User> findOneByEmail(String email);

  Optional<User> findOneByLoginOrEmail(String login, String email);

  Optional<User> findOneByLogin(String login);

  void deleteByEmail(String email);

  @Query("{ $or: [ { 'login': { $regex: ?0, $options: 'i' } }, { 'email': { $regex: ?0, $options: 'i' } }," +
    "{ 'firstName': { $regex: ?0, $options: 'i' } }, { 'lastName': { $regex: ?0, $options: 'i' } } ] }")
  Page<User> findByLoginNameEmail(String searchTerm, Pageable pageable);

}
