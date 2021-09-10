package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

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

import java.time.LocalDate;

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
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Test the REST API for {@link Survey}s.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class SurveyResourceControllerTest extends AbstractTest {
  private static final String API_SURVEYS_URI = "/api/surveys";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository rdcProjectRepository;

  @Autowired
  private SurveyRepository surveyRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;
  
  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @AfterEach
  public void cleanUp() {
    rdcProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    elasticsearchUpdateQueueService.clearQueue();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testCreateValidSurvey() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    // create the survey with the given id
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // read the survey under the new url
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().isOk());

    // call toString for test coverage :-)
    survey.toString();
    
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one survey document
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));
  }
  
  @Test
  public void testCreateValidSurveyWithPost() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    // create the survey with the given id
    mockMvc.perform(post(API_SURVEYS_URI).content(TestUtil.convertObjectToJsonBytes(survey))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // read the survey under the new url
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId())).andExpect(status().isOk());

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one survey document
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));
  }

  @Test
  public void testCreateSurveyWithMixedMethodDataType() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    survey.setDataType(I18nString.builder().de("Mixed Methods").en("Mixed Methods").build());

    // create the survey with the given id but with wrong period
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andReturn();
  }
  
  @Test
  public void testCreateSurveyWithNoDataType() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    survey.setDataType(null);

    // create the survey with the given id but with wrong period
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void testCreateSurveyWithInvalidPeriod() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = Survey.builder().id("testId")
      .dataAcquisitionProjectId(project.getId())
      .title(I18nString.builder().de("titel")
        .en("title")
        .build())
      .fieldPeriod(Period.builder().start(LocalDate.now())
        .end(LocalDate.now()
          .minusDays(1))
        .build())
      .build();

    // create the survey with the given id but with wrong period
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void testCreateSurveyWithUnlimitedPeriod() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    survey.setFieldPeriod(Period.builder().start(null)
        .end(null)
      .build());

    // create the survey with the given id but with an unlimited period
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateSurveyWithInvalidProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();

    Survey survey = Survey.builder().id("testId")
      .dataAcquisitionProjectId(project.getId())
      .title(I18nString.builder().de("titel")
        .en("title")
        .build())
      .fieldPeriod(Period.builder().start(LocalDate.now())
        .end(LocalDate.now()
          .plusDays(1))
        .build())
      .build();

    // create the survey with the given id but with an unknown project
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateSurveyEmptyProject() throws Exception {
    Survey survey = Survey.builder().id("testId")
      .dataAcquisitionProjectId(null)
      .title(I18nString.builder().de("titel")
        .en("title")
        .build())
      .fieldPeriod(Period.builder().start(LocalDate.now())
        .end(LocalDate.now()
          .plusDays(1))
        .build())
      .build();

    // create the survey with the given id but without a project
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateSurveyWithUnparsableDate() throws Exception {
    String survey = "{\"id\":\"6\"," + "\"dataAcquisitionProjectId\":\"Renes Projekt\","
        + "\"title\":{\"en\":\"High school graduates 2008: Third Wave\","
        + "\"de\":\"Bildungs-, Berufs- und Lebenswege - Dritte Befragung der Schulabsolventinnen und -absolventen des Jahrgangs 2007/2008\"},"
        + "\"fieldPeriod\":{\"start\":\"2012-12-01\",\"end\":\"2013-04-01d\"}}";

    // create the survey with the given id but without a project
    mockMvc.perform(put(API_SURVEYS_URI + "/6").content(survey).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())     
      .andExpect(jsonPath("$.errors[0].invalidValue", is("2013-04-01d")))
      .andExpect(jsonPath("$.errors[0].property", is("end")))
      .andExpect(jsonPath("$.errors[0].entity", is("Survey")))
      .andExpect(jsonPath("$.errors[0].message", is("global.error.import.json-parsing-error")));    
  }
  
  @Test
  public void testCreateSurveyWithUnparsableSampleSize() throws Exception {
    String survey = "{\"id\":\"6\"," + "\"dataAcquisitionProjectId\":\"Renes Projekt\","
        + "\"title\":{\"en\":\"High school graduates 2008: Third Wave\","
        + "\"de\":\"Bildungs-, Berufs- und Lebenswege - Dritte Befragung der Schulabsolventinnen und -absolventen des Jahrgangs 2007/2008\"},"
        + "\"sampleSize\":\"6te\","
        + "\"fieldPeriod\":{\"start\":\"2012-12-01\",\"end\":\"2013-04-01\"}}";

    // create the survey with the given id but without a project
    mockMvc.perform(put(API_SURVEYS_URI + "/6").content(survey).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].invalidValue", is("6te")))
      .andExpect(jsonPath("$.errors[0].property", is("sampleSize")))
      .andExpect(jsonPath("$.errors[0].entity", is("Survey")))
      .andExpect(jsonPath("$.errors[0].message", is("global.error.import.json-parsing-error")));
  }
  
  @Test
  public void testCreateSurveyWithUnparsableResponseRate() throws Exception {
    String survey = "{\"id\":\"6\"," + "\"dataAcquisitionProjectId\":\"Renes Projekt\","
        + "\"title\":{\"en\":\"High school graduates 2008: Third Wave\","
        + "\"de\":\"Bildungs-, Berufs- und Lebenswege - Dritte Befragung der Schulabsolventinnen und -absolventen des Jahrgangs 2007/2008\"},"
        + "\"responseRate\":\"6te\","
        + "\"fieldPeriod\":{\"start\":\"2012-12-01\",\"end\":\"2013-04-01\"}}";

    // create the survey with the given id but without a project
    mockMvc.perform(put(API_SURVEYS_URI + "/6").content(survey).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].invalidValue", is("6te")))
      .andExpect(jsonPath("$.errors[0].property", is("responseRate")))
      .andExpect(jsonPath("$.errors[0].entity", is("Survey")))
      .andExpect(jsonPath("$.errors[0].message", is("global.error.import.json-parsing-error")));
  }

  @Test
  public void testDeleteSurvey() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    // create the survey with the given id
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // delete the survey
    mockMvc.perform(delete(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the survey is really deleted
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().isNotFound());
    
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there are no more survey documents
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0L));
  }

  @Test
  public void testUpdateSurvey() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    // create the survey with the given id
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
    
    // delete the survey
    mockMvc.perform(delete(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().is2xxSuccessful());
    
    // update the survey
    survey.getTitle()
      .setDe("titel2");
    
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
        .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    // get the survey and check the updated title and version
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(survey.getId())))
      .andExpect(jsonPath("$.version", is(0)))
      .andExpect(jsonPath("$.title.de", is("titel2")));
    
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one survey documents
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testDeletingProjectDeletesSurvey() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    // check that the survey is present
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(survey.getId())))
      .andExpect(jsonPath("$.version", is(0)));

    // delete the project
    mockMvc.perform(delete("/api/data-acquisition-projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the survey has been deleted as well
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().isNotFound());

  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopySurvey() throws Exception {
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey("issue1991");
    survey.setId(survey.getId() + "-1.0.0");

    mockMvc.perform(post(API_SURVEYS_URI)
        .content(TestUtil.convertObjectToJsonBytes(survey))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUpdateShadowCopySurvey() throws Exception {
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey("issue1991");
    survey.setId(survey.getId() + "-1.0.0");
    surveyRepository.save(survey);

    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
        .content(TestUtil.convertObjectToJsonBytes(survey))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testDeleteShadowCopySurvey() throws Exception {
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey("issue1991");
    survey.setId(survey.getId() + "-1.0.0");
    surveyRepository.save(survey);

    mockMvc.perform(delete(API_SURVEYS_URI + "/" + survey.getId()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }
}
