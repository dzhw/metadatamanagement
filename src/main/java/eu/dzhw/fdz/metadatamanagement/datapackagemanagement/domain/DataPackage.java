package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
import org.javers.core.metamodel.annotation.TypeName;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nLink;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.Sponsor;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmptyOptional;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringMustNotContainComma;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidShadowId;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.projection.DataPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.validation.ValidDataPackageId;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.validation.ValidSurveyDesign;
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
 * A data package contains all metadata of a {@link DataAcquisitionProject}. It will get a DOI
 * (Digital Object Identifier) when the {@link DataAcquisitionProject} is released.
 */
@Entity
@Document(collection = "data_packages")
@TypeName("eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study")
@ValidDataPackageId(message = "data-package-management.error.data-package.id.not-valid-id")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Schema(description = "Go <a href='https://dzhw.github.io/metadatamanagement/"
    + "eu/dzhw/fdz/metadatamanagement/datapackagemanagement/domain/DataPackage.html'>here</a>"
    + " for further details.")
@ValidShadowId(message = "data-package-management.error.data-package.id.pattern")
public class DataPackage extends AbstractShadowableRdcDomainObject
    implements DataPackageSubDocumentProjection {

  private static final long serialVersionUID = -2715210632386850012L;

  /**
   * The id of the dataPackage which uniquely identifies the dataPackage in this application.
   */
  @Id
  @Setter(AccessLevel.NONE)
  @NotEmpty(message = "data-package-management.error.data-package.id.not-empty")
  private String id;

  /**
   * The master id of the dataPackage.
   *
   * The master id must not be empty, must be of the form {@code stu-{{dataAcquisitionProjectId}}$}
   * and the master id must not contain more than 512 characters.
   */
  @NotEmpty(message = "data-package-management.error.data-package.master-id.not-empty")
  @Size(max = StringLengths.MEDIUM,
      message = "data-package-management.error.data-package.master-id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "data-package-management.error.data-package.master-id.pattern")
  @Setter(AccessLevel.NONE)
  @Indexed
  private String masterId;

  /**
   * The id of the {@link DataAcquisitionProject} to which this dataPackage belongs.
   *
   * The dataAcquisitionProjectId must not be empty.
   */
  @Indexed
  @NotEmpty(
      message = "data-package-management.error.data-package.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The title of the dataPackage.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "data-package-management.error.data-package.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-package-management.error.data-package.title.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "data-package-management.error.data-package.title.i18n-string-entire-not-empty")
  private I18nString title;

  /**
   * A description of the dataPackage. Markdown is supported.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "data-package-management.error.data-package.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-package-management.error.data-package.description.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "data-package-management.error.data-package.description.i18n-string-not-empty")
  private I18nString description;

  /**
   * The names of the institutions which have performed the study from which this data package
   * results.
   *
   * It must be specified in German and English and it must not contain more than 512 characters.
   */
  @NotEmpty(message = "data-package-management.error.data-package.institutions.not-null")
  private List<@I18nStringSize(max = StringLengths.MEDIUM,
      message = "data-package-management.error.data-package.institution.i18n-string-size")
               @I18nStringEntireNotEmpty(
      message = "data-package-management.error.data-package.institution"
              + ".i18n-string-entire-not-empty") I18nString> institutions;

  /**
   * The name of the series of dataPackages to which this dataPackage belongs.
   *
   * If specified it must be specified in German and English. It must not contain more than 512
   * characters and must not contain ",".
   */
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "data-package-management.error.data-package.study-series.i18n-string-size")
  @I18nStringEntireNotEmptyOptional(
      message = "data-package-management.error.data-package.study-series"
          + ".i18n-string-entire-not-empty-optional")
  @I18nStringMustNotContainComma(
      message = "data-package-management.error.data-package.study-series."
          + "i18n-string-must-not-contain-comma")
  private I18nString studySeries;

  /**
   * List of {@link Sponsor}s which have sponsored this data package.
   *
   * Must not be empty.
   */
  @Valid
  @NotEmpty(message = "data-package-management.error.data-package.data-curators.not-empty")
  private List<Sponsor> sponsors;

  /**
   * List of {@link Person}s which have performed this dataPackage.
   *
   * Must not be empty.
   */
  @Valid
  @NotEmpty(message = "data-package-management.error.data-package.project-contributors.not-empty")
  private List<Person> projectContributors;

  /**
   * List of {@link Person}s which have curated this data package.
   *
   * Must not be empty.
   */
  @Valid
  @NotEmpty(message = "data-package-management.error.data-package.data-curators.not-empty")
  private List<Person> dataCurators;

  /**
   * The survey design of this {@link DataPackage}.
   *
   * Must be one of {@link SurveyDesigns} and must not be empty.
   */
  @NotNull(message = "data-package-management.error.data-package.survey-design.not-null")
  @ValidSurveyDesign(
      message = "data-package-management.error.data-package.survey-design.valid-survey-design")
  private I18nString surveyDesign;

  /**
   * Arbitrary additional text for this dataPackage. Markdown is supported.
   *
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-package-management.error.data-package.annotations.i18n-string-size")
  private I18nString annotations;

  /**
   * Keywords for the dataPackage.
   */
  @Valid
  @NotNull(message = "data-package-management.error.data-package.tags.not-null")
  private Tags tags;

  /**
   * A list of additional links for the data package.
   *
   * May be empty.
   */
  @Valid
  private List<I18nLink> additionalLinks;

  /**
   * Remarks for User Service config
   */
  @Indexed
  private String remarksUserService;

  public DataPackage(DataPackage dataPackage) {
    super();
    BeanUtils.copyProperties(dataPackage, this);
  }

  @Override
  protected void setMasterIdInternal(String masterId) {
    this.masterId = masterId;
  }

  @Override
  protected void setIdInternal(String id) {
    this.id = id;
  }

  /**
   * Report generation will fail if title strings include line breaks, Line breaks are
   * replaced with spaces.
   */
  public void reformatTitle() {
    I18nString title = new I18nString();
    // replace line break with space
    String de = this.getTitle().getDe().replace("\n", " ");
    String en = this.getTitle().getEn().replace("\n", " ");
    // sometimes the first replacement will lead to double spaces, gets cleaned up here
    title.setDe(de.replace("  ", " "));
    title.setEn(en.replace("  ", " "));
    this.setTitle(title);
  }
}
