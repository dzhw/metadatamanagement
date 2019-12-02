package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmptyOptional;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringMustNotContainComma;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidShadowId;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidDataAvailability;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidStudyId;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidSurveyDesign;
import io.searchbox.annotations.JestId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A study contains all metadata of a {@link DataAcquisitionProject}. It will get a DOI (Digital
 * Object Identifier) when the {@link DataAcquisitionProject} is released.
 */
@Entity
@Document(collection = "studies")
@ValidStudyId(message = "study-management.error.study.id.not-valid-id")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Schema(
    description = "Go <a href='https://metadatamanagement.readthedocs.io/de/stable/javadoc/eu/dzhw/"
        + "fdz/metadatamanagement/studymanagement/domain/Study.html'>here</a> for further details.")
@ValidShadowId(message = "study-management.error.study.id.pattern")
public class Study extends AbstractShadowableRdcDomainObject implements StudySubDocumentProjection {

  private static final long serialVersionUID = -2715210632386850012L;

  /**
   * The id of the study which uniquely identifies the study in this application.
   */
  @Id
  @JestId
  @Setter(AccessLevel.NONE)
  @NotEmpty(message = "study-management.error.study.id.not-empty")
  private String id;

  /**
   * The master id of the study.
   *
   * The master id must not be empty, must be of the form {@code stu-{{dataAcquisitionProjectId}}$}
   * and the master id must not contain more than 512 characters.
   */
  @NotEmpty(message = "study-management.error.study.master-id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "study-management.error.study.master-id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "study-management.error.study.master-id.pattern")
  @Setter(AccessLevel.NONE)
  @Indexed
  private String masterId;

  /**
   * The id of the {@link DataAcquisitionProject} to which this study belongs.
   *
   * The dataAcquisitionProjectId must not be empty.
   */
  @Indexed
  @NotEmpty(message = "study-management.error.study.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The title of the study.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "study-management.error.study.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.title.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "study-management.error.study.title.i18n-string-entire-not-empty")
  private I18nString title;

  /**
   * A description of the study.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "study-management.error.study.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.description.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "study-management.error.study.description.i18n-string-not-empty")
  private I18nString description;

  /**
   * The names of the institutions which have performed this study.
   *
   * It must be specified in German and English and it must not contain more than 512 characters.
   */
  @NotEmpty(message = "study-management.error.study.institutions.not-null")
  private List<@I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.institution.i18n-string-size") @I18nStringEntireNotEmpty(
          message = "study-management.error.study.institution.i18n-string-entire-not-empty") I18nString> institutions;

  /**
   * The name of the series of studies to which this study belongs..
   *
   * If specified it must be specified in German and English. It must not contain more than 512
   * characters and must not contain ",".
   */
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.study-series.i18n-string-size")
  @I18nStringEntireNotEmptyOptional(
      message = "study-management.error.study.study-series.i18n-string-entire-not-empty-optional")
  @I18nStringMustNotContainComma(
      message = "study-management.error.study.study-series.i18n-string-must-not-contain-comma")
  private I18nString studySeries;

  /**
   * The name of the sponsor who which has sponsored this study.
   *
   * It must be specified in German and English and it must not contain more than 512 characters.
   */
  @NotNull(message = "study-management.error.study.sponsor.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.sponsor.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "study-management.error.study.sponsor.i18n-string-entire-not-empty")
  private I18nString sponsor;

  /**
   * List of {@link Person}s which have performed this study.
   *
   * Must not be empty.
   */
  @Valid
  @NotEmpty(message = "study-management.error.study.authors.not-empty")
  private List<Person> authors;

  /**
   * The current state of the data's availability.
   *
   * Must be one of {@link DataAvailabilities} and must not be empty.
   */
  @NotNull(message = "study-management.error.study.data-availability.not-null")
  @ValidDataAvailability(
      message = "study-management.error.study.data-availability.valid-data-availability")
  private I18nString dataAvailability;

  /**
   * The survey design of this {@link Study}.
   *
   * Must be one of {@link SurveyDesigns} and must not be empty.
   */
  @NotNull(message = "study-management.error.study.survey-design.not-null")
  @ValidSurveyDesign(message = "study-management.error.study.survey-design.valid-survey-design")
  private I18nString surveyDesign;

  /**
   * Arbitrary additional text for this study.
   *
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.annotations.i18n-string-size")
  private I18nString annotations;

  /**
   * Keywords for the study.
   */
  @Valid
  private Tags tags;

  public Study(Study study) {
    super();
    BeanUtils.copyProperties(study, this);
  }

  @Override
  protected void setMasterIdInternal(String masterId) {
    this.masterId = masterId;
  }

  @Override
  protected void setIdInternal(String id) {
    this.id = id;
  }
}
