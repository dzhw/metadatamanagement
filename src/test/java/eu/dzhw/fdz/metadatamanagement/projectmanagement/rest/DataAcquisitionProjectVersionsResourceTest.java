package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class DataAcquisitionProjectVersionsResourceTest extends AbstractTest {
  private static final String API_DATA_ACQUISITION_PROJECTS_URI = "/api/data-acquisition-projects";
  
  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;
    
  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  
  @Autowired
  private JaversService javersService;
  
  @Autowired
  private DataAcquisitionProjectVersionsService versionsService;
  
  private MockMvc mockMvc;
  
  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }
  
  @After
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    javersService.deleteAll();
  }
  
  @Test
  public void testCreateProjectAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    
    // create the study with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    // read the study versions
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(1))))
      .andExpect(jsonPath("$[0].id", is(project.getId())));
  }
  
  @Test
  public void testEditProjectAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
            
    // create the study with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());
    
    // update the study with the given id
    project.setHasBeenReleasedBefore(true);
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isNoContent());
    
    // update the study again with the given id
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isNoContent());

    // read the study versions
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(3))))
      .andExpect(jsonPath("$[0].id", is(project.getId())))
      .andExpect(jsonPath("$[0].hasBeenReleasedBefore", is(true)))
      .andExpect(jsonPath("$[0].version", is(equalTo(2))))
      .andExpect(jsonPath("$[0].release.version", is("1.0.0"))) 
      .andExpect(jsonPath("$[1].id", is(project.getId())))
      .andExpect(jsonPath("$[1].hasBeenReleasedBefore", is(true)))
      .andExpect(jsonPath("$[1].version", is(equalTo(1))))
      //.andExpect(jsonPath("$[1].release", isNull()))
      .andExpect(jsonPath("$[2].id", is(project.getId())))
      .andExpect(jsonPath("$[2].version", is(equalTo(0))))
      .andExpect(jsonPath("$[2].hasBeenReleasedBefore", is(false)));
      //.andExpect(jsonPath("$[2].release", isNull()));
  }
  
  @Test
  public void testReleaseCompare() throws IOException, Exception {
    //Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    Javers javers = JaversBuilder.javers().build();
    
    //Act
    //Save first time with Release    
    project.setHasBeenReleasedBefore(true);
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    this.mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()));
    
    //Save second time without Release (simulates unrelease)
    project.setRelease(null);
    this.mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()));
    
    //Save third time with new release and a higher version
    project.setRelease(UnitTestCreateDomainObjectUtils.buildRelease());
    project.getRelease().setVersion("1.0.1");
    this.mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()));
    
    //Get the information about 1.0.0
    this.versionsService.findLastChange(project.getId(), "hasBeenReleasedBefore");
    
    //Assert    
  }
}
