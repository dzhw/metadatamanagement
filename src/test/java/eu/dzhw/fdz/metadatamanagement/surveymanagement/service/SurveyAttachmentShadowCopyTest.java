package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;

public class SurveyAttachmentShadowCopyTest extends AbstractTest {

  private static final String PROJECT_ID = "the-dataacquisition-project-id";

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private GridFS gridFs;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private SurveyAttachmentShadowCopyDataSource surveyAttachmentShadowCopyDataSource;

  @Autowired
  private ShadowCopyService<SurveyAttachmentMetadata> shadowCopyService;

  private DataAcquisitionProject dataAcquisitionProject;

  private Release release;

  @Before
  public void setup() {
    release = new Release("1.0.0", LocalDateTime.now());

    dataAcquisitionProject = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProject.setId(PROJECT_ID);
    dataAcquisitionProject.setRelease(release);
  }

  @After
  public void teardown() {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^/surveys*"));
    this.gridFsOperations.delete(query);
  }

  @Test
  public void createShadowCopy() throws Exception {
    SurveyAttachmentMetadata master = UnitTestCreateDomainObjectUtils
        .buildSurveyAttachmentMetadata(PROJECT_ID, 1);
    createTestFileForAttachment(master);

    shadowCopyService.createShadowCopies(dataAcquisitionProject,
        surveyAttachmentShadowCopyDataSource);

    GridFSFile gridFsFile = gridFsOperations.findOne(new Query(GridFsCriteria
        .whereMetaData("dataAcquisitionProjectId").is(PROJECT_ID + "-1.0.0")
        .andOperator(GridFsCriteria.whereMetaData("shadow").is(true))));

    SurveyAttachmentMetadata metaData = mongoTemplate.getConverter()
        .read(SurveyAttachmentMetadata.class, gridFsFile.getMetadata());

    assertThat(gridFsFile, notNullValue());
    assertThat(metaData, notNullValue());
    assertThat(metaData.getId(), equalTo("/public/files/surveys/sur-the-dataacquisition-project-id-sy1$-1.0.0/attachments/filename.txt"));
    assertThat(metaData.getDataAcquisitionProjectId(), equalTo(PROJECT_ID + "-1.0.0"));
    assertThat(metaData.getSurveyId(), equalTo(master.getSurveyId() + "-1.0.0"));
    assertThat(metaData.isShadow(), equalTo(true));
    assertThat(gridFsFile.getFilename(), equalTo("/surveys/sur-the-dataacquisition-project-id-sy1$-1.0.0/attachments/filename.txt"));

    List<String> expectedFiles = new ArrayList<>();
    expectedFiles.add("/surveys/" + master.getSurveyId() + "/attachments/filename.txt");
    expectedFiles.add("/surveys/" + master.getSurveyId() + "-1.0.0/attachments/filename.txt");
    assertExpectedFilesExistence(expectedFiles);
  }

  @Test
  public void createShadowCopy_link_predecessor_to_successor() throws Exception {
    SurveyAttachmentMetadata master = UnitTestCreateDomainObjectUtils
        .buildSurveyAttachmentMetadata(PROJECT_ID, 1);
    createTestFileForAttachment(master);

    SurveyAttachmentMetadata shadow = UnitTestCreateDomainObjectUtils
        .buildSurveyAttachmentMetadata(PROJECT_ID, 1);
    shadow.setMasterId(master.getId());
    shadow.setSurveyId(shadow.getSurveyId() + "-1.0.0");
    shadow.setDataAcquisitionProjectId(shadow.getDataAcquisitionProjectId() + "-1.0.0");
    shadow.generateId();
    createTestFileForAttachment(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(dataAcquisitionProject, surveyAttachmentShadowCopyDataSource);

    GridFSFile gridFsFile = gridFsOperations.findOne(new Query(GridFsCriteria
        .whereMetaData("dataAcquisitionProjectId").is(PROJECT_ID + "-1.0.0")
        .andOperator(GridFsCriteria.whereMetaData("shadow").is(true))));

    SurveyAttachmentMetadata metaData = mongoTemplate.getConverter()
        .read(SurveyAttachmentMetadata.class, gridFsFile.getMetadata());

    assertThat(metaData, notNullValue());
    assertThat(metaData.getSuccessorId(), equalTo("/public/files/surveys/sur-the-dataacquisition-project-id-sy1$-1.0.1/attachments/filename.txt"));

    List<String> expectedFiles = new ArrayList<>();
    expectedFiles.add("/surveys/" + master.getSurveyId() + "/attachments/filename.txt");
    expectedFiles.add("/surveys/" + master.getSurveyId() + "-1.0.0/attachments/filename.txt");
    expectedFiles.add("/surveys/" + master.getSurveyId() + "-1.0.1/attachments/filename.txt");
    assertExpectedFilesExistence(expectedFiles);
  }

  private void createTestFileForAttachment(SurveyAttachmentMetadata metadata)
      throws Exception {

    InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
    String filename = SurveyAttachmentFilenameBuilder.buildFileName(metadata);
    gridFsOperations.store(is, filename, "text/plain", metadata);
    is.close();
  }

  private void assertExpectedFilesExistence(List<String> expectedFiles) {
    Iterator<DBObject> it = gridFs.getFileList().iterator();
    List<String> fileNames = new ArrayList<>();
    while (it.hasNext()) {
      fileNames.add((String)it.next().get("filename"));
    }
    assertThat(fileNames.size(), equalTo(expectedFiles.size()));
    assertThat(fileNames, containsInAnyOrder(expectedFiles.toArray()));
  }
}
