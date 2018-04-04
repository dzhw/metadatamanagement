package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.service.util.ListUtils;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;

/**
 * Remember the previous version of an instrument per request
 * in order to update elasticsearch correctly.
 *  
 * @author René Reitmann
 */
@Component
@RequestScope
public class InstrumentChangesProvider {
  
  private Map<String, Instrument> oldInstruments = new HashMap<>();
  private Map<String, Instrument> newInstruments = new HashMap<>();
  
  /**
   * Get the list of surveyIds which need to be updated.
   * @return a list of surveyIds
   */
  public List<String> getAffectedSurveyIds(String instrumentId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldInstruments.get(instrumentId) != null) {
      oldIds = oldInstruments.get(instrumentId).getSurveyIds();
    }
    if (newInstruments.get(instrumentId) != null) {
      newIds = newInstruments.get(instrumentId).getSurveyIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }
  
  protected void put(Instrument newInstrument, Instrument oldInstrument) {
    if (newInstrument != null) {      
      newInstruments.put(newInstrument.getId(), newInstrument);
    }
    if (oldInstrument != null) {      
      oldInstruments.put(oldInstrument.getId(), oldInstrument);
    }
  }
}
