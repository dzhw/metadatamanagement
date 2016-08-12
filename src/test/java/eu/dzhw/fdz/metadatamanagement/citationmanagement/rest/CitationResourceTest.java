/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.citationmanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.citationmanagement.domain.Citation;
import eu.dzhw.fdz.metadatamanagement.citationmanagement.repository.CitationRepository;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;

/**
 * @author Daniel Katzberg
 *
 */
public class CitationResourceTest extends AbstractTest {
  private static final String API_CITATION_URI =
      "/api/citations";


  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private CitationRepository citationRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.citationRepository.deleteAll();
  }

  @Test
  public void testCreateDataAcquisitionProject() throws IOException, Exception {
    // Arrange
    Citation citation =
        UnitTestCreateDomainObjectUtils.buildCitation();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_CITATION_URI + "/" + citation.getId())
      .content(TestUtil.convertObjectToJsonBytes(citation)))
      .andExpect(status().isCreated());

    // read the project under the new url
    mockMvc
      .perform(get(API_CITATION_URI + "/" + citation.getId()))
      .andExpect(status().isOk());

    // call toString for test coverage :-)
    citation.toString();
  }

  @Test
  public void testDeleteDataAcquisitionProject() throws IOException, Exception {
    // Arrange
    Citation citation =
        UnitTestCreateDomainObjectUtils.buildCitation();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_CITATION_URI + "/" + citation.getId())
      .content(TestUtil.convertObjectToJsonBytes(citation)))
      .andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc
      .perform(delete(API_CITATION_URI + "/" + citation.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc
      .perform(get(API_CITATION_URI + "/" + citation.getId()))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testCompleteProjectionContainsId() throws IOException, Exception {
    // Arrange
    Citation citation =
        UnitTestCreateDomainObjectUtils.buildCitation();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_CITATION_URI + "/" + citation.getId())
      .content(TestUtil.convertObjectToJsonBytes(citation)))
      .andExpect(status().isCreated());

    // load the project with the projection
    mockMvc.perform(get(API_CITATION_URI + "/" + citation.getId()
        + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(citation.getId())))
      .andExpect(jsonPath("$.version", is(0)));
  }

  @Test
  public void testUpdateProject() throws IOException, Exception {
    // Arrange
    Citation citation =
        UnitTestCreateDomainObjectUtils.buildCitation();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_CITATION_URI + "/" + citation.getId())
      .content(TestUtil.convertObjectToJsonBytes(citation)))
      .andExpect(status().isCreated());

    citation.setBookTitle("Another Booktitle");
    citation.setChapter("Another Chapter");

    // update the project
    mockMvc.perform(put(API_CITATION_URI + "/" + citation.getId())
      .content(TestUtil.convertObjectToJsonBytes(citation)))
      .andExpect(status().is2xxSuccessful());

    // load the project with the complete projection
    mockMvc.perform(get(API_CITATION_URI + "/" + citation.getId()
        + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(citation.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.bookTitle", is("Another Booktitle")))
      .andExpect(jsonPath("$.chapter", is("Another Chapter")));
  }
}
