package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;

/**
 * Test the REST API for {@link DataAcquisitionProject}s.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class DataAcquisitionProjectResourceTest extends AbstractTest {
  private static final String API_DATA_ACQUISITION_PROJECTS_URI = "/api/data-acquisition-projects";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository rdcProjectRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    rdcProjectRepository.deleteAll();
  }

  @Test
  public void testCreateDataAcquisitionProject() throws IOException, Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testId")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    // read the project under the new url
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().isOk());

    // call toString for test coverage :-)
    project.toString();
  }
  
  
  @Test
  public void testCreateDataAcquisitionProjectWithTooLongId() throws IOException, Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("ThisIdIsTooLongAndShouldByShortedByAnUser")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().is4xxClientError()).andExpect(jsonPath("$.errors[0].message", containsString("error.dataAcquisitionProject.id.size")));
  }
  
  @Test
  public void testCreateDataAcquisitionProjectWithTooSurveySeries() throws IOException, Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testId")
      .withSurveySeries(new I18nStringBuilder()
          .withDe("Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn")
          .withEn("Randomstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn")
          .build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testDeleteDataAcquisitionProject() throws IOException, Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testId")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc.perform(delete(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId()))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testCompleteProjectionContainsId() throws IOException, Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testId")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    // load the project with the projection
    mockMvc.perform(
        get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId())))
      .andExpect(jsonPath("$.version", is(0)));
  }

  @Test
  public void testUpdateProject() throws IOException, Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testId")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();

    // create the project with the given id
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isCreated());

    project.getPanelName()
      .setDe("germanTest");
    project.getPanelName()
      .setEn("englishTest");

    // update the project
    mockMvc.perform(put(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId())
      .content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().is2xxSuccessful());

    // load the project with the complete projection
    mockMvc.perform(
        get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + project.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.panelName.de", is("germanTest")))
      .andExpect(jsonPath("$.panelName.en", is("englishTest")));
  }

  @Test
  public void testIdIsMandatory() throws IOException, Exception {
    DataAcquisitionProject project =
        new DataAcquisitionProjectBuilder().withSurveySeries(new I18nStringBuilder().build())
          .withPanelName(new I18nStringBuilder().build())
          .build();

    // create the project without id
    mockMvc.perform(
        post(API_DATA_ACQUISITION_PROJECTS_URI).content(TestUtil.convertObjectToJsonBytes(project)))
      .andExpect(status().isBadRequest())
      .andExpect(
          jsonPath("$.errors[0].message", containsString("dataAcquisitionProject.error.dataAcquisitionProject.id.notEmpty")));
  }
}
