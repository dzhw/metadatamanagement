package eu.dzhw.fdz.metadatamanagement.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.dzhw.fdz.metadatamanagement.domain.User;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findOneByActivationKey(String activationKey);

  List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

  Optional<User> findOneByResetKey(String resetKey);

  Optional<User> findOneByEmail(String email);

  Optional<User> findOneByLogin(String login);

  @Override
  void delete(User user);

}
