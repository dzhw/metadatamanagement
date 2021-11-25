package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.rest;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.javers.common.collections.Lists;
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
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.CustomDataPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ExternalDataPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;

public class AnalysisPackageResourceControllerTest extends AbstractTest {
  private static final String API_ANALYSISPACKAGE_URI = "/api/analysis-packages";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private AnalysisPackageRepository analysisPackageRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

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
    dataAcquisitionProjectRepository.deleteAll();
    analysisPackageRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateAnalysisPackage() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());

    // create the analysis package with the given id
    mockMvc.perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // read the analysis package under the new url
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldFailWithMissingDataPackage() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    DataPackage referencedDataPackage = DataPackage.builder().dataPackageMasterId("stu-gra2005$")
        .accessWay(AccessWays.DOWNLOAD_CUF).version("1.0.0").build();
    analysisPackage.setAnalysisDataPackages(List.of(referencedDataPackage));

    // create the analysis package with the given id
    mockMvc
        .perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("invalid-shadow")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldFailWithWrongIdPattern() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    analysisPackage.setId("stu-" + project.getId() + "$");

    mockMvc
        .perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            is("analysis-package-management.error.analysis-package.id.pattern")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateAnalysisPackageWithPost() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());

    // create the dataPackage with the given id
    mockMvc.perform(
        post(API_ANALYSISPACKAGE_URI).content(TestUtil.convertObjectToJsonBytes(analysisPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    // read the dataPackage under the new url
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateHiddenAnalysisPackage() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    analysisPackage.setHidden(true);

    // create the hidden analysis package with the given id
    mockMvc
        .perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("invalid-hidden-shadow")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateAnalysisPackageWithWrongId() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    analysisPackage.setId("hurz");

    // create the analysis package with the given id
    mockMvc
        .perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(analysisPackage)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateAnalysisPackage() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());

    // create the analysis package with the given id
    mockMvc.perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    List<Person> authors = new ArrayList<>();
    authors.add(UnitTestCreateDomainObjectUtils.buildPerson("Another", null, "Authors"));
    analysisPackage.setAuthors(authors);

    analysisPackage.setVersion(0L);
    // update the analysis package with the given id
    mockMvc.perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // read the analysis package under the new url
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(analysisPackage.getId())))
        .andExpect(jsonPath("$.authors[0].firstName", is("Another")))
        .andExpect(jsonPath("$.authors[0].lastName", is("Authors")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAnalysisPackage() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());

    // create the analysis package with the given id
    mockMvc.perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // delete the analysis package under the new url
    mockMvc.perform(delete(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId()))
        .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateAnalysisPackageWithWrongId() throws IOException, Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    analysisPackage.setId("ThisIdIsWrong.");

    // Try to put into mongo db
    mockMvc
        .perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(analysisPackage)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyAnalysisPackage() throws Exception {
    AnalysisPackage analysisPackage = UnitTestCreateDomainObjectUtils.buildAnalysisPackage("egal");
    analysisPackage.setId(analysisPackage.getId() + "-1.0.0");

    mockMvc
        .perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateShadowCopyAnalysisPackage() throws Exception {
    AnalysisPackage analysisPackage = UnitTestCreateDomainObjectUtils.buildAnalysisPackage("egal");
    analysisPackage.setId(analysisPackage.getId() + "-1.0.0");
    analysisPackageRepository.save(analysisPackage);

    mockMvc
        .perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
            .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteShadowCopyAnalysisPackage() throws Exception {
    AnalysisPackage analysisPackage = UnitTestCreateDomainObjectUtils.buildAnalysisPackage("egal");
    analysisPackage.setId(analysisPackage.getId() + "-1.0.0");
    analysisPackageRepository.save(analysisPackage);

    mockMvc.perform(delete(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId()))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  public void getLatestShadow() throws Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    analysisPackage = analysisPackageRepository.save(analysisPackage);

    elasticsearchAdminService.recreateAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));

    // since there is no shadow yet the public user will get the mongo version
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.completeTitle").doesNotExist());

    // now fake a shadow
    project.setId(project.getId() + "-1.0.0");
    project.setVersion(null);
    project.setRelease(new Release("1.0.0", LocalDateTime.now(), null, false));
    project = dataAcquisitionProjectRepository.save(project);
    analysisPackage.setId(analysisPackage.getId() + "-1.0.0");
    analysisPackage.setDataAcquisitionProjectId(project.getId());
    analysisPackage.setVersion(null);
    analysisPackage = analysisPackageRepository.save(analysisPackage);

    elasticsearchAdminService.recreateAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2L));

    // the public user should now get the latest shadow from elastic
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getMasterId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(analysisPackage.getId())))
        .andExpect(jsonPath("$.masterId", equalTo(analysisPackage.getMasterId())))
        .andExpect(jsonPath("$.release.version", equalTo("1.0.0")))
        .andExpect(jsonPath("$.completeTitle").exists()).andExpect(jsonPath("$.doi").exists());

    // the public user should now get the latest shadow from elastic
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(analysisPackage.getId())))
        .andExpect(jsonPath("$.masterId", equalTo(analysisPackage.getMasterId())))
        .andExpect(jsonPath("$.release.version", equalTo("1.0.0")))
        .andExpect(jsonPath("$.completeTitle").exists()).andExpect(jsonPath("$.doi").exists());

    // set successor of old shadows
    project.setSuccessorId(project.getMasterId() + "-2.0.0");
    project = dataAcquisitionProjectRepository.save(project);
    analysisPackage.setSuccessorId(analysisPackage.getMasterId() + "-2.0.0");
    analysisPackage = analysisPackageRepository.save(analysisPackage);

    // now fake a second shadow
    project.setId(project.getMasterId() + "-2.0.0");
    project.setSuccessorId(null);
    project.setVersion(null);
    project.setRelease(new Release("2.0.0", LocalDateTime.now(), null, false));
    project = dataAcquisitionProjectRepository.save(project);
    analysisPackage.setId(analysisPackage.getMasterId() + "-2.0.0");
    analysisPackage.setSuccessorId(null);
    analysisPackage.setDataAcquisitionProjectId(project.getId());
    analysisPackage.setVersion(null);
    analysisPackage = analysisPackageRepository.save(analysisPackage);

    elasticsearchAdminService.recreateAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(3L));

    // the public user should now get the latest shadow from elastic
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getMasterId()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(analysisPackage.getId())))
        .andExpect(jsonPath("$.masterId", equalTo(analysisPackage.getMasterId())))
        .andExpect(jsonPath("$.completeTitle").exists())
        .andExpect(jsonPath("$.release.version", equalTo("2.0.0")))
        .andExpect(jsonPath("$.doi").exists());

    // the public user could also get the previous shadow from elastic
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getMasterId() + "-1.0.0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(analysisPackage.getMasterId() + "-1.0.0")))
        .andExpect(jsonPath("$.masterId", equalTo(analysisPackage.getMasterId())))
        .andExpect(jsonPath("$.completeTitle").exists())
        .andExpect(jsonPath("$.release.version", equalTo("1.0.0")))
        .andExpect(jsonPath("$.doi").exists());

    // now hide the previous shadow
    AnalysisPackage outdatedShadow =
        analysisPackageRepository.findById(analysisPackage.getMasterId() + "-1.0.0").get();
    outdatedShadow.setHidden(true);
    analysisPackageRepository.save(outdatedShadow);
    elasticsearchAdminService.recreateAllIndices();

    // assert that the public user cannot access the previous shadow anymore
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getMasterId() + "-1.0.0"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldSaveAnalysisPackageWithDifferentDataPackageTypes() throws Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    dataAcquisitionProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    ExternalDataPackage externalDataPackage =
        ExternalDataPackage.builder().title(new I18nString("titel", "title"))
            .description(new I18nString("Beschreibung", "Description"))
            .dataSource(new I18nString("Datenquelle", "Data Source"))
            .availabilityType(ExternalDataPackage.AVAILABLE_AVAILABILITY_TYPES.get(0)).build();
    CustomDataPackage customDataPackage =
        CustomDataPackage.builder().title(new I18nString("titel", "title"))
            .description(new I18nString("Beschreibung", "Description"))
            .availabilityType(CustomDataPackage.AVAILABLE_AVAILABILITY_TYPES.get(0))
            .accessWay(CustomDataPackage.AVAILABLE_ACCESS_WAYS.get(0)).build();
    analysisPackage.setAnalysisDataPackages(Lists.asList(customDataPackage, externalDataPackage));
    
    // create the analysis package with the given id
    mockMvc.perform(put(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // read the analysis package under the new url
    mockMvc.perform(get(API_ANALYSISPACKAGE_URI + "/" + analysisPackage.getId()))
        .andExpect(status().isOk());
  }
}
