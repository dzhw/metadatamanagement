package eu.dzhw.fdz.metadatamanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;

/**
 * The 'complete' Projection of a concept domain object. 'complete' means all attributes will be
 * displayed.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = Concept.class)
public interface CompleteConceptProjection extends AbstractRdcDomainObjectProjection {

  I18nString getName();

  I18nString getDescription();

}
