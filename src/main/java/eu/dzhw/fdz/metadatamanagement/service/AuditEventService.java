package eu.dzhw.fdz.metadatamanagement.service;

import eu.dzhw.fdz.metadatamanagement.config.audit.AuditEventConverter;
import eu.dzhw.fdz.metadatamanagement.domain.PersistentAuditEvent;
import eu.dzhw.fdz.metadatamanagement.repository.PersistenceAuditEventRepository;
import java.time.LocalDateTime;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing audit events.
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
@Transactional
public class AuditEventService {

  private PersistenceAuditEventRepository persistenceAuditEventRepository;

  private AuditEventConverter auditEventConverter;

  /**
   * The constructor of the AuditEventService.
   * 
   * @param persistenceAuditEventRepository the repository of audit events.
   * @param auditEventConverter the audit event converter.
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
   * Find audit events by a date range (from to).
   * 
   * @param fromDate the start of the date range.
   * @param toDate the end of the date range.
   * @return returns a list of audit events within the date range.
   */
  public List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate) {
    List<PersistentAuditEvent> persistentAuditEvents =
        persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate);

    return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
  }

  public Optional<AuditEvent> find(Long id) {
    return Optional.ofNullable(persistenceAuditEventRepository.findOne(id))
        .map(auditEventConverter::convertToAuditEvent);
  }
}
