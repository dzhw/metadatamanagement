package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto;

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
  public static Map<String, HashSet<Bucket>> addEmptyBucketsIfNecessary(
      VariableSearchFormDto variableSearchFormDto, Map<String, HashSet<Bucket>> bucketMap) {
    Map<Field, String> filters = variableSearchFormDto.getAllFilters();

    for (Entry<Field, String> filter : filters.entrySet()) {
      
      //get basic variables from the entry and the nested object
      Field field = filter.getKey();
      String filterValue = filter.getValue();
      String nestedPath = BucketManager.getNestedKey(field);
      
      //check for the group in the map
      if (!bucketMap.containsKey(nestedPath)) {
        HashSet<Bucket> filterBucketList = new HashSet<>();
        filterBucketList.add(new Bucket(filterValue, 0L));
        bucketMap.put(nestedPath, filterBucketList);
        // okay group is in the map, check here for the value
      } else {
        if (!bucketMap.get(nestedPath).contains(new Bucket(filterValue, 0L))) {
          HashSet<Bucket> filterBucketList = bucketMap.get(nestedPath);
          filterBucketList.add(new Bucket(filterValue, 0L));
        }
      }
    }

    return bucketMap;
  }

  /**
   * This is a helper method for addEmptyBucketsIfNecessary. Return the correct nested Path for the
   * bucketMap, where the key is the complete nested path.
   * 
   * @param field a nested or not nested field. this is the key in the filters Hashmap
   * @return the complete (nested) path
   */
  private static String getNestedKey(Field field) {
    if (field.isNested()) {
      return BucketManager.getNestedKey(field.getNestedField());
    } else {
      return field.getPath();
    }
  }
}
