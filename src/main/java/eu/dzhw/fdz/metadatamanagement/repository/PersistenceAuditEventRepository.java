package eu.dzhw.fdz.metadatamanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.dzhw.fdz.metadatamanagement.domain.PersistentAuditEvent;

/**
 * Spring Data JPA repository for the PersistentAuditEvent entity.
 */
public interface PersistenceAuditEventRepository extends JpaRepository<PersistentAuditEvent, Long> {

  List<PersistentAuditEvent> findByPrincipal(String principal);

  List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal,
      LocalDateTime after);

  List<PersistentAuditEvent> findAllByAuditEventDateBetween(LocalDateTime fromDate,
      LocalDateTime toDate);
}
