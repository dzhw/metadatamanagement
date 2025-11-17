package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class ConceptVersionsResourceTest extends AbstractTest {
  private static final String API_CONCEPT_URI = "/api/concepts";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private ConceptRepository conceptRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    conceptRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testCreateConceptAndReadVersions() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();

    // create the concept with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
        .content(TestUtil.convertObjectToJsonBytes(concept))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // read the concept versions
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId() + "/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(equalTo(1))))
        .andExpect(jsonPath("$[0].id", is(concept.getId())))
        .andExpect(jsonPath("$[0].title.de", is(concept.getTitle().getDe())))
        .andExpect(jsonPath("$[0].authors.length()", is(equalTo(concept.getAuthors().size()))))
        .andExpect(
            jsonPath("$[0].authors[0].firstName", is(concept.getAuthors().get(0).getFirstName())));
  }

  @Test
  public void testEditConceptAndReadVersions() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();
    String firstTitle = concept.getTitle().getDe();

    // create the concept with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
        .content(TestUtil.convertObjectToJsonBytes(concept))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    concept.setVersion(0L);
    // update the concept with the given id
    concept.setTitle(new I18nString("hurzDe2", "hurzEn2"));
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
        .content(TestUtil.convertObjectToJsonBytes(concept))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

    concept.setVersion(1L);
    // update the concept again with the given id
    concept.setTitle(new I18nString("hurzDe3", "hurzEn3"));
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
        .content(TestUtil.convertObjectToJsonBytes(concept))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

    // read the concept versions
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId() + "/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(equalTo(3))))
        .andExpect(jsonPath("$[0].id", is(concept.getId())))
        .andExpect(jsonPath("$[0].title.de", is("hurzDe3")))
        .andExpect(jsonPath("$[0].version", is(equalTo(2))))
        .andExpect(jsonPath("$[0].authors.length()", is(equalTo(concept.getAuthors().size()))))
        .andExpect(
            jsonPath("$[0].authors[0].firstName", is(concept.getAuthors().get(0).getFirstName())))
        .andExpect(jsonPath("$[1].id", is(concept.getId())))
        .andExpect(jsonPath("$[1].title.de", is("hurzDe2")))
        .andExpect(jsonPath("$[1].version", is(equalTo(1))))
        .andExpect(jsonPath("$[1].authors.length()", is(equalTo(concept.getAuthors().size()))))
        .andExpect(
            jsonPath("$[1].authors[0].firstName", is(concept.getAuthors().get(0).getFirstName())))
        .andExpect(jsonPath("$[2].id", is(concept.getId())))
        .andExpect(jsonPath("$[2].version", is(equalTo(0))))
        .andExpect(jsonPath("$[2].title.de", is(firstTitle)))
        .andExpect(jsonPath("$[2].authors.length()", is(equalTo(concept.getAuthors().size()))))
        .andExpect(
            jsonPath("$[2].authors[0].firstName", is(concept.getAuthors().get(0).getFirstName())));

    // read the first two concept versions
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId() + "/versions?skip=1"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(equalTo(2))))
        .andExpect(jsonPath("$[0].id", is(concept.getId())))
        .andExpect(jsonPath("$[0].title.de", is("hurzDe2")))
        .andExpect(jsonPath("$[0].version", is(equalTo(1))))
        .andExpect(jsonPath("$[0].authors.length()", is(equalTo(concept.getAuthors().size()))))
        .andExpect(
            jsonPath("$[0].authors[0].firstName", is(concept.getAuthors().get(0).getFirstName())))
        .andExpect(jsonPath("$[1].id", is(concept.getId())))
        .andExpect(jsonPath("$[1].version", is(equalTo(0))))
        .andExpect(jsonPath("$[1].title.de", is(firstTitle)))
        .andExpect(jsonPath("$[1].authors.length()", is(equalTo(concept.getAuthors().size()))))
        .andExpect(
            jsonPath("$[1].authors[0].firstName", is(concept.getAuthors().get(0).getFirstName())));
  }

  @Test
  public void testConceptVersionsNotFound() throws IOException, Exception {
    mockMvc.perform(get(API_CONCEPT_URI + "/spaß/versions")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(0)));
  }
}
