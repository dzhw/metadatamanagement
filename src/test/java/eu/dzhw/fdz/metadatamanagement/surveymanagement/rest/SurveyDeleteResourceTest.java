package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

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
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);
    
    // create the Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    // check that the survey is present
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId() + "-sy1")))
      .andExpect(jsonPath("$.version", is(0)));

    // delete the Survey
     mockMvc.perform( post(API_SURVEYS_DELETE_URI+"?dataAcquisitionProjectId="+project.getId())
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // check that the survey has been deleted
     mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
     .andExpect(status().isNotFound());
  }
}
