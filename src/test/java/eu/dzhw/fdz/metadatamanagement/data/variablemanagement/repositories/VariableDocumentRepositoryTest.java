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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFilter;

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

  @Before
  public void beforeTest() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey =
          new VariableSurveyBuilder()
              .withSurveyId("SurveyFilterUnitTest_Survey_ID")
              .withTitle("SurveyFilterUnitTestTitle 0" + i)
              .withVariableAlias("SurveyFilterUnitTestVariableAlias 0" + i)
              .withSurveyPeriod(
                  new DateRangeBuilder().withEndDate(LocalDate.now().plusMonths(i))
                      .withStartDate(LocalDate.now().minusMonths(i)).build()).build();

      VariableDocument variableDocument =
          new VariableDocumentBuilder().withId("SurveyFilterUnitTest_ID0" + i)
              .withName("SurveyFilterUnitTestName 0" + i)
              .withLabel("SurveyFilterUnitTestLabel 0" + i)
              .withQuestion("SurveyFilterUnitTestQuestion 0" + i)
              .withDataType(this.dataTypesProvider.getNumericValueByLocale())
              .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
              .withVariableSurvey(variableSurvey).build();
      this.variablesRepository.save(variableDocument);
    }
  }

  @After
  public void afterTest() {
    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variablesRepository.delete("SurveyFilterUnitTest_ID0" + i);
    }
  }

  @Test
  public void testMatchFilterBySurveyIdGerman() {

    // Arrange


    // Act
    Page<VariableDocument> result =
        this.variablesRepository.filterBySurveyIdAndVariableAlias("SurveyFilterUnitTest_Survey_ID",
            "SurveyFilterUnitTestVariableAlias 08");

    // Assert
    assertThat(result.getNumberOfElements(), is(1));


  }

  @Test
  public void testSearch() {

    // Arrange
    VariableSearchFilter variableSearchFormDto = new VariableSearchFilter();
    variableSearchFormDto.setQuery("query");
    variableSearchFormDto.setScaleLevel(this.scaleLevelProvider.getMetricByLocal());
    variableSearchFormDto.setSurveyTitle("SurveyTitle");
    variableSearchFormDto.setSurveyPeriod(new DateRangeBuilder()
        .withStartDate(LocalDate.now().minusDays(2)).withEndDate(LocalDate.now()).build());

    // Act
    PageWithBuckets<VariableDocument> pageWithBuckets =
        this.variablesRepository.search(variableSearchFormDto, Mockito.mock(Pageable.class));

    // Assert
    assertThat(pageWithBuckets, not(nullValue()));
    assertThat(pageWithBuckets.getBucketMap().size(), is(2));
    assertThat(pageWithBuckets.getBucketMap().get(VariableDocument.SCALE_LEVEL_FIELD),
        not(nullValue()));
    assertThat(
        pageWithBuckets.getBucketMap()
            .get(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getLeafSubField()).size(),
        not(nullValue()));
  }

  @Test
  public void testSearchDateRangeFilterCase1() {
    // Test Case 1: Find all
    // SurveyPeriod: --[-----]--
    // Filter: ----------[-]----

    // Arrange
    VariableSearchFilter variableSearchFormDto = new VariableSearchFilter();
    variableSearchFormDto.setSurveyPeriod(new DateRangeBuilder()
        .withStartDate(LocalDate.now().minusDays(2)).withEndDate(LocalDate.now()).build());

    // Act
    PageWithBuckets<VariableDocument> pageWithBuckets =
        this.variablesRepository.search(variableSearchFormDto, new PageRequest(0, 10));


    // Assert
    assertThat(pageWithBuckets, not(nullValue()));
    assertThat(pageWithBuckets.getContent().size(), is(9));
  }


  @Test
  public void testSearchDateRangeFilterCase2() {
    // Test Case 2: Find all
    // SurveyPeriod: --[-----]----
    // Filter: ------[---------]--

    // Arrange
    VariableSearchFilter variableSearchFormDto = new VariableSearchFilter();
    variableSearchFormDto.setSurveyPeriod(new DateRangeBuilder()
        .withStartDate(LocalDate.now().minusMonths(12)).withEndDate(LocalDate.now().plusMonths(12))
        .build());

    // Act
    PageWithBuckets<VariableDocument> pageWithBuckets =
        this.variablesRepository.search(variableSearchFormDto, new PageRequest(0, 10));


    // Assert
    assertThat(pageWithBuckets, not(nullValue()));
    assertThat(pageWithBuckets.getContent().size(), is(9));
  }

  @Test
  public void testSearchDateRangeFilterCase3() {
    // Test Case 3: Find nothing
    // SurveyPeriod: --------[-----]--
    // Filter: -----[-----]-----------

    // Arrange
    VariableSearchFilter variableSearchFormDto = new VariableSearchFilter();
    variableSearchFormDto.setSurveyPeriod(new DateRangeBuilder()
        .withStartDate(LocalDate.now().minusMonths(24))
        .withEndDate(LocalDate.now().minusMonths(12)).build());

    // Act
    PageWithBuckets<VariableDocument> pageWithBuckets =
        this.variablesRepository.search(variableSearchFormDto, new PageRequest(0, 10));


    // Assert
    assertThat(pageWithBuckets, not(nullValue()));
    assertThat(pageWithBuckets.getContent().size(), is(0));
  }

  @Test
  public void testSearchDateRangeFilterCase4() {
    // Test Case 4: Find nothing
    // SurveyPeriod: --[-----]------------
    // Filter: ------------------[-----]--

    // Arrange
    VariableSearchFilter variableSearchFormDto = new VariableSearchFilter();
    variableSearchFormDto.setSurveyPeriod(new DateRangeBuilder()
        .withStartDate(LocalDate.now().plusMonths(12)).withEndDate(LocalDate.now().plusMonths(24))
        .build());

    // Act
    PageWithBuckets<VariableDocument> pageWithBuckets =
        this.variablesRepository.search(variableSearchFormDto, new PageRequest(0, 10));


    // Assert
    assertThat(pageWithBuckets, not(nullValue()));
    assertThat(pageWithBuckets.getContent().size(), is(0));
  }

  @Test
  public void testSearchDateRangeFilterCase5() {
    // Test Case 5: Find all, which are in the the filter Range
    // SurveyPeriod: ---[-----]--
    // Filter: ------[-----]-----

    // Arrange
    VariableSearchFilter variableSearchFormDto = new VariableSearchFilter();
    variableSearchFormDto.setSurveyPeriod(new DateRangeBuilder()
        .withStartDate(LocalDate.now().minusMonths(12)).withEndDate(LocalDate.now().minusMonths(5))
        .build());

    // Act
    PageWithBuckets<VariableDocument> pageWithBuckets =
        this.variablesRepository.search(variableSearchFormDto, new PageRequest(0, 10));


    // Assert
    assertThat(pageWithBuckets, not(nullValue()));
    assertThat(pageWithBuckets.getContent().size(), is(5));
  }


  @Test
  public void testSearchDateRangeFilterCase6() {
    // Test Case 6: Find all, which are in the the filter Range
    // SurveyPeriod: --[-----]-----
    // Filter: -----------[-----]--

    // Arrange
    VariableSearchFilter variableSearchFormDto = new VariableSearchFilter();
    variableSearchFormDto.setSurveyPeriod(new DateRangeBuilder()
        .withStartDate(LocalDate.now().plusMonths(5)).withEndDate(LocalDate.now().plusMonths(12))
        .build());

    // Act
    PageWithBuckets<VariableDocument> pageWithBuckets =
        this.variablesRepository.search(variableSearchFormDto, new PageRequest(0, 10));


    // Assert
    assertThat(pageWithBuckets, not(nullValue()));
    assertThat(pageWithBuckets.getContent().size(), is(5));
  }
}
