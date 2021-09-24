package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import javax.validation.constraints.NotEmpty;

import org.javers.core.metamodel.annotation.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A {@link eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage} which has been
 * used in the {@link AnalysisPackage}.
 * 
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@ValueObject
public class DataPackage extends AbstractAnalysisDataPackage {
  private static final long serialVersionUID = 6160837016758021746L;

  @NotEmpty(message = "analysis-package-management.error.analyzed-data-package."
      + "data-package-master-id.not-empty")
  private String dataPackageMasterId;

  // TODO validate
  @NotEmpty(
      message = "analysis-package-management.error.analyzed-data-package." + "access-way.not-empty")
  private String accessWay;

  @NotEmpty(
      message = "analysis-package-management.error.analyzed-data-package." + "version.not-empty")
  private String version;
}
