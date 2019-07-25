package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;

/**
 * Remember the previous version of a study per request
 * in order to update related publications correctly.
 *  
 * @author René Reitmann
 */
@Component
@RequestScope
public class StudyChangesProvider extends DomainObjectChangesProvider<Study> {  
  /**
   * Check if the study series for the given studyId has changed.
   * @param studyId the id of the study
   * @return true if the study series has changed
   */
  public boolean hasStudySeriesChanged(String studyId) {
    if (oldDomainObjects.containsKey(studyId) && newDomainObjects.containsKey(studyId)) {
      if (oldDomainObjects.get(studyId).getStudySeries() == null) {
        return newDomainObjects.get(studyId).getStudySeries() == null;
      }
      return !oldDomainObjects.get(studyId).getStudySeries().equals(
          newDomainObjects.get(studyId).getStudySeries());
    }
    return false;
  }
  
  /**
   * Get the old version of the studySeries.
   * @param studyId the id of the study
   * @return the old version of the studySeries
   */
  public I18nString getPreviousStudySeries(String studyId) {
    if (oldDomainObjects.containsKey(studyId)) {
      return oldDomainObjects.get(studyId).getStudySeries();
    }
    return null;
  }
}
