package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The project configuration describes which users are publishers or data providers for a project.
 */
@Data
@NoArgsConstructor
@ValueObject
@AllArgsConstructor
@Builder
public class Configuration implements Serializable {

  private static final long serialVersionUID = 1549882025184369274L;

  /**
   * User names having the role of a publisher for a project. Must contain at least one user
   * name.
   */
  @NotEmpty(message = "data-acquisition-project-management.error.configuration.publishers"
      + ".not-empty")
  @Builder.Default
  private List<String> publishers = new ArrayList<>();

  /**
   * User names having the role of a data provider for a project. Must contain at least one user
   * name.
   */
  @Builder.Default
  private List<String> dataProviders = new ArrayList<>();

  /**
   * Defines which object types are required before a project can be released.
   */
  @Valid
  @NotNull(message = "data-acquisition-project-management.error.required-object-types.not-null")
  @Builder.Default
  private Requirements requirements = new Requirements();

  /**
   * The state of the study.
   */
  private ProjectState studiesState;
  /**
   * The State of surveys.
   */
  private ProjectState surveysState;
  /**
   * The state of instruments.
   */
  private ProjectState instrumentsState;
  /**
   * The state of data sets.
   */
  private ProjectState dataSetsState;
  /**
   * The state of questions.
   */
  private ProjectState questionsState;
  /**
   * The state of variables.
   */
  private ProjectState variablesState;
  /**
   * The state of related publications.
   */
  private ProjectState publicationsState;
}
