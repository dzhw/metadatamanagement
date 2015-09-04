package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;

import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatype.PageWithBuckets;

/**
 * This is the interface for custom methods of the repository for the variable documents.
 * 
 * @author Daniel Katzberg
 *
 */
public interface VariableRepositoryCustom {

  /**
   * This method search in the elasticsearch _all field. This search use the fuzzyness of 0.1.
   * 
   * @param query the query for the search all field. It can be any value of any attribute, which
   *        are represent in the _all layer.
   * @param pageable a pageable object.
   * @return A page object with all found variable documents.
   * @see Fuzziness
   */
  Page<VariableDocument> matchQueryInAllField(String query, Pageable pageable);

  /**
   * This method search in the elasticsearch. This search use the fuzzyness of 0.1.
   * 
   * @param query the query for the search. It can be any value of name.
   * @param pageable a pageable object.
   * @return A page object with all found variable documents.
   * @see Fuzziness
   */
  Page<VariableDocument> matchPhrasePrefixQuery(String query, Pageable pageable);

  /**
   * This method search in the elasticsearch. This search use the fuzzyness of 0.1. It looks for the
   * all variables with the same survey id.
   * 
   * @param surveyId the surveyId of the survey
   * @param variableAlias variableAlias of the survey
   * @return A page object with all found variable documents.
   * @see Fuzziness
   */
  Page<VariableDocument> filterBySurveyIdAndVariableAlias(String surveyId, String variableAlias);

  /**
   * Query for all variable documents by using the all field for exact matches as well as fuzzy
   * matches.
   * 
   * @param query The query to be executed.
   * @param scaleLevel A filter for reduce the results of a given scaleLevel
   * @param pageable The page size and number and sort.
   * @return A page holding the first variable documents
   */
  PageWithBuckets<VariableDocument> matchQueryInAllFieldAndNgrams(String query,
      String scaleLevel, Pageable pageable);

  /**
   * Query for all variable documents including all aggregations vor variables.
   * 
   * @param pageable The page size and number and sort.
   * @return A page holding the first variable documents and aggreagations
   */
  PageWithBuckets<VariableDocument> matchAllWithAggregations(Pageable pageable);

}
