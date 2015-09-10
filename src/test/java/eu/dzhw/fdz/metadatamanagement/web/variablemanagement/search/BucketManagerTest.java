/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.BucketManager;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.builders.VariableSearchFormDtoBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class BucketManagerTest {


  @Test
  public void testMergeFilterWithDtoInformationEmptyFilterMap() {
    // Arrange
    Map<String, HashSet<Bucket>> filterMap = new HashMap<>();
    VariableSearchFormDto variableSearchFormDto = new VariableSearchFormDtoBuilder()
        .withQuery("EmptyQuery").withScaleLevel("nominal").build();

    Map<String, HashSet<Bucket>> extendedMap =
        BucketManager.addEmptyBucketsIfNecessary(variableSearchFormDto, filterMap);

    // Act

    // Assert
    assertThat(extendedMap.keySet().size(), is(1));
    assertThat(extendedMap.keySet().iterator().next(), is(VariableDocument.SCALE_LEVEL_FIELD));

    HashSet<Bucket> scaleLevelBuckets = extendedMap.get(VariableDocument.SCALE_LEVEL_FIELD);
    for (Bucket bucket : scaleLevelBuckets) {
      assertThat(bucket.getKey(), is("nominal"));
    }
  }

  @Test
  public void testMergeFilterWithDtoInformationFilledMap() {
    // Arrange
    HashSet<Bucket> filterList = new HashSet<>();
    Map<String, HashSet<Bucket>> filterMap = new HashMap<>();
    filterMap.put(VariableDocument.SCALE_LEVEL_FIELD, filterList);
    VariableSearchFormDto variableSearchFormDto = new VariableSearchFormDtoBuilder()
        .withQuery("EmptyQuery").withScaleLevel("ordinal").build();

    Map<String, HashSet<Bucket>> extendedMap =
        BucketManager.addEmptyBucketsIfNecessary(variableSearchFormDto, filterMap);

    // Act

    // Assert
    assertThat(extendedMap.keySet().size(), is(1));
    assertThat(extendedMap.keySet().iterator().next(), is(VariableDocument.SCALE_LEVEL_FIELD));
    HashSet<Bucket> scaleLevelBuckets = extendedMap.get(VariableDocument.SCALE_LEVEL_FIELD);
    for (Bucket bucket : scaleLevelBuckets) {
      assertThat(bucket.getKey(), is("ordinal"));
    }
  }
}
