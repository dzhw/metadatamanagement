package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

/**
 * Test the REST API for {@link Survey}s.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class SurveyResourceTest extends AbstractTest {
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

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    rdcProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    elasticsearchUpdateQueueService.clearQueue();
  }

  @Test
  public void testCreateValidSurvey() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    // create the survey with the given id
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isCreated());

    // read the survey under the new url
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().isOk());

    // call toString for test coverage :-)
    survey.toString();
    
    elasticsearchUpdateQueueService.processQueue();

    // check that there are two survey documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }

  @Test
  public void testCreateSurveyWithInvalidPeriod() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testId")
      .withDataAcquisitionProjectId(project.getId())
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .minusDays(1))
        .build())
      .build();

    // create the survey with the given id but with wrong period
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void testCreateSurveyWithUnlimitedPeriod() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    survey.setFieldPeriod(new PeriodBuilder().withStart(null)
        .withEnd(null)
      .build());

    // create the survey with the given id but with an unlimited period
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateSurveyWithInvalidProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();

    Survey survey = new SurveyBuilder().withId("testId")
      .withDataAcquisitionProjectId(project.getId())
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .plusDays(1))
        .build())
      .build();

    // create the survey with the given id but with an unknown project
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateSurveyEmptyProject() throws Exception {
    Survey survey = new SurveyBuilder().withId("testId")
      .withDataAcquisitionProjectId(null)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .plusDays(1))
        .build())
      .build();

    // create the survey with the given id but without a project
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateUnparsableSurvey() throws Exception {
    String survey = "{\"id\":\"6\"," + "\"dataAcquisitionProjectId\":\"Renes Projekt\","
        + "\"title\":{\"en\":\"High school graduates 2008: Third Wave\","
        + "\"de\":\"Bildungs-, Berufs- und Lebenswege - Dritte Befragung der Schulabsolventinnen und -absolventen des Jahrgangs 2007/2008\"},"
        + "\"fieldPeriod\":{\"start\":\"2012-12-01\",\"end\":\"2013-04-01d\"}}";

    // create the survey with the given id but without a project
    mockMvc.perform(put(API_SURVEYS_URI + "/6").content(survey))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("2013-04-01d")));
  }

  @Test
  public void testDeleteSurvey() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    // create the survey with the given id
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isCreated());

    // delete the survey
    mockMvc.perform(delete(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the survey is really deleted
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().isNotFound());
    
    elasticsearchUpdateQueueService.processQueue();

    // check that there are no more survey documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }

  @Test
  public void testUpdateSurvey() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    // create the survey with the given id
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isCreated());

    // update the survey
    survey.getTitle()
      .setDe("titel2");
    mockMvc.perform(put(API_SURVEYS_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isNoContent());

    // get the survey and check the updated title and version
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(survey.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.title.de", is("titel2")));
    
    elasticsearchUpdateQueueService.processQueue();

    // check that there are two survey documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }

  @Test
  public void testDeletingProjectDeletesSurvey() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    // check that the survey is present
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(survey.getId())))
      .andExpect(jsonPath("$.version", is(0)));

    // delete the project
    mockMvc.perform(delete("/api/data-acquisition-projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the survey has been deleted as well
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
      .andExpect(status().isNotFound());

  }
}
