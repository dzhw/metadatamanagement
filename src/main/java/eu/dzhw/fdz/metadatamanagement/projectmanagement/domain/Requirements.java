package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation.EitherDataPackagesOrAnalysisPackagesRequired;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation.PublicationsRequiredForAnalysisPackages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This configuration defines which object types have to be delivered before a project can be
 * released.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValueObject
@EqualsAndHashCode
@Builder
@EitherDataPackagesOrAnalysisPackagesRequired(
    message = "data-acquisition-project-management.error.configuration.requirements"
        + ".either-data-packages-or-analysis-packages-required")
@PublicationsRequiredForAnalysisPackages(
    message = "data-acquisition-project-management.error.configuration.requirements"
        + ".publications-required-for-analysis-packages")
public class Requirements implements Serializable {

  private static final long serialVersionUID = 1549882098416793512L;

  /**
   * Defines if dataPackage data is required for a release (either this object type is mandatory or
   * analysis packages).
   */
  @JsonProperty("isDataPackagesRequired")
  private boolean isDataPackagesRequired;

  /**
   * Defines if analysis package data is required for a release (either this object type is
   * mandatory or data packages).
   */
  @JsonProperty("isAnalysisPackagesRequired")
  private boolean isAnalysisPackagesRequired;

  /**
   * Defines if survey data is required for a release.
   */
  @JsonProperty("isSurveysRequired")
  private boolean isSurveysRequired;

  /**
   * Defines if instrument data is required for a release.
   */
  @JsonProperty("isInstrumentsRequired")
  private boolean isInstrumentsRequired;

  /**
   * Defines if question data is required for a release.
   */
  @JsonProperty("isQuestionsRequired")
  private boolean isQuestionsRequired;

  /**
   * Defines if data set data is required for a release.
   */
  @JsonProperty("isDataSetsRequired")
  private boolean isDataSetsRequired;

  /**
   * Defines if variable data is required for a release.
   */
  @JsonProperty("isVariablesRequired")
  private boolean isVariablesRequired;

  /**
   * Defines if publication data is required for a release.
   */
  @JsonProperty("isPublicationsRequired")
  private boolean isPublicationsRequired;

  /**
   * Defines if concept data is required for a release.
   */
  @JsonProperty("isConceptsRequired")
  private boolean isConceptsRequired;
}
