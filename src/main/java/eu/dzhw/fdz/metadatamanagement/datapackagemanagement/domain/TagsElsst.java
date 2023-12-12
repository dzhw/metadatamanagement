package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.Elsst;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javers.core.metamodel.annotation.TypeName;

/**
 * Contains ELSST tags associated with a dataPackage.
 */
@Data
@TypeName("eu.dzhw.fdz.metadatamanagement.studymanagement.domain.TagsElsst")
@NoArgsConstructor
public class TagsElsst implements Serializable {

  private static final long serialVersionUID = 72245732592890720L;

  /**
   * German tags. At least one tag must be provided.
   */
  @NotEmpty(message = "datapackagemanagement.error.data-package.tagsElsst.not-empty")
  private Set<Elsst> de;

  /**
   * English tags (optional).
   */
  private Set<Elsst> en;
}
