package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.rest.filter.LegacyUrlsFilter;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class DataPackageResourceControllerTest extends AbstractTest {
  private static final String API_DATAPACKAGE_URI = "/api/data-packages";
  private static final String API_LEGACY_URI = "/api/studies";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  @Autowired
  private LegacyUrlsFilter legacyUrlsFilter;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(legacyUrlsFilter).build();
  }

  @AfterEach
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    dataPackageRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateDataPackage() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());

    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // read the dataPackage under the new url
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getId()))
        .andExpect(status().isOk());

    // read the dataPackage under the legacy url
    mockMvc.perform(get(API_LEGACY_URI + "/" + dataPackage.getId())).andExpect(status().isOk())
        .andExpect(forwardedUrl(API_DATAPACKAGE_URI + "/" + dataPackage.getId()));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateDataPackageWithPost() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());

    // create the dataPackage with the given id
    mockMvc
        .perform(post(API_DATAPACKAGE_URI).content(TestUtil.convertObjectToJsonBytes(dataPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    // read the dataPackage under the new url
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getId()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateHiddenDataPackage() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackage.setHidden(true);

    // create the hidden dataPackage with the given id
    mockMvc
        .perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("invalid-hidden-shadow")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateDataPackageWithTooStudySeries() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackage.setStudySeries(I18nString.builder().de(
        "Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn"
            + "Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn"
            + "Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewg"
            + "Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn")
        .en("Randomstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn")
        .build());

    // create the project with the given id
    mockMvc
        .perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataPackage)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateDataPackageWithWrongId() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackage.setId("hurz");

    // create the dataPackage with the given id
    mockMvc
        .perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataPackage)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateDataPackage() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());

    // create the dataPackage with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    List<Person> projectContributors = new ArrayList<>();
    projectContributors
        .add(UnitTestCreateDomainObjectUtils.buildPerson("Another", null, "ProjectContributors"));
    dataPackage.setProjectContributors(projectContributors);

    dataPackage.setVersion(0L);
    // update the dataPackage with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // read the dataPackage under the new url
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getId())).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(dataPackage.getId())))
        .andExpect(jsonPath("$.projectContributors[0].firstName", is("Another")))
        .andExpect(jsonPath("$.projectContributors[0].lastName", is("ProjectContributors")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteDataPackage() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());

    // create the project with the given id
    mockMvc.perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc.perform(delete(API_DATAPACKAGE_URI + "/" + dataPackage.getId()))
        .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateDataPackageWithWrongId() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackage.setId("ThisIdIsWrong.");

    // Try to put into mongo db
    mockMvc
        .perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataPackage)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyDataPackage() throws Exception {
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage("issue1991");
    dataPackage.setId(dataPackage.getId() + "-1.0.0");

    mockMvc
        .perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateShadowCopyDataPackage() throws Exception {
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage("issue1991");
    dataPackage.setId(dataPackage.getId() + "-1.0.0");
    dataPackageRepository.save(dataPackage);

    mockMvc
        .perform(put(API_DATAPACKAGE_URI + "/" + dataPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteShadowCopyDataPackage() throws Exception {
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage("issue1991");
    dataPackage.setId(dataPackage.getId() + "-1.0.0");
    dataPackageRepository.save(dataPackage);

    mockMvc.perform(delete(API_DATAPACKAGE_URI + "/" + dataPackage.getId()))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  public void getLatestShadow() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project = dataAcquisitionProjectRepository.save(project);
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    dataPackage = dataPackageRepository.save(dataPackage);

    elasticsearchAdminService.recreateAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2L));

    // since there is no shadow yet the public user will get the mongo version
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getId())).andExpect(status().isOk())
        .andExpect(jsonPath("$.completeTitle").doesNotExist());

    // now fake a shadow
    project.setId(project.getId() + "-1.0.0");
    project.setVersion(null);
    project.setRelease(new Release("1.0.0", LocalDateTime.now(), null, false, null, false));
    project = dataAcquisitionProjectRepository.save(project);
    dataPackage.setId(dataPackage.getId() + "-1.0.0");
    dataPackage.setDataAcquisitionProjectId(project.getId());
    dataPackage.setVersion(null);
    dataPackage = dataPackageRepository.save(dataPackage);

    elasticsearchAdminService.recreateAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(4L));

    // the public user should now get the latest shadow from elastic
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getMasterId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(dataPackage.getId())))
        .andExpect(jsonPath("$.masterId", equalTo(dataPackage.getMasterId())))
        .andExpect(jsonPath("$.release.version", equalTo("1.0.0")))
        .andExpect(jsonPath("$.completeTitle").exists()).andExpect(jsonPath("$.doi").exists());

    // the public user should now get the latest shadow from elastic
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getId())).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(dataPackage.getId())))
        .andExpect(jsonPath("$.masterId", equalTo(dataPackage.getMasterId())))
        .andExpect(jsonPath("$.release.version", equalTo("1.0.0")))
        .andExpect(jsonPath("$.completeTitle").exists()).andExpect(jsonPath("$.doi").exists());

    // set successor of old shadows
    project.setSuccessorId(project.getMasterId() + "-2.0.0");
    project = dataAcquisitionProjectRepository.save(project);
    dataPackage.setSuccessorId(dataPackage.getMasterId() + "-2.0.0");
    dataPackage = dataPackageRepository.save(dataPackage);

    // now fake a second shadow
    project.setId(project.getMasterId() + "-2.0.0");
    project.setSuccessorId(null);
    project.setVersion(null);
    project.setRelease(new Release("2.0.0", LocalDateTime.now(), null, false, null, false));
    project = dataAcquisitionProjectRepository.save(project);
    dataPackage.setId(dataPackage.getMasterId() + "-2.0.0");
    dataPackage.setSuccessorId(null);
    dataPackage.setDataAcquisitionProjectId(project.getId());
    dataPackage.setVersion(null);
    dataPackage = dataPackageRepository.save(dataPackage);

    elasticsearchAdminService.recreateAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(6L));

    // the public user should now get the latest shadow from elastic
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getMasterId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(dataPackage.getId())))
        .andExpect(jsonPath("$.masterId", equalTo(dataPackage.getMasterId())))
        .andExpect(jsonPath("$.completeTitle").exists())
        .andExpect(jsonPath("$.release.version", equalTo("2.0.0")))
        .andExpect(jsonPath("$.doi").exists());

    // the public user could also get the previous shadow from elastic
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getMasterId() + "-1.0.0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(dataPackage.getMasterId() + "-1.0.0")))
        .andExpect(jsonPath("$.masterId", equalTo(dataPackage.getMasterId())))
        .andExpect(jsonPath("$.completeTitle").exists())
        .andExpect(jsonPath("$.release.version", equalTo("1.0.0")))
        .andExpect(jsonPath("$.doi").exists());

    // now hide the previous shadow
    DataPackage outdatedShadow =
        dataPackageRepository.findById(dataPackage.getMasterId() + "-1.0.0").get();
    outdatedShadow.setHidden(true);
    dataPackageRepository.save(outdatedShadow);
    elasticsearchAdminService.recreateAllIndices();

    // assert that the public user cannot access the previous shadow anymore
    mockMvc.perform(get(API_DATAPACKAGE_URI + "/" + dataPackage.getMasterId() + "-1.0.0"))
        .andExpect(status().isNotFound());
  }
}
