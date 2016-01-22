package eu.dzhw.fdz.metadatamanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;

/**
 * Projection used to expose all attributes (including ids and versions).
 * Spring Data rest does not expose ids and version per default in the json.
 * 
 * @author René Reitmann
 */
@Projection(name = "complete", types = FdzProject.class)
public interface CompleteFdzProjectProjection extends AbstractFdzDomainObjectProjection {
  String getSufDoi();

  String getCufDoi();
}
