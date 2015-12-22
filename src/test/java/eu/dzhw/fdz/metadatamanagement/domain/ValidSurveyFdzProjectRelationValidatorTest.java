/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.builders.FdzProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.VariableBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidSurveyFdzProjectRelationValidator;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;

/**
 * @author Daniel Katzberg
 *
 */
public class ValidSurveyFdzProjectRelationValidatorTest extends AbstractTest {

  @Inject
  private FdzProjectRepository fdzProjectRepository;

  @Inject
  private SurveyRepository surveyRepository;

  private ValidSurveyFdzProjectRelationValidator validator;

  @Before
  public void beforeEachTest() {
    this.validator = new ValidSurveyFdzProjectRelationValidator();
    ReflectionTestUtils.setField(this.validator, "surveyRepository", this.surveyRepository);

    // Add Project
    FdzProject fdzProject = new FdzProjectBuilder().withName("One Name")
      .withCufDoi("CufDoi")
      .withSufDoi("SufDoi")
      .build();
    this.fdzProjectRepository.insert(fdzProject);

    // Add Survey
    Survey survey = new SurveyBuilder().withFdzProjectName("One Name")
      .withId("Id")
      .withTitle(new I18nString())
      .withFieldPeriod(new Period())
      .build();
    this.surveyRepository.insert(survey);
  }

  @After
  public void afterEachTest() {
    this.fdzProjectRepository.deleteAll();
    this.surveyRepository.deleteAll();
  }

  @Test
  public void testValidationWithoutProjectAndSurvey() {
    // Arrange
    Variable variable = new VariableBuilder().build();

    // Act
    boolean valid = this.validator.isValid(variable, null);

    // Assert
    assertThat(valid, is(true));
  }

  @Test
  public void testValidationWithoutProjectButWithSurvey() {
    // Arrange
    Variable variable = new VariableBuilder().withSurveyId("Id")
      .build();

    // Act
    boolean valid = this.validator.isValid(variable, null);

    // Assert
    assertThat(valid, is(false));
  }

  @Test
  public void testValidationWithWrongProject() {
    // Arrange
    Variable variable = new VariableBuilder().withSurveyId("Id")
      .withFdzProjectName("Second Name")
      .build();

    // Act
    boolean valid = this.validator.isValid(variable, null);

    // Assert
    assertThat(valid, is(false));
  }
  
  @Test
  public void testValidationWithCorrectProject() {
    // Arrange
    Variable variable = new VariableBuilder().withSurveyId("Id")
      .withFdzProjectName("One Name")
      .build();

    // Act
    boolean valid = this.validator.isValid(variable, null);

    // Assert
    assertThat(valid, is(true));
  }
}
