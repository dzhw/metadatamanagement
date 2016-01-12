/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.builders.FdzProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.VariableBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityExistsException;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityNotFoundException;

/**
 * @author Daniel Katzberg
 *
 */
public class FdzProjectServiceTest extends AbstractTest {

  @Inject
  private FdzProjectRepository fdzProjectRepository;

  @Inject
  private SurveyRepository surveyRepository;

  @Inject
  private VariableRepository variableRepository;

  @Inject
  private FdzProjectService fdzProjectService;
  

  private FdzProject fdzProject;

  @Before
  public void beforeTest() {
    this.fdzProject = new FdzProjectBuilder().withCufDoi("CufDoi")
      .withName("Name")
      .withSufDoi("SufDoi")
      .build();
  }

  @After
  public void afterEachTest() {
    this.fdzProjectRepository.delete(this.fdzProject.getName());
  }

  @Test(expected = EntityExistsException.class)
  public void testDuplicatedFdzProject() {
    // Arrange

    // Act
    this.fdzProjectService.createFdzProject(this.fdzProject);
    this.fdzProjectService.createFdzProject(this.fdzProject);

    // Assert
  }

  @Test(expected = EntityNotFoundException.class)
  public void testNotUpdateableFdzProject() {
    // Arrange

    // Act
    this.fdzProjectService.updateFdzProject(this.fdzProject);

    // Assert
  }

  @Test
  public void testFindAll() {
    // Arrange
    this.fdzProjectRepository.insert(this.fdzProject);

    // Act
    List<FdzProject> allFdzProjects = this.fdzProjectService.findAll();

    // Assert
    assertThat(allFdzProjects.size(), is(1));
    assertThat(allFdzProjects.get(0), is(this.fdzProject));
  }

  @Test
  public void testDeleteProject() {
    // Arrange
    this.fdzProjectRepository.insert(this.fdzProject);
    
    Survey survey = new SurveyBuilder().withFdzProjectName("Name")
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("titel")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withId("Id")
      .build();
    Survey survey2 = new SurveyBuilder().withFdzProjectName("Name")
      .withTitle(new I18nStringBuilder().withDe("titel2")
        .withEn("titel2")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withId("Id2")
      .build();
    
    this.surveyRepository.insert(survey);
    this.surveyRepository.insert(survey2);

    Variable variable1 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id1")
      .withFdzProjectName("Name")
      .withSurveyId("Id")
      .withName("Name1")
      .withLabel("Label")
      .build();

    Variable variable2 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id2")
      .withFdzProjectName("Name")
      .withSurveyId("Id")
      .withName("Name2")
      .withLabel("Label")
      .build();

    Variable variable3 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id3")
      .withFdzProjectName("Name")
      .withSurveyId("Id2")
      .withName("Name3")
      .withLabel("Label")
      .build();

    Variable variable4 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id4")
      .withFdzProjectName("Name")
      .withSurveyId("Id2")
      .withName("Name4")
      .withLabel("Label")
      .build();

    this.variableRepository.save(variable1);
    this.variableRepository.save(variable2);
    this.variableRepository.save(variable3);
    this.variableRepository.save(variable4);

    // Act
    this.fdzProjectService.deleteByName("Name");

    // Assert
    assertThat(this.fdzProjectRepository.findOne("Name"), is(nullValue()));
    assertThat(this.surveyRepository.findOne("Id"), is(nullValue()));
    assertThat(this.surveyRepository.findOne("Id2"), is(nullValue()));
    assertThat(this.variableRepository.findOne("Id1"), is(nullValue()));
    assertThat(this.variableRepository.findOne("Id2"), is(nullValue()));
    assertThat(this.variableRepository.findOne("Id3"), is(nullValue()));
    assertThat(this.variableRepository.findOne("Id4"), is(nullValue()));
  }

}
