/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

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
public class SurveyServiceTest extends AbstractTest {
  private static final String TEST_PROJECT = "Project1";

  @Inject
  private SurveyRepository surveyRepository;

  @Inject
  private FdzProjectRepository fdzProjectRepository;

  @Inject
  private SurveyService surveyService;

  @Inject
  private VariableRepository variableRepository;

  private Survey survey;

  private FdzProject fdzProject;

  @Before
  public void beforeTest() {
    this.survey = new SurveyBuilder().withFdzProjectName(TEST_PROJECT)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("titel")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withId("Id")
      .build();

    this.fdzProject = new FdzProjectBuilder().withName(TEST_PROJECT)
      .build();
    this.fdzProjectRepository.insert(fdzProject);
  }

  @After
  public void afterEachTest() {
    this.surveyRepository.deleteAll();
    this.fdzProjectRepository.deleteAll();
  }

  @Test(expected = EntityExistsException.class)
  public void testDuplicatedSurvey() {
    // Arrange

    // Act
    this.surveyService.createSurvey(this.survey);
    this.surveyService.createSurvey(this.survey);

    // Assert
  }

  @Test(expected = EntityNotFoundException.class)
  public void testNotUpdateableSurvey() {
    // Arrange

    // Act
    this.surveyService.updateSurvey(this.survey);

    // Assert
  }

  @Test
  public void testEquals() {

    // Arrange
    Survey survey2 = new SurveyBuilder().withFdzProjectName(TEST_PROJECT)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("titel")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withId("Id2")
      .build();
    Survey survey1_1 = new SurveyBuilder().withFdzProjectName(TEST_PROJECT)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("titel")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withId("Id")
      .build();

    // Act
    boolean checkNull = this.survey.equals(null);
    boolean checkClass = this.survey.equals(new Object());
    boolean checkSame = this.survey.equals(this.survey);
    boolean checkDifferent = this.survey.equals(survey2);
    boolean checkSameButDifferent = this.survey.equals(survey1_1);

    // Assert
    assertThat(checkNull, is(false));
    assertThat(checkClass, is(false));
    assertThat(checkSame, is(true));
    assertThat(checkDifferent, is(false));
    assertThat(checkSameButDifferent, is(true));
  }

  @Test
  public void testDeleteProject() {
    // Arrange
    this.surveyRepository.insert(this.survey);

    Variable variable1 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id1")
      .withFdzProjectName(TEST_PROJECT)
      .withSurveyId("Id")
      .withName("Name1")
      .withLabel("Label")
      .build();

    Variable variable2 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id2")
      .withFdzProjectName(TEST_PROJECT)
      .withSurveyId("Id")
      .withName("Name2")
      .withLabel("Label")
      .build();

    Variable variable3 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id3")
      .withFdzProjectName(TEST_PROJECT)
      .withSurveyId("Id")
      .withName("Name3")
      .withLabel("Label")
      .build();

    this.variableRepository.save(variable1);
    this.variableRepository.save(variable2);
    this.variableRepository.save(variable3);

    // Act
    this.surveyService.delete("Id");

    // Assert
    assertThat(this.surveyRepository.findOne("Id"), is(nullValue()));
    assertThat(this.variableRepository.findOne("Id1"), is(nullValue()));
    assertThat(this.variableRepository.findOne("Id2"), is(nullValue()));
    assertThat(this.variableRepository.findOne("Id3"), is(nullValue()));
  }
}
