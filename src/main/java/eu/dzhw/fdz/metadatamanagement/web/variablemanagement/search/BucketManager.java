package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFilter;

/**
 * This class adds empty buckets to the buckets coming from elasticsearch if the user has filtered
 * by a value (e.g. scaleLEvel=ordinal) but no buckets have been returned.
 * 
 * @author Daniel Katzberg
 *
 */
public class BucketManager {

  /**
   * Adds an empty bucket if the user has search for the filter value but no bucket has been found.
   * 
   * @return The extended bucket map.
   */
  public static Map<String, Set<Bucket>> addEmptyBucketsIfNecessary(
      VariableSearchFilter variableSearchFormDto, Map<String, Set<Bucket>> bucketMap) {
    Map<Field, String> filters = variableSearchFormDto.getAllFilterValues();

    for (Entry<Field, String> filter : filters.entrySet()) {

      // get basic variables from the entry and the nested object
      Field field = filter.getKey();
      String filterValue = filter.getValue();


      // check for the group in the map
      String nestedPath = field.getNestedPath();
      Bucket emptyBucket = new Bucket(filterValue, 0L);
      
      if (!bucketMap.containsKey(nestedPath)) {
        Set<Bucket> filterBucketList = new TreeSet<>();
        filterBucketList.add(emptyBucket);
        bucketMap.put(nestedPath, filterBucketList);
        // okay group is in the map, check here for the value
      } else {
        if (!bucketMap.get(nestedPath).contains(emptyBucket)) {
          Set<Bucket> filterBucketList = bucketMap.get(nestedPath);
          filterBucketList.add(emptyBucket);
        }
      }
    }

    return bucketMap;
  }
}
