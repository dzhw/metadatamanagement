package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DoiBuilder;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
class DataCiteServiceTest extends AbstractTest {

  @Autowired
  private DataCiteService dataCiteService;

  private DataAcquisitionProject testProject;

  private DataPackageRepository dataPackageRepository;

  private AnalysisPackageRepository analysisPackageRepository;

  private SurveyRepository surveyRepository;

  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  private DoiBuilder doiBuilder;

  @BeforeEach
  public void setup() {
    testProject = this.generateTestProject();
    dataPackageRepository = mock(DataPackageRepository.class);
    analysisPackageRepository = mock(AnalysisPackageRepository.class);
    surveyRepository = mock(SurveyRepository.class);
    dataAcquisitionProjectRepository = mock(DataAcquisitionProjectRepository.class);
    dataAcquisitionProjectVersionsService = mock(DataAcquisitionProjectVersionsService.class);
    doiBuilder = mock(DoiBuilder.class);
    List<DataPackage> list = new ArrayList<>();
    list.add(UnitTestCreateDomainObjectUtils.buildDataPackage(testProject.getId()));
    when(dataPackageRepository.findByDataAcquisitionProjectId(testProject.getId()))
      .thenReturn(list);
  }

  @Test
  public void createAttrObjectForDataPackage_missingDataThrowsException() {
    DataPackage dpTitleNull = UnitTestCreateDomainObjectUtils.buildDataPackage(testProject.getId());
    dpTitleNull.setTitle(null);
    DataCiteMetadataException exception = assertThrows(DataCiteMetadataException.class,
      () -> dataCiteService.createAttrObjectForDataPackage(
        testProject,
        dpTitleNull,
        new ArrayList<>()), "Expected createAttrObjectForDataPackage() to throw DataCiteException, but it didn't");
    assertEquals("Error creating DataCite metadata. Project or data package is missing relevant fields.", exception.getMessage());

    DataPackage dpContributorsNull = UnitTestCreateDomainObjectUtils.buildDataPackage(testProject.getId());
    dpContributorsNull.setProjectContributors(null);
    exception = assertThrows(DataCiteMetadataException.class,
      () -> dataCiteService.createAttrObjectForDataPackage(
        testProject,
        dpContributorsNull,
        new ArrayList<>()), "Expected createAttrObjectForDataPackage() to throw DataCiteException, but it didn't");
    assertEquals("Error creating DataCite metadata. Project or data package is missing relevant fields.", exception.getMessage());

    DataPackage dpEmptyContributors = UnitTestCreateDomainObjectUtils.buildDataPackage(testProject.getId());
    dpEmptyContributors.setProjectContributors(new ArrayList<>());
    exception = assertThrows(DataCiteMetadataException.class,
      () -> dataCiteService.createAttrObjectForDataPackage(
        testProject,
        dpEmptyContributors,
        new ArrayList<>()), "Expected createAttrObjectForDataPackage() to throw DataCiteException, but it didn't");
    assertEquals("Error creating DataCite metadata. Project or data package is missing relevant fields.", exception.getMessage());

    DataPackage dp = UnitTestCreateDomainObjectUtils.buildDataPackage(testProject.getId());
    testProject.setRelease(null);
    exception = assertThrows(DataCiteMetadataException.class,
      () -> dataCiteService.createAttrObjectForDataPackage(
        testProject,
        dp,
        new ArrayList<>()), "Expected createAttrObjectForDataPackage() to throw DataCiteException, but it didn't");
    assertEquals("Error creating DataCite metadata. Project or data package is missing relevant fields.", exception.getMessage());
  }

  @Test
  public void createAttrObjectForAnalysisPackage_missingDataThrowsException() {
    AnalysisPackage apTitleNull = UnitTestCreateDomainObjectUtils.buildAnalysisPackage(testProject.getId());
    apTitleNull.setTitle(null);
    DataCiteMetadataException exception = assertThrows(DataCiteMetadataException.class,
      () -> dataCiteService.createAttrObjectForAnalysisPackage(
        testProject,
        apTitleNull), "Expected createAttrObjectForAnalysisPackage() to throw DataCiteException, but it didn't");
    assertEquals("Error creating DataCite metadata. Project or analysis package is missing relevant fields.", exception.getMessage());

    AnalysisPackage apAuthorsNull = UnitTestCreateDomainObjectUtils.buildAnalysisPackage(testProject.getId());
    apAuthorsNull.setAuthors(null);
    exception = assertThrows(DataCiteMetadataException.class,
      () -> dataCiteService.createAttrObjectForAnalysisPackage(
        testProject,
        apAuthorsNull), "Expected createAttrObjectForAnalysisPackage() to throw DataCiteException, but it didn't");
    assertEquals("Error creating DataCite metadata. Project or analysis package is missing relevant fields.", exception.getMessage());

    AnalysisPackage apAuthorsEmpty = UnitTestCreateDomainObjectUtils.buildAnalysisPackage(testProject.getId());
    apAuthorsEmpty.setAuthors(new ArrayList<>());
    exception = assertThrows(DataCiteMetadataException.class,
      () -> dataCiteService.createAttrObjectForAnalysisPackage(
        testProject,
        apAuthorsEmpty), "Expected createAttrObjectForAnalysisPackage() to throw DataCiteException, but it didn't");
    assertEquals("Error creating DataCite metadata. Project or analysis package is missing relevant fields.", exception.getMessage());

    AnalysisPackage ap = UnitTestCreateDomainObjectUtils.buildAnalysisPackage(testProject.getId());
    testProject.setRelease(null);
    exception = assertThrows(DataCiteMetadataException.class,
      () -> dataCiteService.createAttrObjectForAnalysisPackage(
        testProject,
        ap), "Expected createAttrObjectForAnalysisPackage() to throw DataCiteException, but it didn't");
    assertEquals("Error creating DataCite metadata. Project or analysis package is missing relevant fields.", exception.getMessage());
  }

  @Test
  public void createAttrObjectForDataPackage_releasedProjectNoPreviousVersion() throws DataCiteMetadataException {
    DataPackage dp = UnitTestCreateDomainObjectUtils.buildDataPackage(testProject.getId());
    Map<String, Object> result = dataCiteService.createAttrObjectForDataPackage(
      testProject,
      dp,
      new ArrayList<>());
    assertTrue(result != null);
    assertEquals(this.getExpectedTitlesDeAndEn(dp), result.get("titles"));
    assertEquals(testProject.getRelease().getFirstDate().getYear(), result.get("publicationYear"));
    assertEquals(testProject.getRelease().getVersion(), result.get("version"));
    assertEquals(2, ((List<Map<String, Object>>) result.get("creators")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("contributors")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("fundingReferences")).size());
    assertEquals(3, ((List<Map<String, Object>>) result.get("subjects")).size());
    assertEquals(1, ((List<Map<String, Object>>) result.get("dates")).size());

    Map<String, Object> resultDate =  ((List<Map<String, Object>>) result.get("dates")).get(0);
    assertEquals("Available", resultDate.get("dateType"));
    assertNull(resultDate.get("dateInformation"));

    assertEquals(0, ((List<Map<String, Object>>) result.get("geoLocations")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("alternateIdentifiers")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("relatedIdentifiers")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("rightsList")).size());
  }

  @Test
  public void createAttrObjectForAnalysisPackage_releasedProjectNoPreviousVersion() throws DataCiteMetadataException {
    AnalysisPackage ap = UnitTestCreateDomainObjectUtils.buildAnalysisPackage(testProject.getId());
    Map<String, Object> result = dataCiteService.createAttrObjectForAnalysisPackage(
      testProject,
      ap);
    assertTrue(result != null);
    List<Map<String, String>>  expectedTitles= new ArrayList<>();
    Map<String,String> titleObjDe = new HashMap<>();
    titleObjDe.put("title", ap.getTitle().getDe());
    titleObjDe.put("lang", "de");
    expectedTitles.add(titleObjDe);
    Map<String,String> titleObjEn = new HashMap<>();
    titleObjEn.put("title", ap.getTitle().getEn());
    titleObjEn.put("lang", "en");
    expectedTitles.add(titleObjEn);
    assertEquals(expectedTitles, result.get("titles"));
    assertEquals(testProject.getRelease().getFirstDate().getYear(), result.get("publicationYear"));
    assertEquals(testProject.getRelease().getVersion(), result.get("version"));
    assertEquals(1, ((List<Map<String, Object>>) result.get("creators")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("contributors")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("fundingReferences")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("subjects")).size());
    assertEquals(1, ((List<Map<String, Object>>) result.get("dates")).size());

    Map<String, Object> resultDate =  ((List<Map<String, Object>>) result.get("dates")).get(0);
    assertEquals("Available", resultDate.get("dateType"));
    assertNull(resultDate.get("dateInformation"));

    assertEquals(0, ((List<Map<String, Object>>) result.get("geoLocations")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("alternateIdentifiers")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("relatedIdentifiers")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("rightsList")).size());
  }

  @Test
  public void createAttrObjectForDataPackage_preReleasedProjectNoPreviousVersion() throws DataCiteMetadataException {
    DataPackage dp = UnitTestCreateDomainObjectUtils.buildDataPackage(testProject.getId());
    testProject.getRelease().setIsPreRelease(true);
    testProject.setEmbargoDate(LocalDate.now());
    Map<String, Object> result = dataCiteService.createAttrObjectForDataPackage(
      testProject,
      dp,
      new ArrayList<>());
    assertTrue(result != null);
    assertEquals(this.getExpectedTitlesDeAndEn(dp), result.get("titles"));
    assertEquals(testProject.getRelease().getFirstDate().getYear(), result.get("publicationYear"));
    assertEquals(testProject.getRelease().getVersion(), result.get("version"));
    assertEquals(2, ((List<Map<String, Object>>) result.get("creators")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("contributors")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("fundingReferences")).size());
    assertEquals(3, ((List<Map<String, Object>>) result.get("subjects")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("dates")).size());

    Map<String, Object> resultDate1 =  ((List<Map<String, Object>>) result.get("dates")).get(0);
    assertEquals("Available", resultDate1.get("dateType"));
    assertEquals(String.format("This dataset is currently not yet available for order as it is subject to an embargo until %s." +
      " Publication can only take place after this date. Please note that the embargo date does not necessarily correspond" +
      " to the expected release date. Please contact userservice@dzhw.eu if you wish to receive information regarding the" +
      " release date of the dataset.", testProject.getEmbargoDate().toString()), resultDate1.get("dateInformation"));
    Map<String, Object> resultDate2 =  ((List<Map<String, Object>>) result.get("dates")).get(1);
    assertEquals("Accepted", resultDate2.get("dateType"));
    assertNull(resultDate2.get("dateInformation"));

    assertEquals(0, ((List<Map<String, Object>>) result.get("geoLocations")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("alternateIdentifiers")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("relatedIdentifiers")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("rightsList")).size());
  }

  @Test
  public void createAttrObjectForAnalysisPackage_preReleasedProjectNoPreviousVersion() throws DataCiteMetadataException {
    AnalysisPackage ap = UnitTestCreateDomainObjectUtils.buildAnalysisPackage(testProject.getId());
    testProject.getRelease().setIsPreRelease(true);
    testProject.setEmbargoDate(LocalDate.now());
    Map<String, Object> result = dataCiteService.createAttrObjectForAnalysisPackage(
      testProject,
      ap);
    assertTrue(result != null);
    List<Map<String, String>>  expectedTitles= new ArrayList<>();
    Map<String,String> titleObjDe = new HashMap<>();
    titleObjDe.put("title", ap.getTitle().getDe());
    titleObjDe.put("lang", "de");
    expectedTitles.add(titleObjDe);
    Map<String,String> titleObjEn = new HashMap<>();
    titleObjEn.put("title", ap.getTitle().getEn());
    titleObjEn.put("lang", "en");
    expectedTitles.add(titleObjEn);
    assertEquals(expectedTitles, result.get("titles"));
    assertEquals(testProject.getRelease().getFirstDate().getYear(), result.get("publicationYear"));
    assertEquals(testProject.getRelease().getVersion(), result.get("version"));
    assertEquals(1, ((List<Map<String, Object>>) result.get("creators")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("contributors")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("fundingReferences")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("subjects")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("dates")).size());

    Map<String, Object> resultDate1 =  ((List<Map<String, Object>>) result.get("dates")).get(0);
    assertEquals("Available", resultDate1.get("dateType"));
    assertEquals(String.format("This dataset is currently not yet available for order as it is subject to an embargo until %s." +
      " Publication can only take place after this date. Please note that the embargo date does not necessarily correspond" +
      " to the expected release date. Please contact userservice@dzhw.eu if you wish to receive information regarding the" +
      " release date of the dataset.", testProject.getEmbargoDate().toString()), resultDate1.get("dateInformation"));
    Map<String, Object> resultDate2 =  ((List<Map<String, Object>>) result.get("dates")).get(1);
    assertEquals("Accepted", resultDate2.get("dateType"));
    assertNull(resultDate2.get("dateInformation"));

    assertEquals(0, ((List<Map<String, Object>>) result.get("geoLocations")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("alternateIdentifiers")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("relatedIdentifiers")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("rightsList")).size());
  }

  @Test
  public void createAttrObjectForDataPackage_hiddenProjectNoPreviousVersion() throws DataCiteMetadataException {
    DataPackage dp = UnitTestCreateDomainObjectUtils.buildDataPackage(testProject.getId());
    testProject.setHidden(true);
    Map<String, Object> result = dataCiteService.createAttrObjectForDataPackage(
      testProject,
      dp,
      new ArrayList<>());
    assertTrue(result != null);
    assertEquals(this.getExpectedTitlesDeAndEn(dp), result.get("titles"));
    assertEquals(testProject.getRelease().getFirstDate().getYear(), result.get("publicationYear"));
    assertEquals(testProject.getRelease().getVersion(), result.get("version"));
    assertEquals(2, ((List<Map<String, Object>>) result.get("creators")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("contributors")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("fundingReferences")).size());
    assertEquals(3, ((List<Map<String, Object>>) result.get("subjects")).size());
    assertEquals(1, ((List<Map<String, Object>>) result.get("dates")).size());

    Map<String, Object> resultDate =  ((List<Map<String, Object>>) result.get("dates")).get(0);
    assertEquals("Withdrawn", resultDate.get("dateType"));
    assertEquals("The publication of the project was withdrawn.", resultDate.get("dateInformation"));

    assertEquals(0, ((List<Map<String, Object>>) result.get("geoLocations")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("alternateIdentifiers")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("relatedIdentifiers")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("rightsList")).size());
  }

  @Test
  public void createAttrObjectForAnalysisPackage_hiddenProjectNoPreviousVersion() throws DataCiteMetadataException {
    AnalysisPackage ap = UnitTestCreateDomainObjectUtils.buildAnalysisPackage(testProject.getId());
    testProject.setHidden(true);
    Map<String, Object> result = dataCiteService.createAttrObjectForAnalysisPackage(
      testProject,
      ap);
    assertTrue(result != null);
    List<Map<String, String>>  expectedTitles= new ArrayList<>();
    Map<String,String> titleObjDe = new HashMap<>();
    titleObjDe.put("title", ap.getTitle().getDe());
    titleObjDe.put("lang", "de");
    expectedTitles.add(titleObjDe);
    Map<String,String> titleObjEn = new HashMap<>();
    titleObjEn.put("title", ap.getTitle().getEn());
    titleObjEn.put("lang", "en");
    expectedTitles.add(titleObjEn);
    assertEquals(expectedTitles, result.get("titles"));
    assertEquals(testProject.getRelease().getFirstDate().getYear(), result.get("publicationYear"));
    assertEquals(testProject.getRelease().getVersion(), result.get("version"));
    assertEquals(1, ((List<Map<String, Object>>) result.get("creators")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("contributors")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("fundingReferences")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("subjects")).size());
    assertEquals(1, ((List<Map<String, Object>>) result.get("dates")).size());

    Map<String, Object> resultDate =  ((List<Map<String, Object>>) result.get("dates")).get(0);
    assertEquals("Withdrawn", resultDate.get("dateType"));
    assertEquals("The publication of the project was withdrawn.", resultDate.get("dateInformation"));

    assertEquals(0, ((List<Map<String, Object>>) result.get("geoLocations")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("alternateIdentifiers")).size());
    assertEquals(0, ((List<Map<String, Object>>) result.get("relatedIdentifiers")).size());
    assertEquals(2, ((List<Map<String, Object>>) result.get("rightsList")).size());
  }

  private List<Map<String, String>> getExpectedTitlesDeAndEn(DataPackage dp) {
    List<Map<String, String>>  expectedTitles= new ArrayList<>();
    Map<String,String> titleObjDe = new HashMap<>();
    titleObjDe.put("title", dp.getTitle().getDe());
    titleObjDe.put("lang", "de");
    expectedTitles.add(titleObjDe);
    Map<String,String> titleObjEn = new HashMap<>();
    titleObjEn.put("title", dp.getTitle().getEn());
    titleObjEn.put("lang", "en");
    expectedTitles.add(titleObjEn);
    return expectedTitles;
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
