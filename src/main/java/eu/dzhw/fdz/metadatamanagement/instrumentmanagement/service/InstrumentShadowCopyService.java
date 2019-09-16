package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper.InstrumentShadowCopyDataSource;

/**
 * Service which generates shadow copies of all instruments of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class InstrumentShadowCopyService extends ShadowCopyHelper<Instrument> {
  public InstrumentShadowCopyService(
      InstrumentShadowCopyDataSource instrumentShadowCopyDataSource) {
    super(instrumentShadowCopyDataSource);
  }
}
