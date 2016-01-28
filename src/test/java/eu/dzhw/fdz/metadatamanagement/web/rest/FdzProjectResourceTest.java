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
import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.builders.FdzProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;

public class FdzProjectResourceTest extends AbstractTest {
  private static final String API_FDZ_PROJECTS_URI = "/api/fdz_projects";

  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private FdzProjectRepository fdzProjectRepository;
  
  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }
  
  @After
  public void cleanUp() {
    fdzProjectRepository.deleteAll();
  }
  
  @Test
  public void testCreateFdzProject() throws IOException, Exception {
    FdzProject project = new FdzProjectBuilder().withId("testId").withCufDoi("testDoi").withSufDoi("testDoi").build(); 
    //create the project with the given id
    mockMvc.perform(put(API_FDZ_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().is2xxSuccessful());
    
    //read the project under the new url
    mockMvc.perform(get(API_FDZ_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().isOk());
  }
  
  @Test
  public void testDeleteFdzProject() throws IOException, Exception {
    FdzProject project = new FdzProjectBuilder().withId("testId").withCufDoi("testDoi").withSufDoi("testDoi").build(); 
    //create the project with the given id
    mockMvc.perform(put(API_FDZ_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().is2xxSuccessful());
    
    //delete the project under the new url
    mockMvc.perform(delete(API_FDZ_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().is2xxSuccessful());
    
    //ensure it is really deleted
    mockMvc.perform(get(API_FDZ_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().isNotFound());
  }
  
  @Test
  public void testCompleteProjectionContainsId() throws IOException, Exception {
    FdzProject project = new FdzProjectBuilder().withId("testId").withCufDoi("testDoi").withSufDoi("testDoi").build(); 
    //create the project with the given id
    mockMvc.perform(put(API_FDZ_PROJECTS_URI + "/" + project.getId())
        .content(TestUtil.convertObjectToJsonBytes(project)))
        .andExpect(status().is2xxSuccessful());
    
    //load the project with the projection
    mockMvc.perform(get(API_FDZ_PROJECTS_URI + "/" + project.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId())))
      .andExpect(jsonPath("$.version", is(0)));
  }
}
