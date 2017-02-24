package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.OneForeignKeyIsUsed;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.OneStudyIsUsed;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.ValidPublicationYear;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.ValidRelatedPublicationId;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.ValidUrl;
import io.searchbox.annotations.JestId;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Domain Object for the Related Publications.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "related_publications")
@OneForeignKeyIsUsed(
    message = "related-publication-management.error.related-publication.one-foreign-key-is-used")
@OneStudyIsUsed(
    message = "related-publication-management.error.related-publication.one-study-is-used")
@ValidPublicationYear(message = "related-publication-management.error.related-publication." 
    + "year.valid")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.builders")
@ValidRelatedPublicationId(message = 
    "related-publication-management.error.related-publication.valid-related-publication-id")
public class RelatedPublication extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "related-publication-management.error.related-publication.id.not-empty")
  @Size(max = StringLengths.MEDIUM,
      message = "related-publication-management.error.related-publication.id.size")
  @Pattern(regexp = Patterns.NO_WHITESPACE,
      message = "related-publication-management.error.related-publication.id.pattern")
  private String id;
  @Size(max = StringLengths.MEDIUM,
      message = "related-publication-management.error.related-publication." + "doi.size")
  private String doi;
  @Size(max = StringLengths.MEDIUM,
      message = "related-publication-management.error.related-publication." + "title.size")
  @NotEmpty(
      message = "related-publication-management.error.related-publication." + "title.not-empty")
  private String title;
  @Size(max = StringLengths.LARGE,
      message = "related-publication-management.error.related-publication." + "authors.size")
  @NotEmpty(
      message = "related-publication-management.error.related-publication." + "authors.not-empty")
  private String authors;
  
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
  
  @ValidUrl(message = "related-publication-management.error."
      + "related-publication.source-link.pattern")
  private String sourceLink;
  
  @NotNull(message = "related-publication-management.error.related-publication." 
      + "year.not-null")
  private Integer year;
  
  @I18nStringSize(max = StringLengths.LARGE, 
      message = "related-publication-management.error.related-publication" 
      + ".abstract-source.i18n-string-size")
  private I18nString abstractSource;
  
  /* Foreign Keys */
  private List<String> questionIds = new ArrayList<String>();
  
  private List<String> surveyIds = new ArrayList<String>();
  
  private List<String> variableIds = new ArrayList<String>();
  
  private List<String> dataSetIds = new ArrayList<String>();
  
  private List<String> studyIds = new ArrayList<String>();
  
  private List<String> instrumentIds = new ArrayList<String>();
  
  public RelatedPublication() {
    super();
  }
  
  public RelatedPublication(RelatedPublication relatedPublication) {
    super();
    BeanUtils.copyProperties(relatedPublication, this);
  }
  
  @Override
  public String getId() {
    return this.id;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
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

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
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

  public String getSourceLink() {
    return sourceLink;
  }

  public void setSourceLink(String sourceLink) {
    this.sourceLink = sourceLink;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public I18nString getAbstractSource() {
    return abstractSource;
  }

  public void setAbstractSource(I18nString abstractSource) {
    this.abstractSource = abstractSource;
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
