package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;

/**
 * Test the REST API for {@link SurveysDeleteResource}.
 *
 */
public class SurveyDeleteResourceTest extends AbstractTest {
  private static final String API_SURVEYS_URI = "/api/surveys";
  private static final String API_SURVEYS_DELETE_URI = "/api/surveys/delete";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository rdcProjectRepository;

  @Autowired
  private SurveyRepository surveyRepository;

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
  }


  @Test
  public void testDeletingProjectDeletesSurvey() throws Exception {
    
    // create the DataAcquisitionProject
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testId")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    rdcProjectRepository.save(project);
    
    // create the Survey
    Survey survey = new SurveyBuilder().withId("testId")
      .withDataAcquisitionProjectId(project.getId())
      .withQuestionnaireId("QuestionnaireId")
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .plusDays(1))
        .build())
      .build();
    surveyRepository.save(survey);

    // check that the survey is present
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId())))
      .andExpect(jsonPath("$.version", is(0)));

    // delete the Survey
     mockMvc.perform( post(API_SURVEYS_DELETE_URI+"?dataAcquisitionProjectId="+project.getId())
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // check that the survey has been deleted
     mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
     .andExpect(status().isNotFound());
  }
}
