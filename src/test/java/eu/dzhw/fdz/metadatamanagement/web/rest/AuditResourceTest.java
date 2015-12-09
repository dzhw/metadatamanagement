package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractBasicTest;
import eu.dzhw.fdz.metadatamanagement.config.audit.AuditEventConverter;
import eu.dzhw.fdz.metadatamanagement.domain.PersistentAuditEvent;
import eu.dzhw.fdz.metadatamanagement.repository.PersistenceAuditEventRepository;
import eu.dzhw.fdz.metadatamanagement.service.AuditEventService;

/**
 * Test class for the AuditResource REST controller.
 * 
 * @author Daniel Katzberg
 *
 */
public class AuditResourceTest extends AbstractBasicTest {

  private static final String SAMPLE_PRINCIPAL = "SAMPLE_PRINCIPAL";
  private static final String SAMPLE_TYPE = "SAMPLE_TYPE";
  private static final LocalDateTime SAMPLE_TIMESTAMP = LocalDateTime.parse("2015-08-04T10:11:30");

  @Inject
  private PersistenceAuditEventRepository auditEventRepository;

  @Inject
  private AuditEventConverter auditEventConverter;

  private PersistentAuditEvent auditEvent;

  private MockMvc restAuditMockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    AuditEventService auditEventService =
        new AuditEventService(auditEventRepository, auditEventConverter);
    AuditResource auditResource = new AuditResource(auditEventService);
    this.restAuditMockMvc = MockMvcBuilders.standaloneSetup(auditResource)
      .build();
  }

  @Before
  public void initTest() {
    auditEventRepository.deleteAll();
    auditEvent = new PersistentAuditEvent();
    auditEvent.setAuditEventType(SAMPLE_TYPE);
    auditEvent.setPrincipal(SAMPLE_PRINCIPAL);
    auditEvent.setAuditEventDate(SAMPLE_TIMESTAMP);
  }

  @Test
  public void getAllAudits() throws Exception {
    // Initialize the database
    auditEventRepository.save(auditEvent);

    // Get all the audits
    restAuditMockMvc.perform(get("/api/audits"))
      .andExpect(status().isOk())
      // .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].principal").value(hasItem(SAMPLE_PRINCIPAL)));
  }

  @Test
  public void getAudit() throws Exception {
    // Initialize the database
    auditEventRepository.save(auditEvent);

    // Get the audit
    restAuditMockMvc.perform(get("/api/audits/{id}", auditEvent.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.principal").value(SAMPLE_PRINCIPAL));
  }

  @Test
  public void getNonExistingAudit() throws Exception {
    // Get the audit
    restAuditMockMvc.perform(get("/api/audits/{id}", Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testGetByDates() throws Exception {
    // Arrange
    this.auditEventRepository.save(this.auditEvent);

    // Act
    MvcResult mvcResultEmpty =
        this.restAuditMockMvc.perform(get("/api/audits?fromDate=2000-01-01&toDate=2001-12-31"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andReturn();

    MvcResult mvcResultNotEmpty =
        this.restAuditMockMvc.perform(get("/api/audits?fromDate=2014-01-01&toDate=2016-12-31"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andReturn();
    JSONArray jsonArrayEmpty = new JSONArray(mvcResultEmpty.getResponse()
      .getContentAsString());
    JSONArray jsonArrayNotEmpty = new JSONArray(mvcResultNotEmpty.getResponse()
      .getContentAsString());

    // Assert
    assertThat(mvcResultEmpty.getResponse()
      .getContentAsString(), not(nullValue()));
    assertThat(mvcResultNotEmpty.getResponse()
      .getContentAsString(), not(nullValue()));
    assertThat(jsonArrayEmpty.length(), is(0));
    assertThat(jsonArrayNotEmpty.length(), is(1));
  }

}
