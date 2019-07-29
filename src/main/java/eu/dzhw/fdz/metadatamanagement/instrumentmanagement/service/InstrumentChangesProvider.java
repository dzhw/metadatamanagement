package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.common.service.util.ListUtils;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;

/**
 * Tracks an {@link Instrument}'s data before and after saving it through the repository and
 * provides access to some changes.
 * 
 * @author René Reitmann
 */
@Component
@RequestScope
public class InstrumentChangesProvider extends DomainObjectChangesProvider<Instrument> {
  /**
   * Get the list of surveyIds which need to be updated.
   * 
   * @return a list of surveyIds
   */
  public List<String> getAffectedSurveyIds(String instrumentId) {
    List<String> oldIds = null;
    List<String> newIds = null;
    if (oldDomainObjects.get(instrumentId) != null) {
      oldIds = oldDomainObjects.get(instrumentId).getSurveyIds();
    }
    if (newDomainObjects.get(instrumentId) != null) {
      newIds = newDomainObjects.get(instrumentId).getSurveyIds();
    }
    return ListUtils.combineUniquely(newIds, oldIds);
  }

  /**
   * Get removed concept id of an Instrument.
   * 
   * @param instrumentId Instrument id
   * @return Removed concept ids
   */
  public Set<String> getRemovedConceptIds(String instrumentId) {
    Instrument oldInstrument = oldDomainObjects.get(instrumentId);

    if (oldInstrument == null) {
      return Collections.emptySet();
    }

    Instrument newInstrument = newDomainObjects.get(instrumentId);

    if (newInstrument == null) {
      return Collections.emptySet();
    }

    List<String> oldConceptIds = oldInstrument.getConceptIds();

    if (oldConceptIds == null || oldConceptIds.isEmpty()) {
      return Collections.emptySet();
    }

    List<String> newConceptIds = newInstrument.getConceptIds();

    if (newConceptIds == null || newConceptIds.isEmpty()) {
      return new HashSet<>(oldConceptIds);
    }

    return oldConceptIds.stream().filter(oldConceptId -> !newConceptIds.contains(oldConceptId))
        .collect(Collectors.toSet());
  }
}
