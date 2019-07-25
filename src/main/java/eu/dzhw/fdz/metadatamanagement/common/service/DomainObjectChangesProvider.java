package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.HashMap;
import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

/**
 * Remember the previous version of a {@link AbstractShadowableRdcDomainObject} per request in order
 * to update elasticsearch correctly.
 * 
 * @param <T> The {@link AbstractRdcDomainObject} to manage.
 * @author René Reitmann
 */
public class DomainObjectChangesProvider<T extends AbstractRdcDomainObject> {
  protected Map<String, T> oldDomainObjects = new HashMap<>();
  protected Map<String, T> newDomainObjects = new HashMap<>();

  public void put(T oldDomainObject, T newDomainObject) {
    if (newDomainObject != null) {
      newDomainObjects.put(newDomainObject.getId(), newDomainObject);
    }
    if (oldDomainObject != null) {
      oldDomainObjects.put(oldDomainObject.getId(), oldDomainObject);
    }
  }
}
