package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.time.LocalDateTime;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataCiteService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
public class DataCiteServiceTest extends AbstractTest {

//    @Autowired
//    private DataCiteService dataCiteService;
//
//    private DataPackageRepository dataPackageRepository;
//
//    @Autowired
//    private AnalysisPackageRepository analysisPackageRepository;

    private DataAcquisitionProject testProject;


    @BeforeEach
    public void setup() {
      testProject = this.generateTestProject();
//      this.dataPackageRepository = mock(DataPackageRepository.class);
//      when(this.dataPackageRepository.findByDataAcquisitionProjectId(testProject.getId()))
//        .thenReturn((List<DataPackage>) UnitTestCreateDomainObjectUtils.buildDataPackage(testProject.getId()));
    }

    @AfterEach
    public void cleanUp() {

    }

    @Test
    public void testGetAttrObj() {
      //todo: test method
    }

    private DataAcquisitionProject generateTestProject() {
      DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
      project.setId(project.getId() + "-1.0.0");
      Release release = new Release();
      release.setIsPreRelease(false);
      release.setDoiPageLanguage("de");
      release.setFirstDate(LocalDateTime.now());
      release.setLastDate(LocalDateTime.now());
      release.setVersion("1.0.0");
      release.setPinToStartPage(false);
      project.setRelease(release);
      return project;
    }
}
