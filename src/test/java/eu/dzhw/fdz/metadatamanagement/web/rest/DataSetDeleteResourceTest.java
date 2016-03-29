package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import eu.dzhw.fdz.metadatamanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestCreateDomainObjectUtils;

/**
 * Test the REST API for {@link DataSetDeleteResource}.
 *
 */
public class DataSetDeleteResourceTest extends AbstractTest {
  
  private static final String API_DATA_SETS_URI = "/api/data_sets";
  private static final String API_DATA_SETS_DELETE_URI = "/api/data_sets/delete";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository rdcProjectRepository;
  
  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private DataSetRepository dataSetRepository;

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
    dataSetRepository.deleteAll();
  }


  @Test
  public void testDeletingProjectDeletesDataSets() throws Exception {
    
    // create the DataAcquisitionProject
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testId")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    rdcProjectRepository.save(project);
    
    // create the Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);
    
    List<String> variablesId = new ArrayList<String>();
    variablesId.add("testID");
    
    List<String> surveyIds = new ArrayList<String>();
    surveyIds.add(survey.getId());
    
    // create the dataSet
    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    dataSetRepository.save(dataSet);
    
    // check that the survey is present
    mockMvc.perform(get(API_DATA_SETS_URI + "/" + dataSet.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(dataSet.getId())))
      .andExpect(jsonPath("$.version", is(0)));

    // delete the Survey
     mockMvc.perform( post(API_DATA_SETS_DELETE_URI+"?dataAcquisitionProjectId="+project.getId())
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // check that the survey has been deleted
     mockMvc.perform(get(API_DATA_SETS_URI + "/" + dataSet.getId() + "?projection=complete"))
     .andExpect(status().isNotFound());
  }
}
