/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search.dto;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AggregationType;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.FilterType;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionSearchFilterTest {
  @Test
  public void testGetAllFiltersEmptyMap() {
    // Arrange
    QuestionSearchFilter emptysearchFilter = new QuestionSearchFilter();

    // Act
    Map<DocumentField, String> emptyMap = emptysearchFilter.getAllFilterValues();

    // Assert
    assertThat(emptyMap.size(), is(0));
  }

  @Test
  public void testGetAllFilters() {
    // Arrange
    QuestionSearchFilter searchFilter = new QuestionSearchFilter();
    searchFilter.setQuery("Query");

    // Act
    Map<DocumentField, String> filterMap = searchFilter.getAllFilterValues();

    // Assert
    assertThat(filterMap.size(), is(0));
    assertThat(searchFilter.getQuery(), is("Query"));
  }

  @Test
  public void testGetFilterFields() {
    // Arrange
    QuestionSearchFilter searchFilter = new QuestionSearchFilter();

    // Act
    Map<DocumentField, FilterType> filterFields = searchFilter.getAllFilterTypes();

    // Assert
    assertThat(filterFields.size(), is(0));
  }

  @Test
  public void testGetAggrogationFields() {
    // Arrange
    QuestionSearchFilter searchFilter = new QuestionSearchFilter();

    // Act
    Map<DocumentField, AggregationType> aggrogationFields = searchFilter.getAllAggregationTypes();

    // Assert
    assertThat(aggrogationFields.size(), is(0));
  }
}
