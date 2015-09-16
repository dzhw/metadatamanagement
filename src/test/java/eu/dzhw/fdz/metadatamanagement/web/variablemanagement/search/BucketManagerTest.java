/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFilter;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.builders.VariableSearchFilterBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class BucketManagerTest {


  @Test
  public void testMergeFilterWithDtoInformationEmptyFilterMap() {
    // Arrange
    Map<String, TreeSet<Bucket>> filterMap = new HashMap<>();
    VariableSearchFilter variableSearchFormDto =
        new VariableSearchFilterBuilder().withQuery("EmptyQuery").withScaleLevel("nominal").build();

    Map<String, TreeSet<Bucket>> extendedMap =
        BucketManager.addEmptyBucketsIfNecessary(variableSearchFormDto, filterMap);

    // Act

    // Assert
    assertThat(extendedMap.keySet().size(), is(1));
    assertThat(extendedMap.keySet().iterator().next(),
        is(VariableDocument.SCALE_LEVEL_FIELD.getPath()));

    TreeSet<Bucket> scaleLevelBuckets =
        extendedMap.get(VariableDocument.SCALE_LEVEL_FIELD.getPath());
    for (Bucket bucket : scaleLevelBuckets) {
      assertThat(bucket.getKey(), is("nominal"));
    }
  }

  @Test
  public void testMergeFilterWithDtoInformationFilledMap() {
    // Arrange
    TreeSet<Bucket> filterList = new TreeSet<>();
    Map<String, TreeSet<Bucket>> filterMap = new HashMap<>();
    filterMap.put(VariableDocument.SCALE_LEVEL_FIELD.getPath(), filterList);
    VariableSearchFilter variableSearchFormDto =
        new VariableSearchFilterBuilder().withQuery("EmptyQuery").withScaleLevel("ordinal").build();

    Map<String, TreeSet<Bucket>> extendedMap =
        BucketManager.addEmptyBucketsIfNecessary(variableSearchFormDto, filterMap);

    // Act

    // Assert
    assertThat(extendedMap.keySet().size(), is(1));
    assertThat(extendedMap.keySet().iterator().next(),
        is(VariableDocument.SCALE_LEVEL_FIELD.getPath()));
    TreeSet<Bucket> scaleLevelBuckets =
        extendedMap.get(VariableDocument.SCALE_LEVEL_FIELD.getPath());
    for (Bucket bucket : scaleLevelBuckets) {
      assertThat(bucket.getKey(), is("ordinal"));
    }
  }
}
