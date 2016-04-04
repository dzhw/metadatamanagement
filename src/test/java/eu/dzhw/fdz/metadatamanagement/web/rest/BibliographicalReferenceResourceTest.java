/**
 * 
 */
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
import eu.dzhw.fdz.metadatamanagement.domain.BibliographicalReference;
import eu.dzhw.fdz.metadatamanagement.repository.BibliographicalReferenceRepository;

/**
 * @author Daniel Katzberg
 *
 */
public class BibliographicalReferenceResourceTest extends AbstractTest {
  private static final String API_BIBLIOGRAPHICAL_REFERENCES_URI =
      "/api/bibliographical_references";


  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private BibliographicalReferenceRepository bibliographicalReferenceRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.bibliographicalReferenceRepository.deleteAll();
  }

  @Test
  public void testCreateDataAcquisitionProject() throws IOException, Exception {
    // Arrange
    BibliographicalReference bibliographicalReference =
        UnitTestCreateDomainObjectUtils.buildBibliographicalReference();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId())
      .content(TestUtil.convertObjectToJsonBytes(bibliographicalReference)))
      .andExpect(status().isCreated());

    // read the project under the new url
    mockMvc
      .perform(get(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId()))
      .andExpect(status().isOk());

    // call toString for test coverage :-)
    bibliographicalReference.toString();
  }

  @Test
  public void testDeleteDataAcquisitionProject() throws IOException, Exception {
    // Arrange
    BibliographicalReference bibliographicalReference =
        UnitTestCreateDomainObjectUtils.buildBibliographicalReference();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId())
      .content(TestUtil.convertObjectToJsonBytes(bibliographicalReference)))
      .andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc
      .perform(delete(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc
      .perform(get(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId()))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testCompleteProjectionContainsId() throws IOException, Exception {
    // Arrange
    BibliographicalReference bibliographicalReference =
        UnitTestCreateDomainObjectUtils.buildBibliographicalReference();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId())
      .content(TestUtil.convertObjectToJsonBytes(bibliographicalReference)))
      .andExpect(status().isCreated());

    // load the project with the projection
    mockMvc.perform(get(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId()
        + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(bibliographicalReference.getId())))
      .andExpect(jsonPath("$.version", is(0)));
  }

  @Test
  public void testUpdateProject() throws IOException, Exception {
    // Arrange
    BibliographicalReference bibliographicalReference =
        UnitTestCreateDomainObjectUtils.buildBibliographicalReference();

    // Act and Assert
    // create the project with the given id
    mockMvc.perform(put(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId())
      .content(TestUtil.convertObjectToJsonBytes(bibliographicalReference)))
      .andExpect(status().isCreated());

    bibliographicalReference.setBookTitle("Another Booktitle");
    bibliographicalReference.setChapter("Another Chapter");

    // update the project
    mockMvc.perform(put(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId())
      .content(TestUtil.convertObjectToJsonBytes(bibliographicalReference)))
      .andExpect(status().is2xxSuccessful());

    // load the project with the complete projection
    mockMvc.perform(get(API_BIBLIOGRAPHICAL_REFERENCES_URI + "/" + bibliographicalReference.getId()
        + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(bibliographicalReference.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.bookTitle", is("Another Booktitle")))
      .andExpect(jsonPath("$.chapter", is("Another Chapter")));
  }
}
