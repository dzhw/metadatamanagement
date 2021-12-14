package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

/**
 *
 * @author Daniel Katzberg
 *
 */
@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class RelatedPublicationResourceControllerTest extends AbstractTest {
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

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
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

    // ASSERT
    // read the related publication under the new url
    this.mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
        .andExpect(status().isOk());

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one dataPackage and one publication document
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2L));
  }

  @Test
  public void testCreateRelatedPublicationsWithPost() throws IOException, Exception {
    // ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackageRepository.save(dataPackage);
    RelatedPublication relatedPublication =
        UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    // ACT
    // create the related publication with the given id
    this.mockMvc.perform(post(API_RELATED_PUBLICATION_URI)
        .content(TestUtil.convertObjectToJsonBytes(relatedPublication))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // ASSERT
    // read the related publication under the new url
    this.mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
        .andExpect(status().isOk());

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one dataPackage and one publication document
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2L));
  }

  @Test
  public void testCreateRelatedPublicationsWithoutAuthors() throws IOException, Exception {
    // ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackageRepository.save(dataPackage);
    RelatedPublication relatedPublication =
        UnitTestCreateDomainObjectUtils.buildRelatedPublication(null, 2017);

    // ACT
    this.mockMvc
        .perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
            .content(TestUtil.convertObjectToJsonBytes(relatedPublication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString(
            "related-publication-management.error.related-publication.authors.not-empty")));
  }

  @Test
  public void testCreateRelatedPublicationsWithInvalidYearInPast() throws IOException, Exception {
    // ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackageRepository.save(dataPackage);
    RelatedPublication relatedPublication =
        UnitTestCreateDomainObjectUtils.buildRelatedPublication("TestAuthors", 1959);

    // ACT
    this.mockMvc
        .perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
            .content(TestUtil.convertObjectToJsonBytes(relatedPublication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("related-publication-management.error.related-publication.year.valid")));
  }

  @Test
  public void testUpdateRelatedPublications() throws IOException, Exception {
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
    relatedPublication.setVersion(0L);
    relatedPublication.setDoi("Another DOI");

    // update the related publication with the given id
    mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
        .content(TestUtil.convertObjectToJsonBytes(relatedPublication))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // ASSERT
    // read the related publication under the new url
    mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(relatedPublication.getId())))
        .andExpect(jsonPath("$.doi", is("Another DOI")));

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there are is one dataPackage and one publication document
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2L));
  }

  @Test
  public void testDeleteRelatedPublications() throws IOException, Exception {
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

    // delete the related publication under the new url
    mockMvc.perform(delete(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
        .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
        .andExpect(status().isNotFound());

    // check that there is one dataPackage document left
    elasticsearchUpdateQueueService.processAllQueueItems();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));
  }

  @Test
  public void testUpdateDataPackageWithInvalidReleaseDoi() throws IOException, Exception {
    // ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackageRepository.save(dataPackage);
    RelatedPublication relatedPublication =
        UnitTestCreateDomainObjectUtils.buildRelatedPublication();


    relatedPublication.setDoi(
        "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong."
            + "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong."
            + "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong."
            + "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong."
            + "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong."
            + "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong."
            + "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong."
            + "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.");

    // ACT and ASSERT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
        .content(TestUtil.convertObjectToJsonBytes(relatedPublication))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
  }
}
