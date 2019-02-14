package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyResponseRateImageMetadata;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SurveyResponseRateImageMetadataShadowCopyTest extends AbstractTest {

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private GridFS gridFs;

  @Autowired
  private SurveyResponseRateImageMetadataShadowCopyDataSource surveyResponseRateImageMetadataShadowCopyDataSource;

  @Autowired
  private ShadowCopyService<SurveyResponseRateImageMetadata> shadowCopyService;

  @After
  public void teardown() {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^/surveys*"));
    this.gridFsOperations.delete(query);
  }

  @Test
  public void createShadowCopyLinkPredecessorToSuccessor() throws Exception {
    SurveyResponseRateImageMetadata master = new SurveyResponseRateImageMetadata();
    master.setSurveyId("sur-test2019-sy1");
    master.setDataAcquisitionProjectId("test2019");
    master.setLanguage("en");
    master.setFileName("4_responserate_en");
    master.setSurveyNumber(4);
    master.generateId();
    master.setMasterId(master.getId());
    createTestFileForSurveyRateImage(master);

    SurveyResponseRateImageMetadata shadow = new SurveyResponseRateImageMetadata();
    shadow.setSurveyId("sur-test2019-sy1-1.0.0");
    shadow.setDataAcquisitionProjectId("test2019-1.0.0");
    shadow.setLanguage("en");
    shadow.setFileName("4_responserate_en");
    shadow.setSurveyNumber(4);
    shadow.generateId();
    shadow.setMasterId(master.getId());
    createTestFileForSurveyRateImage(shadow);

    DataAcquisitionProject dataAcquisitionProject = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProject.setId("test2019");
    dataAcquisitionProject.setMasterId(dataAcquisitionProject.getId());
    dataAcquisitionProject.setRelease(new Release("1.0.1", LocalDateTime.now()));

    shadowCopyService.createShadowCopies(dataAcquisitionProject, "1.0.0", surveyResponseRateImageMetadataShadowCopyDataSource);

    Iterator<DBObject> it = gridFs.getFileList().iterator();
    List<String> fileNames = new ArrayList<>();
    while (it.hasNext()) {
      fileNames.add((String)it.next().get("filename"));
    }

    assertThat(fileNames.size(), equalTo(3));
  }

  private void createTestFileForSurveyRateImage(SurveyResponseRateImageMetadata metadata)
      throws Exception {

    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      String filename = String.format("/surveys/%s/%s", metadata.getSurveyId(), metadata.getFileName());
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }
  }
}
