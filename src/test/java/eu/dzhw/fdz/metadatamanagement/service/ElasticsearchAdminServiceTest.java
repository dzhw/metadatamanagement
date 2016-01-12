package eu.dzhw.fdz.metadatamanagement.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.builders.FdzProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.VariableBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.search.ElasticsearchAdminDao;
import eu.dzhw.fdz.metadatamanagement.search.VariableSearchDao;

/**
 * Test which ensures that all indices are created successfully.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class ElasticsearchAdminServiceTest extends AbstractTest {

  @Inject
  private ElasticsearchAdminService elasticsearchAdminService;

  @Inject
  private VariableService variableService;
  
  @Inject
  private FdzProjectService fdzProjectService;

  @Inject
  private ElasticsearchAdminDao elasticsearchAdminDao;
  
  @Inject 
  private VariableSearchDao variableSearchDao;
  
  private FdzProject fdzProject;

  @Before
  public void initFdzProjects() {
    this.fdzProject = new FdzProjectBuilder().withName("Project")
        .withCufDoi("CufDoi")
        .withSufDoi("SufDoi")
        .build();
    this.fdzProjectService.createFdzProject(this.fdzProject);
  }
  
  @After
  public void cleanUp() {
      this.fdzProjectService.deleteByName(this.fdzProject.getName());
      for (String index : ElasticsearchAdminService.INDICES) {
        this.variableSearchDao.refresh(index);
      }
      this.variableService.deleteAll();
  }

  @Test
  public void testRecreateAllIndices() {
    Variable variable1 = new VariableBuilder().withId("1234")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withName("name")
      .withLabel("label")
      .withFdzProjectName(this.fdzProject.getName())
      .build();
    variableService.createVariable(variable1);

    Variable variable2 = new VariableBuilder().withId("5678")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withName("name2")
      .withLabel("label")
      .withFdzProjectName(this.fdzProject.getName())
      .build();
    variableService.createVariable(variable2);

    elasticsearchAdminService.recreateAllIndices();

    for (String index : ElasticsearchAdminService.INDICES) {
      elasticsearchAdminDao.refresh(index);
      assertThat(variableSearchDao.findAll(index)
        .size(), equalTo(2));
    }
  }
}
