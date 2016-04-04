package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.repository.ConceptRepository;

/**
 * @author Daniel Katzberg
 *
 */
public class ConceptResourceTest extends AbstractTest {

  private static final String API_CONCEPT_URI = "/api/concepts";


  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private ConceptRepository conceptRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.conceptRepository.deleteAll();
  }

  @Test
  public void testCreateConcept() throws IOException, Exception {

    // Arrange
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)))
      .andExpect(status().isCreated());

    // read the project under the new url
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId()))
      .andExpect(status().isOk());

    // call toString for test coverage :-)
    concept.toString();
  }

  @Test
  public void testDeleteConcept() throws IOException, Exception {

    // Arrange
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)))
      .andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc.perform(delete(API_CONCEPT_URI + "/" + concept.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId()))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testCompleteProjectionContainsId() throws IOException, Exception {

    // Arrange
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)))
      .andExpect(status().isCreated());

    // load the project with the projection
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(concept.getId())))
      .andExpect(jsonPath("$.version", is(0)));
  }

  @Test
  public void testUpdateConcept() throws IOException, Exception {

    // Arrange
    Concept concept = UnitTestCreateDomainObjectUtils.buildConcept();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)))
      .andExpect(status().isCreated());

    concept.getDescription()
      .setDe("Andere Beschreibung");
    concept.getDescription()
      .setEn("Another Description");

    // update the project
    mockMvc.perform(put(API_CONCEPT_URI + "/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)))
      .andExpect(status().is2xxSuccessful());

    // load the project with the complete projection
    mockMvc.perform(get(API_CONCEPT_URI + "/" + concept.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(concept.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.description.de", is("Andere Beschreibung")))
      .andExpect(jsonPath("$.description.en", is("Another Description")));
  }
}
