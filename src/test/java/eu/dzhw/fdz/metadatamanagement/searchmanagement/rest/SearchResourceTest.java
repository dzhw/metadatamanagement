package eu.dzhw.fdz.metadatamanagement.searchmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
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

  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @AfterEach
  @WithMockUser(username = "admin", authorities = AuthoritiesConstants.ADMIN)
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    variableRepository.deleteAll();
    javersService.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    SecurityContextHolder.clearContext();
  }

  @Test
  @WithMockUser(username = "admin", authorities = AuthoritiesConstants.ADMIN)
  public void testRecreateAllIndices() throws Exception {
    // test recreation of all elasticsearch indices
    mockMvc.perform(post("/api/search/recreate"))
      .andExpect(status().isOk());

    // test recreation of all elasticsearch indices (should delete the previously
    // created indices)
    mockMvc.perform(post("/api/search/recreate"))
      .andExpect(status().isOk());
  }

  @Test
  public void testRecreateAllIndicesWithoutValidCredentials() throws Exception {
    Assertions.assertThrows(NestedServletException.class, () -> {
    	// test recreation of all elasticsearch indices
    	mockMvc.perform(post("/api/search/recreate"));
    });
  }

  @Test
  @WithMockUser(username = "admin", authorities = AuthoritiesConstants.ADMIN)
  public void testRecreateIndicesWithExistingVariablesAndSurveys() throws Exception {
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

    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));
  }
}
