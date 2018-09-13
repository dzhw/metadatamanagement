package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;

/**
 * Remember the previous version of a study per request
 * in order to update related publications correctly.
 *  
 * @author René Reitmann
 */
@Component
@RequestScope
public class StudyChangesProvider {
  private Map<String, Study> oldStudies = new HashMap<>();
  private Map<String, Study> newStudies = new HashMap<>();
   
  protected void put(Study newStudy, Study oldStudy) {
    if (newStudy != null) {      
      newStudies.put(newStudy.getId(), newStudy);
    }
    if (oldStudy != null) {      
      oldStudies.put(oldStudy.getId(), oldStudy);
    }
  }
  
  /**
   * Check if the study series for the given studyId has changed.
   * @param studyId the id of the study
   * @return true if the study series has changed
   */
  public boolean hasStudySeriesChanged(String studyId) {
    if (oldStudies.containsKey(studyId) && newStudies.containsKey(studyId)) {
      if (oldStudies.get(studyId).getStudySeries() == null) {
        return newStudies.get(studyId).getStudySeries() == null;
      }
      return !oldStudies.get(studyId).getStudySeries().equals(
          newStudies.get(studyId).getStudySeries());
    }
    return false;
  }
  
  /**
   * Get the old version of the studySeries.
   * @param studyId the id of the study
   * @return the old version of the studySeries
   */
  public I18nString getPreviousStudySeries(String studyId) {
    if (oldStudies.containsKey(studyId)) {
      return oldStudies.get(studyId).getStudySeries();
    }
    return null;
  }
}
