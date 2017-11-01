package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.OneForeignKeyIsUsed;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.OneStudyIsUsed;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.ValidPublicationYear;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.ValidRelatedPublicationId;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.ValidUrl;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@ValidRelatedPublicationId(message = 
    "related-publication-management.error.related-publication.valid-related-publication-id")
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
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
  @Size(max = StringLengths.LARGE,
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
  
  @NotNull(message =
      "related-publication-management.error.related-publication.language.not-null")
  @ValidIsoLanguage(message =
      "related-publication-management.error.related-publication.language.not-supported")
  private String language;

  /* Foreign Keys */
  @Indexed
  private List<String> questionIds;
  
  @Indexed
  private List<String> surveyIds;
  
  @Indexed
  private List<String> variableIds;
  
  @Indexed
  private List<String> dataSetIds;
  
  @Indexed
  private List<String> studyIds;
  
  @Indexed
  private List<String> instrumentIds;
  
  public RelatedPublication(RelatedPublication relatedPublication) {
    super();
    BeanUtils.copyProperties(relatedPublication, this);
  }
}
