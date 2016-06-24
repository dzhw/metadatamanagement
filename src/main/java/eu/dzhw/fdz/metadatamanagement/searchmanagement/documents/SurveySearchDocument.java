package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import io.searchbox.annotations.JestId;

/**
 * Representation of a survey which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class SurveySearchDocument {
  @JestId
  private String id;

  private String title;

  private Period fieldPeriod;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public SurveySearchDocument(Survey survey, ElasticsearchIndices index) {
    this.id = survey.getId();
    createI18nAttributes(survey, index);
    this.fieldPeriod = survey.getFieldPeriod();
  }

  private void createI18nAttributes(Survey survey, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        title = survey.getTitle() != null ? survey.getTitle().getDe() : null;
        break;
      case METADATA_EN:
        title = survey.getTitle() != null ? survey.getTitle().getEn() : null;
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Period getFieldPeriod() {
    return fieldPeriod;
  }

  public void setFieldPeriod(Period fieldPeriod) {
    this.fieldPeriod = fieldPeriod;
  }
}
