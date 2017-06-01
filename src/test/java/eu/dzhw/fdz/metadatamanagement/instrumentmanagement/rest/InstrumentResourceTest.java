/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class InstrumentResourceTest extends AbstractTest {
  private static final String API_INSTRUMENTS_URI = "/api/instruments";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private MockMvc mockMvc;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
    elasticsearchAdminService.recreateAllIndices();
  }

  @After
  public void cleanUp() {
    this.dataAcquisitionProjectRepository.deleteAll();
    this.instrumentRepository.deleteAll();
    this.elasticsearchUpdateQueueService.clearQueue();
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testCreateInstrument() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Instrument instrument =
        UnitTestCreateDomainObjectUtils.buildInstrument(project.getId(), project.getId() + "-sy1");

    // Act and Assert
    // create the instrument with the given id
    mockMvc.perform(put(API_INSTRUMENTS_URI + "/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().isCreated());

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one instrument document
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1.0));

    // check that auditing attributes have been set
    mockMvc.perform(get(API_INSTRUMENTS_URI + "/" + instrument.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.createdBy", is("test")))
      .andExpect(jsonPath("$.lastModifiedBy", is("test")));

    // call toString for test coverage :-|
    instrument.toString();
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUpdateInstrument() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Instrument instrument =
        UnitTestCreateDomainObjectUtils.buildInstrument(project.getId(), project.getId() + "-sy1");

    // Act and Assert
    // create the instrument with the given id
    mockMvc.perform(put(API_INSTRUMENTS_URI + "/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().isCreated());

    // delete the survey
    mockMvc.perform(delete(API_INSTRUMENTS_URI + "/" + instrument.getId()))
      .andExpect(status().is2xxSuccessful());

    instrument.setTitle(new I18nString("Hurz2", "Hurz2"));

    // update the instrument with the given id
    mockMvc.perform(put(API_INSTRUMENTS_URI + "/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().is2xxSuccessful());

    // read the updated instrument and check the version
    mockMvc.perform(get(API_INSTRUMENTS_URI + "/" + instrument.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(instrument.getId())))
      .andExpect(jsonPath("$.version", is(0)))
      .andExpect(jsonPath("$.title.de", is("Hurz2")));

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one instrument documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1.0));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUpdateWithWrongType() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Instrument instrument =
        UnitTestCreateDomainObjectUtils.buildInstrument(project.getId(), project.getId() + "-sy1");

    // Act and Assert
    // create the instrument with the given id
    mockMvc.perform(put(API_INSTRUMENTS_URI + "/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().isCreated());

    // delete the survey
    mockMvc.perform(delete(API_INSTRUMENTS_URI + "/" + instrument.getId()))
      .andExpect(status().is2xxSuccessful());

    // set inconsistent type
    instrument.setType("HURZ");

    // update the instrument with the given id
    mockMvc.perform(put(API_INSTRUMENTS_URI + "/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().isBadRequest())
      .andExpect(
          jsonPath("$.errors[0].message", is("instrument-management.error.instrument.type.valid")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testCreateWithWrongId() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Instrument instrument =
        UnitTestCreateDomainObjectUtils.buildInstrument(project.getId(), project.getId() + "-sy1");

    // Act and Assert
    // set inconsistent id
    instrument.setId("HURZ");

    // create the instrument with the given id
    mockMvc.perform(put(API_INSTRUMENTS_URI + "/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("instrument-management.error" + ".instrument.valid-instrument-id-pattern")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testCreateWithWrongTitle() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Instrument instrument =
        UnitTestCreateDomainObjectUtils.buildInstrument(project.getId(), project.getId() + "-sy1");

    // Act and Assert
    // set inconsistent title
    instrument.setTitle(new I18nString());

    // create the instrument with the given id
    mockMvc.perform(put(API_INSTRUMENTS_URI + "/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("instrument-management.error.instrument.title.i18n-string-not-empty")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testInstrumentIsDeletedWithProject() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Instrument instrument =
        UnitTestCreateDomainObjectUtils.buildInstrument(project.getId(), project.getId() + "-sy1");

    // create the instrument with the given id
    mockMvc.perform(put(API_INSTRUMENTS_URI + "/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().isCreated());

    // check that the instrument has been created
    mockMvc.perform(get(API_INSTRUMENTS_URI + "/" + instrument.getId()))
      .andExpect(status().isOk());

    // delete the project
    mockMvc.perform(delete("/api/data-acquisition-projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the instrument has been deleted as well
    mockMvc.perform(get(API_INSTRUMENTS_URI + "/" + instrument.getId()))
      .andExpect(status().isNotFound());
  }
}
