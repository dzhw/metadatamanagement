package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;

/**
 * Remember the previous version of a dataPackage per request
 * in order to update related publications correctly.
 *
 * @author René Reitmann
 */
@Component
@RequestScope
public class DataPackageChangesProvider extends DomainObjectChangesProvider<DataPackage> {
  /**
   * Check if the dataPackage series for the given dataPackageId has changed.
   * @param dataPackageId the id of the dataPackage
   * @return true if the dataPackage series has changed
   */
  public boolean hasStudySeriesChanged(String dataPackageId) {
    if (oldDomainObjects.containsKey(dataPackageId)
        && newDomainObjects.containsKey(dataPackageId)) {
      if (oldDomainObjects.get(dataPackageId).getStudySeries() == null) {
        return newDomainObjects.get(dataPackageId).getStudySeries() == null;
      }
      return !oldDomainObjects.get(dataPackageId).getStudySeries().equals(
          newDomainObjects.get(dataPackageId).getStudySeries());
    }
    return false;
  }

  /**
   * Get the old version of the studySeries.
   * @param dataPackageId the id of the dataPackage
   * @return the old version of the studySeries
   */
  public I18nString getPreviousStudySeries(String dataPackageId) {
    if (oldDomainObjects.containsKey(dataPackageId)) {
      return oldDomainObjects.get(dataPackageId).getStudySeries();
    }
    return null;
  }
}
