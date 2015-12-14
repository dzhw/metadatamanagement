/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service;

import java.time.LocalDate;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityExistsException;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityNotFoundException;

/**
 * @author Daniel Katzberg
 *
 */
public class SurveyServiceTest extends AbstractTest{
  @Inject
  private SurveyRepository surveyRepository;

  @Inject
  private SurveyService surveyService;

  private Survey survey;

  @Before
  public void beforeTest() {
    this.survey = new SurveyBuilder().withFdzProjectName("Project1")
      .withTitle(new I18nStringBuilder().withDe("titel").withEn("titel").build())
      .withFieldPeriod(LocalDate.now())
      .withId("Id")
      .build();
  }

  @After
  public void afterEachTest() {
    this.surveyRepository.delete(this.survey.getId());
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
}
