package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class StudyAttachmentMetadataShadowCopyDataSourceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private GridFS gridFs;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private StudyAttachmentMetadataShadowCopyDataSource shadowCopyDataSource;

  @Autowired
  private ShadowCopyService<StudyAttachmentMetadata> shadowCopyService;

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
    Query query = new Query(GridFsCriteria.whereFilename().regex("^/studies"));
    this.gridFsOperations.delete(query);
  }

  @Test
  public void createShadowCopy() throws Exception {
    StudyAttachmentMetadata master = UnitTestCreateDomainObjectUtils
        .buildStudyAttachmentMetadata(PROJECT_ID);
    createTestFileForAttachment(master);

    shadowCopyService.createShadowCopies(dataAcquisitionProject,
        null, shadowCopyDataSource);

    GridFSFile gridFsFile = gridFsOperations.findOne(new Query(GridFsCriteria
        .whereMetaData("dataAcquisitionProjectId").is(PROJECT_ID + "-1.0.0")
        .andOperator(GridFsCriteria.whereMetaData("shadow").is(true))));

    StudyAttachmentMetadata metaData = mongoTemplate.getConverter()
        .read(StudyAttachmentMetadata.class, gridFsFile.getMetadata());

    assertThat(gridFsFile, notNullValue());
    assertThat(metaData, notNullValue());
    assertThat(metaData.getId(), equalTo("/public/files/studies/stu-" + PROJECT_ID + "$-1.0.0/attachments/filename.txt"));
    assertThat(metaData.getDataAcquisitionProjectId(), equalTo(PROJECT_ID + "-1.0.0"));
    assertThat(metaData.isShadow(), equalTo(true));
    assertThat(gridFsFile.getFilename(), equalTo("/studies/stu-" + PROJECT_ID + "$-1.0.0/attachments/filename.txt"));

    List<String> expectedFiles = new ArrayList<>();
    expectedFiles.add("/studies/stu-" + PROJECT_ID + "$/attachments/filename.txt");
    expectedFiles.add("/studies/stu-" + PROJECT_ID + "$-1.0.0/attachments/filename.txt");
    assertExpectedFilesExistence(expectedFiles);
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() throws Exception {
    StudyAttachmentMetadata master = UnitTestCreateDomainObjectUtils
        .buildStudyAttachmentMetadata(PROJECT_ID);
    createTestFileForAttachment(master);
    StudyAttachmentMetadata shadow = createShadow(master, "1.0.0");
    createTestFileForAttachment(shadow);

    shadowCopyService.createShadowCopies(dataAcquisitionProject, "1.0.0",
        shadowCopyDataSource);

    List<DBObject> files = new ArrayList<>();
    gridFs.getFileList().iterator().forEachRemaining(files::add);

    assertThat(files.size(), equalTo(2));

    Query shadowQuery = new Query(GridFsCriteria.whereFilename().is("/studies/stu-" + PROJECT_ID + "$-1.0.0/attachments/filename.txt"));
    GridFSFile shadowFile = gridFsOperations.findOne(shadowQuery);

    assertThat(shadowFile, notNullValue());

    StudyAttachmentMetadata metadata = mongoTemplate.getConverter()
        .read(StudyAttachmentMetadata.class, shadowFile.getMetadata());

    assertThat(metadata.getSuccessorId(), nullValue());
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() throws Exception {
    StudyAttachmentMetadata master = UnitTestCreateDomainObjectUtils
        .buildStudyAttachmentMetadata(PROJECT_ID);
    createTestFileForAttachment(master);

    StudyAttachmentMetadata shadow = createShadow(master, "1.0.0");
    createTestFileForAttachment(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(dataAcquisitionProject, "1.0.0",
        shadowCopyDataSource);

    GridFSFile gridFsFile = gridFsOperations.findOne(new Query(GridFsCriteria
        .whereMetaData("dataAcquisitionProjectId").is(PROJECT_ID + "-1.0.0")
        .andOperator(GridFsCriteria.whereMetaData("shadow").is(true))));

    StudyAttachmentMetadata metadata = mongoTemplate.getConverter()
        .read(StudyAttachmentMetadata.class, gridFsFile.getMetadata());

    assertThat(metadata, notNullValue());
    assertThat(metadata.getSuccessorId(), equalTo("/public/files/studies/stu-" + PROJECT_ID + "$-1.0.1/attachments/filename.txt"));

    List<String> expectedFiles = new ArrayList<>();
    expectedFiles.add("/studies/stu-" + PROJECT_ID + "$/attachments/filename.txt");
    expectedFiles.add("/studies/stu-" + PROJECT_ID + "$-1.0.0/attachments/filename.txt");
    expectedFiles.add("/studies/stu-" + PROJECT_ID + "$-1.0.1/attachments/filename.txt");
    assertExpectedFilesExistence(expectedFiles);
  }

  @Test
  public void createShadowCopyWithDeletedMaster() throws Exception {
    StudyAttachmentMetadata master = UnitTestCreateDomainObjectUtils
        .buildStudyAttachmentMetadata(PROJECT_ID);

    StudyAttachmentMetadata shadow = createShadow(master, "1.0.0");
    createTestFileForAttachment(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(dataAcquisitionProject, "1.0.0",
        shadowCopyDataSource);

    Query shadowQuery = new Query(GridFsCriteria.whereFilename().is("/studies/stu-" + PROJECT_ID + "$-1.0.0/attachments/filename.txt"));
    GridFSFile shadowFile = gridFsOperations.findOne(shadowQuery);

    assertThat(shadowFile, notNullValue());

    StudyAttachmentMetadata metadata = mongoTemplate.getConverter()
        .read(StudyAttachmentMetadata.class, shadowFile.getMetadata());

    assertThat(metadata.getSuccessorId(), equalTo("DELETED"));
  }

  private StudyAttachmentMetadata createShadow(StudyAttachmentMetadata master, String version) {

    StudyAttachmentMetadata shadow = new StudyAttachmentMetadata();
    BeanUtils.copyProperties(master, shadow);
    shadow.setStudyId(master.getStudyId() + "-" + version);
    shadow.setDataAcquisitionProjectId(shadow.getDataAcquisitionProjectId() + "-" + version);
    shadow.generateId();
    return shadow;
  }

  private void createTestFileForAttachment(StudyAttachmentMetadata metadata)
      throws Exception {

    InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
    String filename = StudyAttachmentFilenameBuilder.buildFileName(metadata);
    gridFsOperations.store(is, filename, "text/plain", metadata);
    is.close();
  }

  private void assertExpectedFilesExistence(List<String> expectedFiles) {
    Iterator<DBObject> it = gridFs.getFileList().iterator();
    List<String> fileNames = new ArrayList<>();
    while (it.hasNext()) {
      fileNames.add((String) it.next().get("filename"));
    }
    assertThat(fileNames.size(), equalTo(expectedFiles.size()));
    assertThat(fileNames, containsInAnyOrder(expectedFiles.toArray()));
  }
}