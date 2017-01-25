package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import io.searchbox.annotations.JestId;

/**
 * Representation of a related publication which is stored in elasticsearch.
 *
 * @author Daniel Katzberg
 */
public class RelatedPublicationSearchDocument {
  @JestId
  private String id;
  
  private String sourceReference;
  
  private String publicationAbstract;
  
  private String doi;
  
  private String sourceLink;
  
  private String title;
  
  private String authors;
  
  private String abstractSource;
  
  private Integer year;
  
  private List<String> questionIds;
  
  private List<String> surveyIds;
  
  private List<String> variableIds;
  
  private List<String> dataSetIds;
  
  private List<String> studyIds;
  
  private List<String> instrumentIds;
  
  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public RelatedPublicationSearchDocument(RelatedPublication relatedPublication,
      ElasticsearchIndices index) {
    this.id = relatedPublication.getId();
    this.sourceReference = relatedPublication.getSourceReference();
    this.publicationAbstract = relatedPublication.getPublicationAbstract();
    this.doi = relatedPublication.getDoi();
    this.sourceLink = relatedPublication.getSourceLink();
    this.title = relatedPublication.getTitle();
    this.questionIds = relatedPublication.getQuestionIds();
    this.surveyIds = relatedPublication.getSurveyIds();
    this.variableIds = relatedPublication.getVariableIds();
    this.dataSetIds = relatedPublication.getDataSetIds();
    this.studyIds = relatedPublication.getStudyIds();
    this.instrumentIds = relatedPublication.getInstrumentIds();
    this.year = relatedPublication.getYear();
    this.authors = relatedPublication.getAuthors();
    createI18N(relatedPublication, index);
  }
  
  private void createI18N(RelatedPublication relatedPublication, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        abstractSource = relatedPublication.getAbstractSource() != null
          ? relatedPublication.getAbstractSource().getDe() : null;
        break;
      case METADATA_EN:
        abstractSource = relatedPublication.getAbstractSource() != null
          ? relatedPublication.getAbstractSource().getEn() : null;
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }
  
  public String getId() {
    return id;
  }

  public String getSourceReference() {
    return sourceReference;
  }

  public String getPublicationAbstract() {
    return publicationAbstract;
  }

  public String getDoi() {
    return doi;
  }

  public String getSourceLink() {
    return sourceLink;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getAbstractSource() {
    return abstractSource;
  }

  public void setAbstractSource(String abstractSource) {
    this.abstractSource = abstractSource;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public List<String> getQuestionIds() {
    return questionIds;
  }

  public List<String> getSurveyIds() {
    return surveyIds;
  }

  public List<String> getVariableIds() {
    return variableIds;
  }

  public List<String> getDataSetIds() {
    return dataSetIds;
  }

  public List<String> getStudyIds() {
    return studyIds;
  }

  public List<String> getInstrumentIds() {
    return instrumentIds;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setSourceReference(String sourceReference) {
    this.sourceReference = sourceReference;
  }

  public void setPublicationAbstract(String publicationAbstract) {
    this.publicationAbstract = publicationAbstract;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public void setSourceLink(String sourceLink) {
    this.sourceLink = sourceLink;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setQuestionIds(List<String> questionIds) {
    this.questionIds = questionIds;
  }

  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }

  public void setVariableIds(List<String> variableIds) {
    this.variableIds = variableIds;
  }

  public void setDataSetIds(List<String> dataSetIds) {
    this.dataSetIds = dataSetIds;
  }

  public void setStudyIds(List<String> studyIds) {
    this.studyIds = studyIds;
  }

  public void setInstrumentIds(List<String> instrumentIds) {
    this.instrumentIds = instrumentIds;
  }
}
