package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;

import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto;

/**
 * This is the interface for custom methods of the repository for the variable documents.
 * 
 * @author Daniel Katzberg
 *
 */
public interface VariableRepositoryCustom {

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
   * @param variableSearchFormDto the data transfer object of the search
   * @param scaleLevel A filter for reduce the results of a given scaleLevel
   * @param pageable The page size and number and sort.
   * @return A page holding the first variable documents
   */
  PageWithBuckets<VariableDocument> search(
      VariableSearchFormDto variableSearchFormDto, Pageable pageable);
}
