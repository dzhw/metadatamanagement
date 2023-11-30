package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.security.test.context.support.WithMockUser;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper.DataSetAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class DataSetAttachmentShadowCopyServiceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private GridFsMetadataUpdateService gridFsMetadataUpdateService;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private DataSetAttachmentShadowCopyService shadowCopyService;

  private DataAcquisitionProject dataAcquisitionProject;

  private Release release;

  @BeforeEach
  public void setup() {
    release = new Release("1.0.0", LocalDateTime.now(), null, false, null);

    dataAcquisitionProject = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProject.setId(PROJECT_ID);
    dataAcquisitionProject.setRelease(release);
  }

  @AfterEach
  public void teardown() {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^/data-sets"));
    this.gridFsOperations.delete(query);
  }

  @Test
  public void createShadowCopy() throws Exception {
    DataSetAttachmentMetadata master =
        UnitTestCreateDomainObjectUtils.buildDataSetAttachmentMetadata(PROJECT_ID, 1);
    master.generateId();
    master.setMasterId(master.getId());
    createTestFileForAttachment(master);

    shadowCopyService.createShadowCopies(dataAcquisitionProject.getId(),
        dataAcquisitionProject.getRelease(), null);

    GridFSFile gridFsFile = gridFsOperations.findOne(
        new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(PROJECT_ID + "-1.0.0")
            .andOperator(GridFsCriteria.whereMetaData("shadow").is(true))));

    DataSetAttachmentMetadata metaData = mongoTemplate.getConverter()
        .read(DataSetAttachmentMetadata.class, gridFsFile.getMetadata());

    assertThat(gridFsFile, notNullValue());
    assertThat(metaData, notNullValue());
    assertThat(metaData.getId(), equalTo(
        "/public/files/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.0/attachments/filename.txt"));
    assertThat(metaData.getDataAcquisitionProjectId(), equalTo(PROJECT_ID + "-1.0.0"));
    assertThat(metaData.isShadow(), equalTo(true));
    assertThat(gridFsFile.getFilename(),
        equalTo("/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.0/attachments/filename.txt"));

    List<String> expectedFiles = new ArrayList<>();
    expectedFiles.add("/data-sets/dat-" + PROJECT_ID + "-ds1$/attachments/filename.txt");
    expectedFiles.add("/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.0/attachments/filename.txt");
    assertExpectedFilesExistence(expectedFiles);

    GridFsResource shadowCopy = gridFsOperations
        .getResource("/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.0/attachments/filename.txt");
    assertThat(shadowCopy.getOptions().getMetadata().get("_contentType"), equalTo("text/plain"));
  }

  @Test
  public void createShadowCopyWithSameReleaseVersion() throws Exception {
    DataSetAttachmentMetadata master =
        UnitTestCreateDomainObjectUtils.buildDataSetAttachmentMetadata(PROJECT_ID, 1);
    master.generateId();
    master.setMasterId(master.getId());
    createTestFileForAttachment(master);
    DataSetAttachmentMetadata shadow = createShadow(master, "1.0.0");
    createTestFileForAttachment(shadow);

    shadowCopyService.createShadowCopies(dataAcquisitionProject.getId(),
        dataAcquisitionProject.getRelease(), "1.0.0");

    List<GridFSFile> files = new ArrayList<>();
    gridFsOperations.find(new Query())
        .forEach((Consumer<? super GridFSFile>) (GridFSFile f) -> files.add(f));

    assertThat(files.size(), equalTo(2));

    Query shadowQuery = new Query(GridFsCriteria.whereFilename()
        .is("/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.0/attachments/filename.txt"));
    GridFSFile shadowFile = gridFsOperations.findOne(shadowQuery);

    assertThat(shadowFile, notNullValue());

    DataSetAttachmentMetadata metadata = mongoTemplate.getConverter()
        .read(DataSetAttachmentMetadata.class, shadowFile.getMetadata());

    assertThat(metadata.getSuccessorId(), nullValue());
    assertThat(shadowFile.getMetadata().get("_contentType"), equalTo("text/plain"));
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() throws Exception {
    DataSetAttachmentMetadata master =
        UnitTestCreateDomainObjectUtils.buildDataSetAttachmentMetadata(PROJECT_ID, 1);
    master.generateId();
    master.setMasterId(master.getId());
    createTestFileForAttachment(master);

    DataSetAttachmentMetadata shadow = createShadow(master, "1.0.0");
    createTestFileForAttachment(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(dataAcquisitionProject.getId(),
        dataAcquisitionProject.getRelease(), "1.0.0");

    GridFSFile gridFsFile = gridFsOperations.findOne(
        new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(PROJECT_ID + "-1.0.0")
            .andOperator(GridFsCriteria.whereMetaData("shadow").is(true))));

    DataSetAttachmentMetadata metadata = mongoTemplate.getConverter()
        .read(DataSetAttachmentMetadata.class, gridFsFile.getMetadata());

    assertThat(metadata, notNullValue());
    assertThat(metadata.getSuccessorId(), equalTo(
        "/public/files/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.1/attachments/filename.txt"));

    List<String> expectedFiles = new ArrayList<>();
    expectedFiles.add("/data-sets/dat-" + PROJECT_ID + "-ds1$/attachments/filename.txt");
    expectedFiles.add("/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.0/attachments/filename.txt");
    expectedFiles.add("/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.1/attachments/filename.txt");
    assertExpectedFilesExistence(expectedFiles);

    GridFsResource predecessor = gridFsOperations
        .getResource("/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.0/attachments/filename.txt");
    assertThat(predecessor.getOptions().getMetadata().get("_contentType"), equalTo("text/plain"));
  }

  @Test
  public void createShadowCopyWithDeletedMaster() throws Exception {
    DataSetAttachmentMetadata master =
        UnitTestCreateDomainObjectUtils.buildDataSetAttachmentMetadata(PROJECT_ID, 1);

    DataSetAttachmentMetadata shadow = createShadow(master, "1.0.0");
    createTestFileForAttachment(shadow);
    release.setVersion("1.0.1");

    shadowCopyService.createShadowCopies(dataAcquisitionProject.getId(),
        dataAcquisitionProject.getRelease(), "1.0.0");

    Query shadowQuery = new Query(GridFsCriteria.whereFilename()
        .is("/data-sets/dat-" + PROJECT_ID + "-ds1$-1.0.0/attachments/filename.txt"));
    GridFSFile shadowFile = gridFsOperations.findOne(shadowQuery);

    assertThat(shadowFile, notNullValue());

    DataSetAttachmentMetadata metadata = mongoTemplate.getConverter()
        .read(DataSetAttachmentMetadata.class, shadowFile.getMetadata());

    assertThat(metadata.getSuccessorId(), equalTo("DELETED"));
  }

  private DataSetAttachmentMetadata createShadow(DataSetAttachmentMetadata master, String version) {

    DataSetAttachmentMetadata shadow = new DataSetAttachmentMetadata();
    BeanUtils.copyProperties(master, shadow);
    shadow.setDataSetId(master.getDataSetId() + "-" + version);
    shadow.setDataAcquisitionProjectId(shadow.getDataAcquisitionProjectId() + "-" + version);
    shadow.generateId();
    return shadow;
  }

  private void createTestFileForAttachment(DataSetAttachmentMetadata metadata) throws Exception {

    InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
    String filename = DataSetAttachmentFilenameBuilder.buildFileName(metadata);
    gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    is.close();
  }

  private void assertExpectedFilesExistence(List<String> expectedFiles) {
    Iterator<GridFSFile> it = gridFsOperations.find(new Query()).iterator();
    List<String> fileNames = new ArrayList<>();
    while (it.hasNext()) {
      fileNames.add(it.next().getFilename());
    }
    assertThat(fileNames.size(), equalTo(expectedFiles.size()));
    assertThat(fileNames, containsInAnyOrder(expectedFiles.toArray()));
  }
}
