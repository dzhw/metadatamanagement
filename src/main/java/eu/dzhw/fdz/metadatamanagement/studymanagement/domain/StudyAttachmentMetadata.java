package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidStudyAttachmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Metadata which will be stored in GridFS with each attachment for studies.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class StudyAttachmentMetadata extends AbstractRdcDomainObject {
  @Id
  private String id;
  
  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.study-id.not-empty")
  private String studyId;

  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  @NotNull(message =
      "study-management.error.study-attachment-metadata.index-in-study.not-null")
  private Integer indexInStudy;

  @Size(max = StringLengths.LARGE, message =
      "study-management.error.study-attachment-metadata.title.string-size")
  private String title;
  
  @NotNull(message =
      "study-management.error.study-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "study-management.error.study-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "study-management.error.study-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  @NotNull(message =
      "study-management.error.study-attachment-metadata.type.not-null")
  @I18nStringSize(min = 1, max = StringLengths.SMALL, message =
      "study-management.error.study-attachment-metadata.type.i18n-string-size")
  @ValidStudyAttachmentType(message =
      "study-management.error.study-attachment-metadata.type.valid-type")
  private I18nString type;

  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.filename.not-empty")
  @Pattern(message =
      "study-management.error.study-attachment-metadata.filename.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  @NotNull(message =
      "study-management.error.study-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "study-management.error.study-attachment-metadata.language.not-supported")
  private String language;
  
  public void generateId() {
    // hack to satisfy javers
    this.id = "/public/files/studies/" + studyId + "/attachments/" + fileName;
  }
}
