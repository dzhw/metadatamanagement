/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.common.FilterManager;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.builders.VariableSearchFormDtoBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class FilterManagerTest {


  @Test
  public void testMergeFilterWithDtoInformationEmptyFilterMap() {
    // Arrange
    Map<String, List<Bucket>> filterMap = new HashMap<>();
    VariableSearchFormDto variableSearchFormDto = new VariableSearchFormDtoBuilder()
        .withQuery("EmptyQuery").withScaleLevel("nominal").build();
    FilterManager filterManager = new FilterManager(variableSearchFormDto, filterMap);

    // Act

    // Assert
    assertThat(filterManager.getFilterMap().keySet().size(), is(1));
    assertThat(filterManager.getFilterMap().keySet().iterator().next(), is(VariableDocument.SCALE_LEVEL_FIELD));
    assertThat(filterManager.getFilterMap().get(VariableDocument.SCALE_LEVEL_FIELD).get(0).getKey(), is("nominal"));
  }
  
  @Test
  public void testMergeFilterWithDtoInformationFilledMap() {
    // Arrange
    List<Bucket> filterList = new ArrayList<>();
    Map<String, List<Bucket>> filterMap = new HashMap<>();
    filterMap.put(VariableDocument.SCALE_LEVEL_FIELD, filterList);
    VariableSearchFormDto variableSearchFormDto = new VariableSearchFormDtoBuilder()
        .withQuery("EmptyQuery").withScaleLevel("ordinal").build();
    FilterManager filterManager = new FilterManager(variableSearchFormDto, filterMap);

    // Act

    // Assert
    assertThat(filterManager.getFilterMap().keySet().size(), is(1));
    assertThat(filterManager.getFilterMap().keySet().iterator().next(), is(VariableDocument.SCALE_LEVEL_FIELD));
    assertThat(filterManager.getFilterMap().get(VariableDocument.SCALE_LEVEL_FIELD).get(0).getKey(), is("ordinal"));
  }
}
