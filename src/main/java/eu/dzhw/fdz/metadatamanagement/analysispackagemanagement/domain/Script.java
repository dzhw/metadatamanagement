package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation.ValidSoftwarePackage;
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
public class Script implements Serializable {

  private static final long serialVersionUID = 5119412626299466224L;

  /**
   * Client side generated id of the script. Used to reference script attachments. Not unique in the
   * DB cause shadow copies of {@link AnalysisPackage}s do not changes this id.
   *
   * Must not be empty and must be unique within the {@link AnalysisPackage}.
   */
  @NotEmpty(message = "analysis-package-management.error.script.uuid.not-empty")
  private String uuid;

  /**
   * The title of the script.
   *
   * It must not contain more than 2048 characters.
   */
  @Size(max = StringLengths.LARGE,
      message = "analysis-package-management.error.script.title.size")
  private String title;

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

  /**
   * The license of the script. Markdown is supported.
   *
   * May be empty. Must not contain more than 1 MB characters.
   */
  @Size(max = StringLengths.X_LARGE,
      message = "analysis-package-management.error.analysis-package.license.size")
  private String license;
}
