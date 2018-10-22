package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Get only the instrument id and the instrument number of an instrument.
 *
 * @author René Reitmann
 */
public interface IdAndNumberInstrumentProjection 
    extends IdAndVersionProjection {
  Integer getNumber();
}
