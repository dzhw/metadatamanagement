package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;

/**
 * Complete Projection Instrument. This projection displays all attributes from the
 * instrument.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = Instrument.class)
public interface CompleteInstrumentProjection extends AbstractRdcDomainObjectProjection {

  String getDataAcquisitionProjectId();
  
  String getSurveyId();
}
