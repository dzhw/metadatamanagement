package eu.dzhw.fdz.metadatamanagement.service.common;

import org.springframework.data.domain.Pageable;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.SearchFilter;

/**
 * A Interface for search services.
 * 
 * @param <D> The class of the document
 * 
 * @author Daniel Katzberg
 *
 */
public interface SearchService<D> extends BasicService<D> {

  /**
   * Search questions by query. If the query string does not contain text the first n questions are
   * returned as defined in the pageable.
   * 
   * @param searchFilter The searchfilter with all information from the query (filter, query etc)
   * @param pageable a pageable object.
   * 
   * @return Page with the found document of the class D
   */
  PageWithBuckets<D> search(SearchFilter searchFilter, Pageable pageable);

}
