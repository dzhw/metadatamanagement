package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;

/**
 * Projection used to expose all attributes (including ids and versions). Spring Data rest does not
 * expose ids and version per default in the json.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Projection(name = "complete", types = DataAcquisitionProject.class)
public interface CompleteDataAcquisitionProjectProjection
    extends AbstractRdcDomainObjectProjection {
  Release getRelease();
}
