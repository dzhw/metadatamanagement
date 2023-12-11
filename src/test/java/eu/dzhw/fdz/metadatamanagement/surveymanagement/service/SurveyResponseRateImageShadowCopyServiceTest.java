package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.util.StringUtils;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyResponseRateImageMetadata;

public class SurveyResponseRateImageShadowCopyServiceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  private static final String FILE_NAME = "1_responserate_en";

  private static final String SURVEY_ID = "sur-test2019-sy1$";

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private GridFsMetadataUpdateService gridFsMetadataUpdateService;

  @Autowired
  private SurveyResponseRateImageShadowCopyService shadowCopyService;

  private Release release;

  @BeforeEach
  public void setup() {
    release = new Release("1.0.0", LocalDateTime.now(), null, false, null, false);
  }

  @AfterEach
  public void teardown() {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^/surveys*"));
    this.gridFsOperations.delete(query);
  }

  @Test
  public void createShadowCopy() throws Exception {
    SurveyResponseRateImageMetadata master = createResponseRateImageMetadata();
    createTestFileForSurveyRateImage(master);
    DataAcquisitionProject dataAcquisitionProject = createDataAcquisitionProject();
    release.setVersion("1.0.1");
    shadowCopyService.createShadowCopies(dataAcquisitionProject.getId(),
        dataAcquisitionProject.getRelease(), "1.0.0");

    String shadowFileName = createFileName(release.getVersion());

    Query query = new Query(GridFsCriteria.whereFilename().is(shadowFileName));
    GridFSFile shadow = gridFsOperations.findOne(query);

    assertThat(shadow, notNullValue());

    SurveyResponseRateImageMetadata metadata =
        convertGridFsFileToSurveyResponseRateImageMetadata(shadow);

    assertThat(metadata.getId(), equalTo("/public/files" + shadowFileName));
    assertThat(metadata.getSurveyId(), equalTo(master.getSurveyId() + "-" + release.getVersion()));
    assertThat(metadata.getDataAcquisitionProjectId(),
        equalTo(master.getDataAcquisitionProjectId() + "-" + release.getVersion()));
    assertThat(metadata.isShadow(), equalTo(true));
    assertThat(shadow.getMetadata().get("_contentType"), equalTo("image/png"));
  }

  private SurveyResponseRateImageMetadata convertGridFsFileToSurveyResponseRateImageMetadata(
      GridFSFile shadow) {
    return mongoTemplate.getConverter().read(SurveyResponseRateImageMetadata.class,
        shadow.getMetadata());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() throws Exception {
    SurveyResponseRateImageMetadata master = createResponseRateImageMetadata();
    createTestFileForSurveyRateImage(master);
    SurveyResponseRateImageMetadata shadow = createShadow(master, "1.0.0");
    createTestFileForSurveyRateImage(shadow);

    DataAcquisitionProject dataAcquisitionProject = createDataAcquisitionProject();
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(dataAcquisitionProject.getId(),
        dataAcquisitionProject.getRelease(), "1.0.0");

    Query predecessorQuery = new Query(GridFsCriteria.whereFilename().is(createFileName("1.0.0")));
    GridFSFile predecessorFile = gridFsOperations.findOne(predecessorQuery);
    assertThat(predecessorFile, notNullValue());
    assertThat(predecessorFile.getMetadata().get("_contentType"), equalTo("image/png"));
    SurveyResponseRateImageMetadata predecessorMetadata =
        convertGridFsFileToSurveyResponseRateImageMetadata(predecessorFile);

    Query successorQuery = new Query(GridFsCriteria.whereFilename().is(createFileName("1.0.1")));
    GridFSFile successorFile = gridFsOperations.findOne(successorQuery);
    assertThat(successorFile, notNullValue());
    SurveyResponseRateImageMetadata successorMetadata =
        convertGridFsFileToSurveyResponseRateImageMetadata(successorFile);

    assertThat(predecessorMetadata.getSuccessorId(), equalTo(successorMetadata.getId()));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() throws Exception {
    SurveyResponseRateImageMetadata master = createResponseRateImageMetadata();
    SurveyResponseRateImageMetadata shadow = createShadow(master, "1.0.0");
    createTestFileForSurveyRateImage(shadow);
    DataAcquisitionProject dataAcquisitionProject = createDataAcquisitionProject();
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(dataAcquisitionProject.getId(),
        dataAcquisitionProject.getRelease(), "1.0.0");

    Query shadowCopyQuery = new Query(GridFsCriteria.whereFilename().is(createFileName("1.0.0")));
    GridFSFile shadowCopyFile = gridFsOperations.findOne(shadowCopyQuery);
    assertThat(shadowCopyFile, notNullValue());
    SurveyResponseRateImageMetadata shadowCopyMetadata =
        convertGridFsFileToSurveyResponseRateImageMetadata(shadowCopyFile);

    assertThat(shadowCopyMetadata.getSuccessorId(), equalTo("DELETED"));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() throws Exception {
    SurveyResponseRateImageMetadata master = createResponseRateImageMetadata();
    createTestFileForSurveyRateImage(master);
    DataAcquisitionProject dataAcquisitionProject = createDataAcquisitionProject();
    shadowCopyService.createShadowCopies(dataAcquisitionProject.getId(),
        dataAcquisitionProject.getRelease(), "1.0.0");

    Query masterQuery = new Query(GridFsCriteria.whereFilename().is(createFileName("")));
    GridFSFile masterFile = gridFsOperations.findOne(masterQuery);

    assertThat(masterFile, notNullValue());

    Query shadowQuery =
        new Query(GridFsCriteria.whereFilename().is(createFileName(release.getVersion())));
    GridFSFile shadowFile = gridFsOperations.findOne(shadowQuery);

    assertThat(shadowFile, notNullValue());

    SurveyResponseRateImageMetadata shadowMetadata =
        convertGridFsFileToSurveyResponseRateImageMetadata(shadowFile);

    assertThat(shadowMetadata.getSuccessorId(), nullValue());

    List<GridFSFile> files = new ArrayList<>();

    gridFsOperations.find(new Query()).iterator().forEachRemaining(files::add);
    assertThat(files.size(), equalTo(2));
    assertThat(shadowFile.getMetadata().get("_contentType"), equalTo("image/png"));
  }

  private String createFileName(String version) {
    if (StringUtils.hasText(version)) {
      return String.format("/surveys/%s-%s/%s", SURVEY_ID, version, FILE_NAME);
    } else {
      return String.format("/surveys/%s/%s", SURVEY_ID, FILE_NAME);
    }
  }

  private DataAcquisitionProject createDataAcquisitionProject() {
    DataAcquisitionProject dataAcquisitionProject =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProject.setId(PROJECT_ID);
    dataAcquisitionProject.setMasterId(dataAcquisitionProject.getId());
    dataAcquisitionProject.setRelease(release);
    return dataAcquisitionProject;
  }

  private SurveyResponseRateImageMetadata createResponseRateImageMetadata() {
    SurveyResponseRateImageMetadata master = new SurveyResponseRateImageMetadata();
    master.setSurveyId(SURVEY_ID);
    master.setDataAcquisitionProjectId(PROJECT_ID);
    master.setLanguage("en");
    master.setFileName(FILE_NAME);
    master.setSurveyNumber(4);
    master.generateId();
    master.setMasterId(master.getId());
    return master;
  }

  private SurveyResponseRateImageMetadata createShadow(SurveyResponseRateImageMetadata master,
      String version) {
    SurveyResponseRateImageMetadata shadow = new SurveyResponseRateImageMetadata();
    BeanUtils.copyProperties(master, shadow);
    shadow.setSurveyId(master.getSurveyId() + "-" + version);
    shadow.setDataAcquisitionProjectId(master.getDataAcquisitionProjectId() + "-" + version);
    shadow.generateId();
    return shadow;
  }

  private void createTestFileForSurveyRateImage(SurveyResponseRateImageMetadata metadata)
      throws Exception {

    try (InputStream is = new ByteArrayInputStream("fakeimage".getBytes(StandardCharsets.UTF_8))) {
      String filename =
          String.format("/surveys/%s/%s", metadata.getSurveyId(), metadata.getFileName());
      gridFsMetadataUpdateService.store(is, filename, "image/png", metadata);
    }
  }
}
