package eu.dzhw.fdz.metadatamanagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.config.audit.AuditEventConverter;
import eu.dzhw.fdz.metadatamanagement.domain.PersistentAuditEvent;
import eu.dzhw.fdz.metadatamanagement.repository.PersistenceAuditEventRepository;

/**
 * Service for managing audit events.
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
public class AuditEventService {

  private PersistenceAuditEventRepository persistenceAuditEventRepository;

  private AuditEventConverter auditEventConverter;

  /**
   * Create the audit event service.
   */
  @Inject
  public AuditEventService(PersistenceAuditEventRepository persistenceAuditEventRepository,
      AuditEventConverter auditEventConverter) {

    this.persistenceAuditEventRepository = persistenceAuditEventRepository;
    this.auditEventConverter = auditEventConverter;
  }

  public List<AuditEvent> findAll() {
    return auditEventConverter.convertToAuditEvent(persistenceAuditEventRepository.findAll());
  }

  /**
   * Find all audit events.
   */
  public List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate) {
    List<PersistentAuditEvent> persistentAuditEvents =
        persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate);

    return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
  }

  public Optional<AuditEvent> find(String id) {
    return Optional.ofNullable(persistenceAuditEventRepository.findOne(id))
      .map(auditEventConverter::convertToAuditEvent);
  }
}
