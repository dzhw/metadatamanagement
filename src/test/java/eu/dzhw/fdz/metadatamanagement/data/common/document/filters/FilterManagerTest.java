/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.document.filters;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.filters.FilterManager;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto;

/**
 * @author Daniel Katzberg
 *
 */
public class FilterManagerTest extends AbstractWebTest {

  private FilterManager filterManager;

  private List<Terms.Bucket> buckets;

  @Before
  public void beforeTest() {
    this.filterManager = new FilterManager(new VariableSearchFormDto());

    this.buckets = new ArrayList<>();

    buckets.add(Mockito.mock(Terms.Bucket.class));
    buckets.add(Mockito.mock(Terms.Bucket.class));
    buckets.add(Mockito.mock(Terms.Bucket.class));
  }

  @Test
  public void testUpdateScaleLevelFilters() {
    //Arrange
    
    //Act
    this.filterManager.updateAllFilter(this.buckets);
    
    //Assert
    assertThat(this.filterManager.getFilterMap().values().size(), is(1));
  }

  @Test
  public void testUpdateScaleLevelFiltersNull() {
    //Arrange
    
    //Act
    this.filterManager.updateAllFilter(this.buckets);
    
    //Assert
    assertThat(this.filterManager.getFilterMap().values().size(), is(1));
  }
}
