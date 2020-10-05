package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class PublicationAssignmentResourceControllerTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private RelatedPublicationRepository publicationRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @After
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    publicationRepository.deleteAll();
    dataPackageRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testAssignPublicationToDataPackage() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackageRepository.save(dataPackage);

    RelatedPublication publication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    publication.setDataPackageIds(null);
    publicationRepository.save(publication);

    // assign the data package to the publication
    mockMvc
        .perform(put(
            "/api/data-packages/" + dataPackage.getId() + "/publications/" + publication.getId()))
        .andExpect(status().isNoContent());

    // read the publication and check that it is assigned
    mockMvc.perform(get("/api/related-publications/" + publication.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.dataPackageIds[0]", is(dataPackage.getId())));

    // unassign the data package from the publication
    mockMvc
        .perform(delete(
            "/api/data-packages/" + dataPackage.getId() + "/publications/" + publication.getId()))
        .andExpect(status().isNoContent());

    // read the publication and check that it is not assigned anymore
    mockMvc.perform(get("/api/related-publications/" + publication.getId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.dataPackageIds.length()", is(0)));
  }
}
