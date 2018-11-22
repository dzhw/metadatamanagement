package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javers.core.metamodel.annotation.ValueObject;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * The project configuration describes which users are publishers or data providers for a project.
 */
@Data
@NoArgsConstructor
@ValueObject
@AllArgsConstructor
@Builder
public class Configuration {
  /**
   * User names having the role of a publisher for a project. Must contain at least one user
   * name.
   */
  @NotEmpty(message = "data-acquisition-project-management.error.configuration.publishers"
      + ".not-empty")
  private List<String> publishers = new ArrayList<>();

  /**
   * User names having the role of a data provider for a project. Must contain at least one user
   * name.
   */
  private List<String> dataProviders = new ArrayList<>();

  /**
   * Defines which object types are required before a project can be released.
   */
  @Valid
  @NotNull(message = "data-acquisition-project-management.error.required-object-types.not-null")
  private Requirements requirements = new Requirements();
}
