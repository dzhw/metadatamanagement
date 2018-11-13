package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.javers.core.metamodel.annotation.ValueObject;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * The project configuration describes which users are publishers or data providers for a project.
 */
@Data
@NoArgsConstructor
@ValueObject
public class Configuration {
  /**
   * User names having the role of a publisher for a project. Must contain at least one user name.
   */
  @Size(min = 1, message = "data-acquisition-project-management.error.configuration.publishers.size")
  private List<String> publishers;

  /**
   * User names having the role of a data provider for a project. Must contain at least one user name.
   */
  @Size(min = 1, message = "data-acquisition-project-management.error.configuration.data-providers.size")
  private List<String> dataProviders;
}
