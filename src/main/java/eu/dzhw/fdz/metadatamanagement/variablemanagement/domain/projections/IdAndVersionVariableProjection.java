package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * {@link IdAndVersionProjection} for {@link Variable}s.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = Variable.class)
public interface IdAndVersionVariableProjection extends IdAndVersionProjection {

}
