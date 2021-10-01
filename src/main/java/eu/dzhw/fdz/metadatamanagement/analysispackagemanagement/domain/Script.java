package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation.ValidSoftwarePackage;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A script which will be published with this {@link AnalysisPackage}.
 * 
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@ValueObject
public class Script {

  /**
   * Client side generated id of the script. Used to reference script attachments. Not unique in the
   * DB cause shadow copies of {@link AnalysisPackage}s do not changes this id.
   * 
   * Must not be empty.
   */
  @NotEmpty(message = "analysis-package-management.error.script.uuid.not-empty")
  private String uuid;

  /**
   * The title of the script.
   *
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "analysis-package-management.error.script.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "analysis-package-management.error.script.title.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "analysis-package-management.error.script.title." + "i18n-string-entire-not-empty")
  private I18nString title;

  /**
   * The human language used for code comments as ISO 639 code.
   *
   * Must not be empty and must be a valid ISO 639 code.
   */
  @NotEmpty(message = "analysis-package-management.error.script.used-language." + "not-empty")
  @ValidIsoLanguage(
      message = "analysis-package-management.error.script.used-language." + "not-valid")
  private String usedLanguage;

  /**
   * The software package in which this script was written.
   * 
   * Must be one of {@link SoftwarePackages}.
   */
  @ValidSoftwarePackage(
      message = "analysis-package-management.error.script.software-package." + "not-valid")
  private String softwarePackage;

  /**
   * The version of the software package in which this script was written.
   * 
   * Must not be empty and must not contain more than 32 characters.
   */
  @NotEmpty(
      message = "analysis-package-management.error.script.software-package-version" + ".not-empty")
  @Size(max = StringLengths.SMALL,
      message = "analysis-package-management.error.script.software-package-version"
          + ".string-size")
  private String softwarePackageVersion;
}
