package eu.dzhw.fdz.metadatamanagement.searchmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

public class SearchResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    UnitTestUserManagementUtils.login("admin", "admin");
    dataAcquisitionProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    variableRepository.deleteAll();
    UnitTestUserManagementUtils.logout();
  }

  @Test
  public void testRecreateAllIndices() throws Exception {
    UnitTestUserManagementUtils.login("admin", "admin");

    // test recreation of all elasticsearch indices
    mockMvc.perform(post("/api/search/recreate"))
      .andExpect(status().isOk());

    // test recreation of all elasticsearch indices (should delete the previously
    // created indices)
    mockMvc.perform(post("/api/search/recreate"))
      .andExpect(status().isOk());
  }

  @Test(expected = NestedServletException.class)
  public void testRecreateAllIndicesWithoutValidCredentials() throws Exception {
    // test recreation of all elasticsearch indices
    mockMvc.perform(post("/api/search/recreate"));
  }

  @Test
  public void testRecreateIndicesWithExistingVariablesAndSurveys() throws Exception {
    UnitTestUserManagementUtils.login("admin", "admin");
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<Integer>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variableRepository.save(variable);

    // test recreation of all elasticsearch indices with previously created variable
    mockMvc.perform(post("/api/search/recreate"))
      .andExpect(status().isOk());

    elasticsearchAdminService.refreshAllIndices();

    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1.0));
  }
}
