package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SurveyAttachmentShadowCopytest extends AbstractTest {

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private GridFS gridFs;

  @Autowired
  private MongoTemplate mongoTemplate;

  private SurveyAttachmentShadowCopyDataSource surveyAttachmentShadowCopyDataSource;

  private ShadowCopyService<SurveyAttachmentMetadata> shadowCopyService;

  private SurveyAttachmentMetadata master;

  @Before
  public void setup() {
    InputStream is = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
    master = new SurveyAttachmentMetadata();
    master.setSurveyId("survey");
    master.setDataAcquisitionProjectId("dataAcquisitionProjectId");
    master.setDescription(new I18nString("de-description","en-description"));
    master.setLanguage("de");
    master.setFileName("some-random-text.txt");
    master.setTitle("Create shadow copy of this attachment");
    master.setSurveyNumber(1);
    master.setIndexInSurvey(0);

    BasicDBObject metadata = new BasicDBObject((Document)mongoTemplate.getConverter()
        .convertToMongoType(master));

    String filename = SurveyAttachmentFilenameBuilder.buildFileName(master);
    gridFsOperations.store(is, filename, "text/plain", metadata);
  }

  @After
  public void teardown() {
    gridFsOperations.delete(new Query());
  }

  @Test
  public void createShadowCopy() {

  }
}
