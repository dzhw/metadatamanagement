package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation.ValidCustomDataPackageAccessWay;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation.ValidCustomDataPackageAvailabilityType;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Scenario: Some kind of data package that is part of the {@link AnalysisPackage} and hosted by the
 * FDZ-DZHW but not a doified {@link DataPackage} (SUF or CUF) of the FDZ-DZHW.
 * 
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@ValueObject
public class CustomDataPackage extends AbstractAnalysisDataPackage {
  private static final long serialVersionUID = -513926799487045749L;
  private static final String DOWNLOAD = "Download";
  private static final String REMOTE = "Remote-Desktop";
  private static final String ONSITE = "Onsite";

  public static final List<String> AVAILABLE_ACCESS_WAYS =
      Collections.unmodifiableList(Arrays.asList(DOWNLOAD, REMOTE, ONSITE));

  private static final I18nString ACCESSIBLE = new ImmutableI18nString("verfügbar", "accessible");
  private static final I18nString NOT_ACCESSIBLE =
      new ImmutableI18nString("nicht verfügbar", "not accessible");

  public static final List<I18nString> AVAILABLE_AVAILABILITY_TYPES =
      Collections.unmodifiableList(Arrays.asList(ACCESSIBLE, NOT_ACCESSIBLE));

  /**
   * The title of the custom data package.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "analysis-package-management.error.custom-data-package.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.custom-data-package.title.i18n-string-size")
  @I18nStringEntireNotEmpty(message = "analysis-package-management.error.custom-data-package."
      + "title.i18n-string-entire-not-empty")
  private I18nString title;

  /**
   * A description of the analysis package. Markdown is supported.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "analysis-package-management.error.custom-data-package.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.custom-data-package.description"
          + ".i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "analysis-package-management.error.custom-data-package.description."
          + "i18n-string-not-empty")
  private I18nString description;

  /**
   * Arbitrary additional text for this analysis package. Markdown is supported.
   *
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.custom-data-package.annotations"
          + ".i18n-string-size")
  private I18nString annotations;

  /**
   * List of data sources used to create this custom data package.
   * 
   * May be empty.
   */
  @Valid
  private List<DataSource> dataSources;

  /**
   * Must be one of AVAILABLE_ACCESS_WAYS. May be empty.
   */
  @ValidCustomDataPackageAccessWay(
      message = "analysis-package-management.error.custom-data-package.access-way.not-valid")
  private String accessWay;

  /**
   * Must be one of AVAILABLE_AVAILABILITY_TYPES.
   */
  @ValidCustomDataPackageAvailabilityType(
      message = "analysis-package-management.error.custom-data-package.availability-type"
          + ".not-valid")
  private I18nString availabilityType;
}
