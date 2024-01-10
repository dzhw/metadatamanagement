package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;
import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@link CustomDataPackage} maybe composed of several data sets coming form {@link DataSource}s
 * like this one.
 *
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class DataSource implements Serializable {
  private static final long serialVersionUID = -5737407422356435071L;

  /**
   * The name of the data source where the data is stored must be specified here (e.g. name of the
   * institution/repository, private data storage).
   *
   * Must not be empty and must not contain more than 512 characters.
   */
  @NotNull(message = "analysis-package-management.error.data-source.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "analysis-package-management.error.data-source" + ".i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "analysis-package-management.error.data-source." + "i18n-string-not-empty")
  private I18nString name;

  /**
   * Optional url for the data source.
   *
   * If specified it must be a valid URL and not longer than 2000 characters.
   */
  @URL(message = "analysis-package-management.error.data-source-url" + ".invalid-url")
  @Size(max = 2000, message = "analysis-package-management.error.data-source-url.length")
  private String url;

  /**
   * The license of this data source. Markdown is supported.
   *
   * May be empty. Must not contain more than 1 MB characters.
   */
  @Size(max = StringLengths.X_LARGE,
      message = "analysis-package-management.error.analysis-package.license.size")
  private String license;
}
