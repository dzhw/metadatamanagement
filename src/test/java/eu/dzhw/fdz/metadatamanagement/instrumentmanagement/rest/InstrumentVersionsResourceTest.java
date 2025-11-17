package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class InstrumentVersionsResourceTest extends AbstractTest {
  private static final String API_INSTRUMENT_URI = "/api/instruments";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @AfterEach
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    instrumentRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testCreateInstrumentAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());

    // create the instrument with the given id
    mockMvc
        .perform(put(API_INSTRUMENT_URI + "/" + instrument.getId())
            .content(TestUtil.convertObjectToJsonBytes(instrument))
            .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // read the instrument versions
    mockMvc.perform(get(API_INSTRUMENT_URI + "/" + instrument.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(1))))
        .andExpect(jsonPath("$[0].id", is(instrument.getId())))
        .andExpect(jsonPath("$[0].title.de", is(instrument.getTitle().getDe())));
  }

  @Test
  public void testEditInstrumentAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    String firstTitle = instrument.getTitle().getDe();

    // create the instrument with the given id
    mockMvc
        .perform(put(API_INSTRUMENT_URI + "/" + instrument.getId())
            .content(TestUtil.convertObjectToJsonBytes(instrument))
            .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    instrument.setVersion(0L);
    // update the instrument with the given id
    instrument.setTitle(new I18nString("hurzDe2", "hurzEn2"));
    mockMvc
        .perform(put(API_INSTRUMENT_URI + "/" + instrument.getId())
            .content(TestUtil.convertObjectToJsonBytes(instrument))
            .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    instrument.setVersion(1L);
    // update the instrument again with the given id
    instrument.setTitle(new I18nString("hurzDe3", "hurzEn3"));
    mockMvc
        .perform(put(API_INSTRUMENT_URI + "/" + instrument.getId())
            .content(TestUtil.convertObjectToJsonBytes(instrument))
            .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // read the instrument versions
    mockMvc.perform(get(API_INSTRUMENT_URI + "/" + instrument.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(3))))
        .andExpect(jsonPath("$[0].id", is(instrument.getId())))
      .andExpect(jsonPath("$[0].title.de", is("hurzDe3")))
      .andExpect(jsonPath("$[0].version", is(equalTo(2))))
        .andExpect(jsonPath("$[1].id", is(instrument.getId())))
      .andExpect(jsonPath("$[1].title.de", is("hurzDe2")))
      .andExpect(jsonPath("$[1].version", is(equalTo(1))))
        .andExpect(jsonPath("$[2].id", is(instrument.getId())))
      .andExpect(jsonPath("$[2].version", is(equalTo(0))))
      .andExpect(jsonPath("$[2].title.de", is(firstTitle)));
  }

  @Test
  public void testInstrumentVersionsNotFound() throws IOException, Exception {
    mockMvc.perform(get(API_INSTRUMENT_URI + "/spaß/versions")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(0)));
  }
}
