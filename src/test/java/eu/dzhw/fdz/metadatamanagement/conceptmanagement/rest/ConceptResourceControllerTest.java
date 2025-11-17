package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
public class ConceptResourceControllerTest extends AbstractTest {
  private static final String API_CONCEPT_URI = "/api/concepts";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private ConceptRepository conceptRepository;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private QuestionRepository questionRepository;

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
    instrumentRepository.deleteAll();
    questionRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateConcept() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();

    // create the concept with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
        .content(TestUtil.convertObjectToJsonBytes(concept))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // read the concept under the new url
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId())).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateConceptWithPost() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();

    // create the concept with the given id
    mockMvc.perform(post(API_CONCEPT_URI).content(TestUtil.convertObjectToJsonBytes(concept))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // read the concept under the new url
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId())).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateConceptWithWrongId() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();
    concept.setId("hurz");

    // create the concept with the given id
    mockMvc
        .perform(put(API_CONCEPT_URI + "/" + concept.getId())
            .content(TestUtil.convertObjectToJsonBytes(concept)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateConcept() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();

    // create the concept with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
        .content(TestUtil.convertObjectToJsonBytes(concept))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    List<Person> authors = new ArrayList<>();
    authors.add(UnitTestCreateDomainObjectUtils.buildPerson("Another", null, "Author"));
    concept.setAuthors(authors);

    concept.setVersion(0L);
    // update the concept with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
        .content(TestUtil.convertObjectToJsonBytes(concept))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // read the concept under the new url
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId())).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(concept.getId()))).andExpect(jsonPath("$.version", is(1)))
        .andExpect(jsonPath("$.authors[0].firstName", is("Another")))
        .andExpect(jsonPath("$.authors[0].lastName", is("Author")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteConcept() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();

    // create the project with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
        .content(TestUtil.convertObjectToJsonBytes(concept))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc.perform(delete(API_CONCEPT_URI + "/" + concept.getId()))
        .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId())).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteUsedConceptByQuestion() throws Exception {
    Concept conceptInQuestion = UnitTestCreateDomainObjectUtils.buildConcept();
    conceptRepository.save(conceptInQuestion);
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion("project1", 1, "ins-1");
    question.setConceptIds(Collections.singletonList(conceptInQuestion.getId()));

    mockMvc.perform(put("/api/questions/" + question.getId())
        .content(TestUtil.convertObjectToJsonBytes(question))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    mockMvc.perform(delete(API_CONCEPT_URI + "/" + conceptInQuestion.getId()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message",
            is("concept-management.error.concept.in-use.questions")))
        .andExpect(jsonPath("$.errors[0].invalidValue[0]", is(question.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteUsedConceptByInstrument() throws Exception {
    Concept conceptInInstrument = UnitTestCreateDomainObjectUtils.buildConcept();
    conceptInInstrument.setId("con-conceptid2$");
    conceptRepository.save(conceptInInstrument);
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument("project1");
    instrument.setConceptIds(Collections.singletonList(conceptInInstrument.getId()));

    mockMvc.perform(put("/api/instruments/" + instrument.getId())
        .content(TestUtil.convertObjectToJsonBytes(instrument))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    mockMvc.perform(delete(API_CONCEPT_URI + "/" + conceptInInstrument.getId()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message",
            is("concept-management.error.concept.in-use.instruments")))
        .andExpect(jsonPath("$.errors[0].invalidValue[0]", is(instrument.getId())));
  }

  @Test
  public void testGetConceptAsPublicUser() throws Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();
    conceptRepository.save(concept);

    elasticsearchAdminService.recreateAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));

    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId())).andExpect(status().isOk())
        .andExpect(jsonPath("$.completeTitle").exists());
  }
}
