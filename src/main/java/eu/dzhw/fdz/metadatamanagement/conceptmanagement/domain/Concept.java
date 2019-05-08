package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import io.searchbox.annotations.JestId;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A concept is something which cannot be observed directly but there is a model which helps
 * observing the concept. E.g.: The concept "Personality" can be observed with the help of the
 * five-factor model (Big5).
 */
@Entity
@Document(collection = "concepts")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ApiModel(
    description = "Go <a href='https://metadatamanagement.readthedocs.io/de/stable/javadoc/eu/dzhw/"
        + "fdz/metadatamanagement/conceptmanagement/domain/Concept.html'>here</a> for further details.")
public class Concept extends AbstractRdcDomainObject implements ConceptSubDocumentProjection {

  /**
   * The id of the concept which uniquely identifies the concept in this application.
   * 
   * Must not be empty and mist not contain more than 512 characters. Must start with "con-" and end
   * with "$" and must not contain any whitespace.
   */
  @Id
  @JestId
  @Setter(AccessLevel.NONE)
  @NotEmpty(message = "concept-management.error.concept.id.not-empty")
  @Pattern(regexp = Patterns.NO_WHITESPACE,
    message = "concept-management.error.concept.id.no-whitespace")
  @Pattern(regexp = "^con\\-.*\\$$",
    message = "concept-management.error.concept.id.pattern")
  @Size(max = StringLengths.MEDIUM,
    message = "concept-management.error.concept.id.size")
  private String id;

  /**
   * The title of the concept.
   *
   * It must be specified in German and English and it must not contain more than 512 characters.
   */
  @NotNull(message = "concept-management.error.concept.title.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "concept-management.error.concept.title.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "concept-management.error.concept.title.i18n-string-entire-not-empty")
  private I18nString title;

  /**
   * A description of the concept.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "concept-management.error.concept.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "concept-management.error.concept.description.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "concept-management.error.concept.description.i18n-string-not-empty")
  private I18nString description;

  /**
   * List of {@link Person}s which have defined this concept.
   *
   * Must not be empty.
   */
  @Valid
  @NotEmpty(message = "concept-management.error.concept.authors.not-empty")
  private List<Person> authors;

  /**
   * The doi of the paper defining the concept.
   * 
   * Must not contain more than 512 characters.
   */
  @Size(max = StringLengths.MEDIUM, message = "concept-management.error.concept.doi.size")
  private String doi;

  /**
   * The license of this concept.
   * 
   * Must not contain more than 1 MB characters.
   */
  @Size(max = StringLengths.X_LARGE, message = "concept-management.error.concept.license.size")
  private String license;

  /**
   * Hint on how to cite this concept.
   * 
   * Must not be empty and must not contain more than 2048 characters.
   */
  @NotEmpty(message = "concept-management.error.concept." + "citation-hint.not-empty")
  @Size(max = StringLengths.LARGE,
      message = "concept-management.error.concept." + "citation-hint.size")
  private String citationHint;

  /**
   * Keywords for the concept.
   * 
   * Must not be empty.
   */
  @Valid
  private Tags tags;

  /**
   * The original languages of the definition of the concept as ISO 639 code.
   * 
   * Must not be empty.
   */
  @NotEmpty(message = "concept-management.error.concept." + "original-languages.not-empty")
  private Set<@ValidIsoLanguage(message = "concept-management.error.concept."
      + "original-languages.not-valid-iso") String> originalLanguages;

  public Concept(Concept concept) {
    super();
    BeanUtils.copyProperties(concept, this);
  }
}
