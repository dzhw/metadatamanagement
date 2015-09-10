/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.FacetedPage;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class PageWithBucketsTest {


  @SuppressWarnings("unchecked")
  @Test
  public void testHashCode() {
    // Arrange
    FacetedPage<VariableDocument> facetedPage = Mockito.mock(FacetedPage.class);
    Pageable pageable = Mockito.mock(Pageable.class);
    Map<String, List<Bucket>> bucketMap = new HashMap<>();
    PageWithBuckets<VariableDocument> pageWithBuckets =
        new PageWithBuckets<>(facetedPage, pageable, bucketMap);

    // Act

    // Assert
    assertThat(pageWithBuckets.hashCode(), not(0));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testEquals() {
    //Arrange
    FacetedPage<VariableDocument> facetedPage = Mockito.mock(FacetedPage.class);
    Pageable pageable = Mockito.mock(Pageable.class);
    Map<String, List<Bucket>> bucketMap = new HashMap<>();
    Map<String, List<Bucket>> bucketMap2 = new HashMap<>();
    List<Bucket> buckets = new ArrayList<>();
    bucketMap2.put(VariableDocument.SCALE_LEVEL_FIELD, buckets);
    PageWithBuckets<VariableDocument> pageWithBuckets =
        new PageWithBuckets<>(facetedPage, pageable, bucketMap);
    PageWithBuckets<VariableDocument> pageWithBuckets2 =
        new PageWithBuckets<>(facetedPage, pageable, bucketMap2);

    //Act
    boolean checkSame = pageWithBuckets.equals(pageWithBuckets);
    boolean checkNull = pageWithBuckets.equals(null);
    boolean checkOtherClass = pageWithBuckets.equals(new Object());
    boolean checkDifferent = pageWithBuckets.equals(pageWithBuckets2);
    
    //Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkDifferent);
  }

}
