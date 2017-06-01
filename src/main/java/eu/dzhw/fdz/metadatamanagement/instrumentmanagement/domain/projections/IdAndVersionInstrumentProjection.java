package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;

/**
 * {@link IdAndVersionProjection} for {@link Instrument}s.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = Instrument.class)
public interface IdAndVersionInstrumentProjection extends IdAndVersionProjection {

}
