package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import static org.hamcrest.CoreMatchers.is;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * 
 * @author Daniel Katzberg
 *
 */
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DeleteAllPublicationsResourceControllerTest extends AbstractTest {
  private static final String API_RELATED_PUBLICATION_URI = "/api/related-publications";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private RelatedPublicationRepository publicationRepository;

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

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
    this.publicationRepository.deleteAll();
    this.dataPackageRepository.deleteAll();
    this.dataAcquisitionProjectRepository.deleteAll();
    this.elasticsearchUpdateQueueService.clearQueue();
    this.elasticsearchAdminService.recreateAllIndices();
    this.javersService.deleteAll();
  }

  @Test
  public void testCreateRelatedPublications() throws IOException, Exception {
    // ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackageRepository.save(dataPackage);
    RelatedPublication relatedPublication =
        UnitTestCreateDomainObjectUtils.buildRelatedPublication();

    // ACT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
        .content(TestUtil.convertObjectToJsonBytes(relatedPublication))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // delete all publication assignments of the data package
    this.mockMvc.perform(delete("/api/data-packages/" + dataPackage.getId() + "/publications"))
        .andExpect(status().isNoContent());

    // ensure that the publication assignment has been deleted
    this.mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.dataPackageIds.length()", is(0)));
  }
}
