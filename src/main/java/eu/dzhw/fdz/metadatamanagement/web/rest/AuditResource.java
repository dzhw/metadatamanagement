package eu.dzhw.fdz.metadatamanagement.web.rest;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.service.AuditEventService;

/**
 * REST controller for getting the audit events.
 */
@RestController
@RequestMapping(value = "/api/audits", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditResource {

  private AuditEventService auditEventService;

  @Inject
  public AuditResource(AuditEventService auditEventService) {
    this.auditEventService = auditEventService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<AuditEvent> getAll() {
    return auditEventService.findAll();
  }

  @RequestMapping(method = RequestMethod.GET, params = {"fromDate", "toDate"})
  public List<AuditEvent> getByDates(
      @RequestParam(value = "fromDate") @DateTimeFormat(
          iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam(value = "toDate") @DateTimeFormat(
          iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

    return auditEventService.findByDates(fromDate.atTime(0, 0), toDate.atTime(23, 59));
  }

  /**
   * Get a single audit event.
   */
  @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
  public ResponseEntity<AuditEvent> get(@PathVariable String id) {
    return auditEventService.find(id)
      .map((entity) -> new ResponseEntity<>(entity, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
