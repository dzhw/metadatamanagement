package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DataAcquisitionProjectControllerTest extends AbstractTest {

    private static final String API_DATA_ACQUISITION_PROJECTS_URI = "/api/data-acquisition-projects";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private JaversService javersService;

    @Autowired
    private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
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
        DataAcquisitionProject shouldBeFound = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
        DataAcquisitionProject shouldNotBeFound = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
        shouldBeFound.setId("shouldnotbefoundid");

        dataAcquisitionProjectRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

        mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/findByIdLikeOrderByIdAsc").param("id", ""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.dataAcquisitionProjects.length()", equalTo(1)));
    }
}
