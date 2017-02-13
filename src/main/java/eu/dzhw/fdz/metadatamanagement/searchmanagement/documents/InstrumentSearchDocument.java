package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;

/**
 * Representation of an instrument which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class InstrumentSearchDocument extends Instrument {
  
  public InstrumentSearchDocument(Instrument instrument) {
    super(instrument);
  }
}
