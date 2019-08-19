package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain;

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
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.validation.ValidConceptAttachmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Metadata which will be stored with each attachment of a {@link Concept}.
 */
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ConceptAttachmentMetadata extends AbstractRdcDomainObject {

  private static final long serialVersionUID = -3238212286047012095L;

  /**
   * The id of the attachment. Holds the complete path which can be used to download the file.
   */
  @Id
  private String id;

  /**
   * The id of the {@link Concept} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message =
      "concept-management.error.concept-attachment-metadata.concept-id.not-empty")
  private String conceptId;

  /**
   * The index in the {@link Concept} of this attachment. Used for sorting the attachments of this
   * {@link Concept}.
   *
   * Must not be empty.
   */
  @NotNull(message =
      "concept-management.error.concept-attachment-metadata.index-in-concept.not-null")
  private Integer indexInConcept;

  /**
   * An optional title of this attachment in the attachments' language.
   *
   * It must not contain more than 2048 characters.
   */
  @NotEmpty(message =
      "concept-management.error.concept-attachment-metadata.title.not-null")
  @Size(max = StringLengths.LARGE, message =
      "concept-management.error.concept-attachment-metadata.title.string-size")
  private String title;

  /**
   * A description for this attachment.
   *
   * It must be specified in at least one language and it must not contain more than 512 characters.
   */
  @NotNull(message =
      "concept-management.error.concept-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "concept-management.error.concept-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "concept-management.error.concept-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  /**
   * The type of the attachment.
   *
   * Must be one of {@link ConceptAttachmentTypes} and must not be empty.
   */
  @NotNull(message =
      "concept-management.error.concept-attachment-metadata.type.not-null")
  @I18nStringSize(min = 1, max = StringLengths.SMALL, message =
      "concept-management.error.concept-attachment-metadata.type.i18n-string-size")
  @ValidConceptAttachmentType(message =
      "concept-management.error.concept-attachment-metadata.type.valid-type")
  private I18nString type;

  /**
   * The filename of the attachment.
   *
   * Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and
   * ".".
   */
  @NotEmpty(message =
      "concept-management.error.concept-attachment-metadata.filename.not-empty")
  @Pattern(message =
      "concept-management.error.concept-attachment-metadata.filename.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  /**
   * The language of the attachments content.
   *
   * Must not be empty and must be specified as ISO 639 language code.
   */
  @NotNull(message =
      "concept-management.error.concept-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "concept-management.error.concept-attachment-metadata.language.not-supported")
  private String language;

  /**
   * Generate the id of this attachment from the studyId and the fileName.
   */
  public void generateId() {
    // hack to satisfy javers
    this.setId("/public/files/concepts/" + conceptId + "/attachments/" + fileName);
  }
}
