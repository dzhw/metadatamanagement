package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import java.util.HashSet;
import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
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
    Map<String, String> filters = variableSearchFormDto.getAllFilters();

    filters.keySet().forEach(filterName -> {
        String filterValue = filters.get(filterName);
        Bucket emptyBucket = new Bucket(filterValue, 0L);
        if (!bucketMap.containsKey(filterName)) {
          HashSet<Bucket> filterBucketList = new HashSet<>();
          filterBucketList.add(emptyBucket);
          bucketMap.put(filterName, filterBucketList);
          // okay group is in the map, check here for the value
        } else {
          if (!bucketMap.get(filterName).contains(emptyBucket)) {
            HashSet<Bucket> filterBucketList = bucketMap.get(filterName);
            filterBucketList.add(emptyBucket);
          }
        }
      });

    return bucketMap;
  }
}
