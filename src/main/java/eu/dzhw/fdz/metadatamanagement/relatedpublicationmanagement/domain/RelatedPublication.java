package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.OneForeignKeyIsUsed;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.ValidUrl;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Domain Object for the Related Publications.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "related_publications")
@OneForeignKeyIsUsed
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.builders")
public class RelatedPublication extends AbstractRdcDomainObject {

  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "related-publication-management.error.related-publication.id.not-empty")
  @Size(max = StringLengths.MEDIUM, 
      message = "related-publication-management.error.related-publication.id.size")
  @Pattern(regexp = Patterns.NO_WHITESPACE,
      message = "related-publication-management.error.related-publication.id.pattern")
  private String id;
 
  @NotEmpty(message = "related-publication-management.error.related-publication." 
      + "source-reference.not-empty")
  @Size(max = StringLengths.LARGE,
      message = "related-publication-management.error.related-publication." 
          + "source-reference.size")
  private String sourceReference;
  
  @Size(max = StringLengths.X_LARGE,
      message = "related-publication-management.error.related-publication." 
          + "publication-abstract.size")
  private String publicationAbstract;
  
  @Size(max = StringLengths.MEDIUM,
      message = "related-publication-management.error.related-publication." 
          + "doi.size")
  private String doi;
  
  @ValidUrl(message = "related-publication-management.error."
      + "related-publication.source-link.pattern")
  private String sourceLink;
  
  @Size(max = StringLengths.MEDIUM,
      message = "related-publication-management.error.related-publication." 
          + "title.size")
  @NotEmpty(message = "related-publication-management.error.related-publication." 
      + "title.not-empty")
  private String title;
  
  /* Foreign Keys */
  private List<String> questionIds;
  
  private List<String> surveyIds;
  
  private List<String> variableIds;
  
  private List<String> dataSetIds;
  
  private List<String> studyIds;
  
  private List<String> instrumentIds;
  
  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return this.id;
  }

  /* GETTER / SETTER */
  public String getSourceReference() {
    return sourceReference;
  }

  public void setSourceReference(String sourceReference) {
    this.sourceReference = sourceReference;
  }

  public String getPublicationAbstract() {
    return publicationAbstract;
  }

  public void setPublicationAbstract(String publicationAbstract) {
    this.publicationAbstract = publicationAbstract;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getSourceLink() {
    return sourceLink;
  }

  public void setSourceLink(String sourceLink) {
    this.sourceLink = sourceLink;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getQuestionIds() {
    return questionIds;
  }

  public void setQuestionIds(List<String> questionIds) {
    this.questionIds = questionIds;
  }

  public List<String> getSurveyIds() {
    return surveyIds;
  }

  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }

  public List<String> getVariableIds() {
    return variableIds;
  }

  public void setVariableIds(List<String> variableIds) {
    this.variableIds = variableIds;
  }

  public List<String> getDataSetIds() {
    return dataSetIds;
  }

  public void setDataSetIds(List<String> dataSetIds) {
    this.dataSetIds = dataSetIds;
  }

  public List<String> getStudyIds() {
    return studyIds;
  }

  public void setStudyIds(List<String> studyIds) {
    this.studyIds = studyIds;
  }

  public List<String> getInstrumentIds() {
    return instrumentIds;
  }

  public void setInstrumentIds(List<String> instrumentIds) {
    this.instrumentIds = instrumentIds;
  }
}
