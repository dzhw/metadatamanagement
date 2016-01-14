package eu.dzhw.fdz.metadatamanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;

/**
 * Interface used in projections to expose all attributes. Id and version are handled differently
 * per default in spring data rest!
 * 
 * @author René Reitmann
 */
@Projection(name = "complete", types = FdzProject.class)
public interface CompleteFdzProjectProjection extends AbstractFdzDomainObjectProjection {

  String getName();

  String getSufDoi();

  String getCufDoi();

}
