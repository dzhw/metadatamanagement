package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

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

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.projection.AnalysisPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation.ValidAnalysisPackageId;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation.UniqueScriptUuids;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nLink;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.Sponsor;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidShadowId;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
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
 * An analysis package contains all metadata of scripts and analysis data. It will get a DOI
 * (Digital Object Identifier) when the {@link DataAcquisitionProject} is released.
 */
@Entity
@Document(collection = "analysis_packages")
@ValidAnalysisPackageId(
    message = "analysis-package-management.error.analysis-package.id.not-valid-id")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Schema(description = "Go <a href='https://dzhw.github.io/metadatamanagement/"
    + "eu/dzhw/fdz/metadatamanagement/analysispackagemanagement/domain/"
    + "AnalysisPackage.html'>here</a> for further details.")
@ValidShadowId(message = "analysis-package-management.error.analysis-package.id.pattern")
@UniqueScriptUuids(
    message = "analysis-package-management.error.analysis-package.scripts.unique-uuids")
public class AnalysisPackage extends AbstractShadowableRdcDomainObject
    implements AnalysisPackageSubDocumentProjection {
  private static final long serialVersionUID = 3006197991079331471L;
  /**
   * The id of the analysis package which uniquely identifies the analysis package in this
   * application.
   */
  @Id
  @Setter(AccessLevel.NONE)
  @NotEmpty(message = "analysis-package-management.error.analysis-package.id.not-empty")
  private String id;

  /**
   * The master id of the analysis package.
   *
   * The master id must not be empty, must be of the form {@code ana-{{dataAcquisitionProjectId}}$}
   * and the master id must not contain more than 512 characters.
   */
  @NotEmpty(message = "analysis-package-management.error.analysis-package.master-id.not-empty")
  @Size(max = StringLengths.MEDIUM,
      message = "analysis-package-management.error.analysis-package.master-id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "analysis-package-management.error.analysis-package.master-id.pattern")
  @Setter(AccessLevel.NONE)
  @Indexed
  private String masterId;

  /**
   * The id of the {@link DataAcquisitionProject} to which this analysis package belongs.
   *
   * The dataAcquisitionProjectId must not be empty.
   */
  @Indexed
  @NotEmpty(message = "analysis-package-management.error.analysis-package."
      + "data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The title of the analysis package.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "analysis-package-management.error.analysis-package.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.analysis-package.title.i18n-string-size")
  @I18nStringEntireNotEmpty(message = "analysis-package-management.error.analysis-package.title."
      + "i18n-string-entire-not-empty")
  private I18nString title;

  /**
   * A description of the analysis package. Markdown is supported.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "analysis-package-management.error.analysis-package.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.analysis-package.description.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "analysis-package-management.error.analysis-package.description."
          + "i18n-string-not-empty")
  private I18nString description;

  /**
   * Arbitrary additional text for this analysis package. Markdown is supported.
   *
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.analysis-package.annotations.i18n-string-size")
  private I18nString annotations;

  /**
   * List of {@link Person}s which have created this analysis package.
   *
   * Must not be empty.
   */
  @Valid
  @NotEmpty(message = "analysis-package-management.error.analysis-package.authors.not-empty")
  private List<Person> authors;

  /**
   * List of {@link Person}s which have curated this analysis package.
   *
   * Must not be empty.
   */
  @Valid
  @NotEmpty(message = "analysis-package-management.error.analysis-package.data-curators.not-empty")
  private List<Person> dataCurators;

  /**
   * The names of the institutions which have participated in creating this analysis package.
   *
   * It can be empty but if present must be specified in German and English and it must not contain
   * more than 512 characters.
   */
  @Valid
  private List<@I18nStringSize(max = StringLengths.MEDIUM,
      message = "analysis-package-management.error.analysis-package.institution.i18n-string-size")
      @I18nStringEntireNotEmpty(
          message = "analysis-package-management.error.analysis-package.institution"
              + ".i18n-string-entire-not-empty") I18nString> institutions;

  /**
   * List of {@link Sponsor}s which have sponsored this data package.
   *
   * May be empty.
   */
  @Valid
  private List<Sponsor> sponsors;

  /**
   * The license of this analysis package. Markdown is supported.
   *
   * May be empty. Must not contain more than 1 MB characters.
   */
  @Size(max = StringLengths.X_LARGE,
      message = "analysis-package-management.error.analysis-package.license.size")
  private String license;

  /**
   * A list of additional links for the analysis package.
   *
   * May be empty.
   */
  @Valid
  private List<I18nLink> additionalLinks;

  /**
   * List of data packages used by the scripts in this analysis package.
   *
   * May be empty.
   */
  @Valid
  private List<AbstractAnalysisDataPackage> analysisDataPackages;

  /**
   * List of scripts which are part of the analysis package.
   *
   * At least one {@link Script} must be specified.
   */
  @Valid
  @NotEmpty(message = "analysis-package-management.error.analysis-package.scripts.at-leat-one")
  private List<Script> scripts;

  /**
   * Keywords for the analysis package.
   *
   * Must not be empty.
   */
  @Valid
  @NotNull(message = "analysis-package-management.error.analysis-package.tags.not-null")
  private Tags tags;

  /**
   * ELSST keywords for the analysis package.
   */
  @Valid
  private TagsElsst tagsElsst;

  public AnalysisPackage(AnalysisPackage analysisPackage) {
    super();
    BeanUtils.copyProperties(analysisPackage, this);
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
