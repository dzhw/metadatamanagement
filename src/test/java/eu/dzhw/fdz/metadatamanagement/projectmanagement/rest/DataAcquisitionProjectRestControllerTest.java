package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserAuthenticationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DataAcquisitionProjectRestControllerTest extends AbstractTest {
  private static final String API_DATA_ACQUISITION_PROJECTS_URI = "/api/data-acquisition-projects/search";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private JaversService javersService;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @MockBean
  private UserAuthenticationProvider userAuthenticationProvider;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    javersService.deleteAll();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testGetDataAcquisitionProjectList() throws Exception {
    UserDetails userDetails = UnitTestCreateDomainObjectUtils.buildUserWithDataProviderRole();
    Mockito.when(userAuthenticationProvider.getUserDetails()).thenReturn(userDetails);

    Configuration shouldBeFoundConfiguration = new Configuration();
    shouldBeFoundConfiguration.setDataProviders(Collections.singletonList(userDetails.getUsername()));
    shouldBeFoundConfiguration.setPublishers(Collections.singletonList("aPublisherId"));

    DataAcquisitionProject shouldBeFound = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    shouldBeFound.setId("testid");
    shouldBeFound.setConfiguration(shouldBeFoundConfiguration);

    Configuration shouldNotBeFoundConfiguration = new Configuration();
    shouldNotBeFoundConfiguration.setDataProviders(Collections.singletonList("anotherProviderId"));
    shouldNotBeFoundConfiguration.setPublishers(Collections.singletonList("bPublisherId"));

    DataAcquisitionProject shouldNotBeFound = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    shouldNotBeFound.setId("shouldnotbefoundid");
    shouldNotBeFound.setConfiguration(shouldNotBeFoundConfiguration);

    dataAcquisitionProjectRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/findByIdLikeOrderByIdAsc").param("id", "TES"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", equalTo(1)));
  }
}
