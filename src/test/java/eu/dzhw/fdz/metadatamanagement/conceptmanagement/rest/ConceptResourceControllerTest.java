package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class ConceptResourceControllerTest extends AbstractTest {
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

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
    elasticsearchAdminService.recreateAllIndices();
  }

  @After
  public void cleanUp() {
    conceptRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    javersService.deleteAll();
  }
  
  @Test
  public void testCreateConcept() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();
    
    // create the concept with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // read the concept under the new url
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId()))
      .andExpect(status().isOk());
  }
   
  @Test
  public void testCreateConceptWithWrongId() throws IOException, Exception { 
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();
    concept.setId("hurz");
      
    // create the concept with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)))
      .andExpect(status().is4xxClientError());
  }
  
  @Test
  public void testUpdateConcept() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();
    
    // create the concept with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
    
    List<Person> authors = new ArrayList<>();
    authors.add(UnitTestCreateDomainObjectUtils.buildPerson("Another", null, "Author"));
    concept.setAuthors(authors);

    concept.setVersion(0L);
    // update the concept with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful());

    // read the concept under the new url
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(concept.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.authors[0].firstName", is("Another")))
      .andExpect(jsonPath("$.authors[0].lastName", is("Author")));
  }
  
  @Test
  public void testDeleteConcept() throws IOException, Exception {
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();
    
    // create the project with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc.perform(delete(API_CONCEPT_URI + "/" + concept.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId()))
      .andExpect(status().isNotFound());
  }
}
