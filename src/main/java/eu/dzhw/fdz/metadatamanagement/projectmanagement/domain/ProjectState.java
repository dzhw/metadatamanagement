package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import org.javers.core.metamodel.annotation.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * State of a data acquisition project. Used for all metadata
 * 
 * @author tgehrke
 *
 */

@ValueObject
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectState implements Serializable {

  private static final long serialVersionUID = 1549882052456349234L;

  /**
   * indicates if the data providers marked it's metadata as ready.
   */
  @Builder.Default
  private boolean isDataProviderReady = false;
  /**
   * indicates if the publisher marked the metadata as ready.
   */
  @Builder.Default
  private boolean isPublisherReady = false;

}
