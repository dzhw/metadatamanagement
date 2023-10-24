package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;
import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation.ValidExternalDataPackageAvailabilityType;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * External data describes data sets of an {@link AnalysisPackage} which cannot be ordered in our
 * RDC.
 *
 * Scenario: The actual files (that are part of the analysis package) are not hosted by FDZ-DZHW.
 * Therefore, this object refers to external data sources.
 *
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@ValueObject
public class ExternalDataPackage extends AbstractAnalysisDataPackage {
  private static final long serialVersionUID = 7675341390552887966L;

  private static final I18nString OPEN_ACCESS = new I18nString("Open Access", "open-access");
  private static final I18nString RESTRICTED_ACCESS =
      new I18nString("beschränkter Zugang", "restricted access");
  private static final I18nString NOT_ACCESSIBLE =
      new I18nString("nicht zugänglich", "not accessible");

  public static final List<I18nString> AVAILABLE_AVAILABILITY_TYPES =
      Collections.unmodifiableList(Arrays.asList(OPEN_ACCESS, RESTRICTED_ACCESS, NOT_ACCESSIBLE));

  /**
   * The title of the external data package.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "analysis-package-management.error.external-data-package.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.external-data-package.title.i18n-string-size")
  @I18nStringEntireNotEmpty(message = "analysis-package-management.error.external-data-package."
      + "title.i18n-string-entire-not-empty")
  private I18nString title;

  /**
   * A description of the analysis package. Markdown is supported.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "analysis-package-management.error.external-data-package.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.external-data-package.description"
          + ".i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "analysis-package-management.error.external-data-package.description."
          + "i18n-string-not-empty")
  private I18nString description;

  /**
   * Arbitrary additional text for this analysis package. Markdown is supported.
   *
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.external-data-package.annotations"
          + ".i18n-string-size")
  private I18nString annotations;

  /**
   * The data source where the data is stored must be specified here (e.g. name of the
   * institution/repository, private data storage).
   *
   * Must not be empty and must not contain more than 512 characters.
   */
  @NotNull(message = "analysis-package-management.error.external-data-package.data-source.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "analysis-package-management.error.external-data-package.data-source"
          + ".i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "analysis-package-management.error.external-data-package.data-source."
          + "i18n-string-not-empty")
  private I18nString dataSource;

  /**
   * Optional url for the data source.
   *
   * If specified it must be a valid URL and not longer than 2000 characters.
   */
  @URL(
      message = "analysis-package-management.error.external-data-package.data-source-url"
          + ".invalid-url")
  @Size(max = 2000,
      message = "analysis-package-management.error.external-data-package.data-source-url.length")
  private String dataSourceUrl;

  /**
   * Must be one of AVAILABLE_AVAILABILITY_TYPES.
   */
  @ValidExternalDataPackageAvailabilityType(
      message = "analysis-package-management.error.external-data-package.availability-type"
          + ".not-valid")
  private I18nString availabilityType;

  /**
   * The license of this external data package. Markdown is supported.
   *
   * May be empty. Must not contain more than 1 MB characters.
   */
  @Size(max = StringLengths.X_LARGE,
    message = "analysis-package-management.error.analysis-package.license.size")
  private String license;
}
