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
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.filters.FilterManager;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class FilterManagerTest extends AbstractWebTest {

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;

  private FilterManager filterManager;

  private List<Terms.Bucket> buckets;

  @Before
  public void beforeTest() {
    this.filterManager = new FilterManager(this.scaleLevelProvider);

    this.buckets = new ArrayList<>();

    buckets.add(Mockito.mock(Terms.Bucket.class));
    buckets.add(Mockito.mock(Terms.Bucket.class));
    buckets.add(Mockito.mock(Terms.Bucket.class));
  }

  @Test
  public void testUpdateScaleLevelFilters() {
    //Arrange
    
    //Act
    this.filterManager.updateScaleLevelFilters(this.buckets,
        this.scaleLevelProvider.getOrdinalByLocal());
    
    //Assert
    assertThat(this.filterManager.getScaleLevelFilters().size(), is(3));
  }

  @Test
  public void testUpdateScaleLevelFiltersNull() {
    //Arrange
    
    //Act
    this.filterManager.updateScaleLevelFilters(this.buckets, null);
    
    //Assert
    assertThat(this.filterManager.getScaleLevelFilters().size(), is(3));
  }
}
