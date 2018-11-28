package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javers.core.metamodel.annotation.ValueObject;

import javax.validation.constraints.AssertTrue;

/**
 * This configuration defines which object types have to be delivered before
 * a project can be released.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValueObject
@Builder
public class Requirements {

  /**
   * Defines if study data is required for a release (this object type is mandatory and this setting
   * is therefore always {@code true}.
   */
  @AssertTrue(message = "data-acquisition-project-management.error.required-object-types"
      + ".is-study-required.assert-true")
  @Setter(AccessLevel.NONE)
  private boolean isStudyRequired = true;

  /**
   * Defines if survey data is required for a release.
   */
  private boolean isSurveysRequired;

  /**
   * Defines if instrument data is required for a release.
   */
  private boolean isInstrumentsRequired;

  /**
   * Defines if question data is required for a release.
   */
  private boolean isQuestionsRequired;

  /**
   * Defines if data set data is required for a release.
   */
  private boolean isDataSetsRequired;

  /**
   * Defines if variable data is required for a release.
   */
  private boolean isVariablesRequired;
}
