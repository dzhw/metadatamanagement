/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableDocumentRepositoryTest extends AbstractWebTest {

  @Autowired
  private VariableDocumentRepository variablesRepository;

  @Autowired
  private DataTypesProvider dataTypesProvider;

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;

  @Test
  public void testMatchFilterBySurveyIdGerman() {

    // Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey =
          new VariableSurveyBuilder().withSurveyId("SurveyFilterUnitTest_Survey_ID")
              .withTitle("SurveyFilterUnitTestTitle 0" + i)
              .withVariableAlias("SurveyFilterUnitTestVariableAlias 0" + i).build();

      VariableDocument variableDocument = new VariableDocumentBuilder()
          .withId("SurveyFilterUnitTest_ID0" + i).withName("SurveyFilterUnitTestName 0" + i)
          .withLabel("SurveyFilterUnitTestLabel 0" + i)
          .withQuestion("SurveyFilterUnitTestQuestion 0" + i)
          .withDataType(this.dataTypesProvider.getNumericValueByLocale())
          .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
          .withVariableSurvey(variableSurvey).build();
      this.variablesRepository.save(variableDocument);
    }

    // Act
    Page<VariableDocument> result = this.variablesRepository.filterBySurveyIdAndVariableAlias(
        "SurveyFilterUnitTest_Survey_ID", "SurveyFilterUnitTestVariableAlias 08");

    // Assert
    assertThat(result.getNumberOfElements(), is(1));

    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variablesRepository.delete("SurveyFilterUnitTest_ID0" + i);
    }
  }

  @Test
  public void testSearch() {

    // Arrange
    VariableSearchFormDto variableSearchFormDto = new VariableSearchFormDto();
    variableSearchFormDto.setQuery("query");
    variableSearchFormDto.setScaleLevel(this.scaleLevelProvider.getMetricByLocal());
    variableSearchFormDto.setSurveyTitle("SurveyTitle");
    variableSearchFormDto.setDateRange(new DateRangeBuilder()
        .withStartDate(LocalDate.now().minusDays(2)).withEndDate(LocalDate.now()).build());

    // Act
    PageWithBuckets<VariableDocument> pageWithBuckets =
        this.variablesRepository.search(variableSearchFormDto, Mockito.mock(Pageable.class));
    
    // Assert
    assertThat(pageWithBuckets, not(nullValue()));
    assertThat(pageWithBuckets.getBucketMap().size(), is(2));
    assertThat(pageWithBuckets.getBucketMap().get(VariableDocument.SCALE_LEVEL_FIELD.getPath()),
        not(nullValue()));
    assertThat(pageWithBuckets.getBucketMap()
        .get("variableSurvey.title").size(), not(nullValue()));
  }
}
