package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.List;
import java.util.stream.Stream;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

/**
 * Provides domain objects for {@link ShadowCopyService} which are used to create shadow copies.
 * 
 * @param <T> The domain object to be copied.
 */
public interface ShadowCopyDataProvider<T extends AbstractShadowableRdcDomainObject> {

  /**
   * Returns the master objects which are used to derive shadow copies from. Consumers are expected
   * to close the stream.
   * @param dataAcquisitionProjectId Project id used to find master objects
   * @return List of master objects
   */
  Stream<T> getMasters(String dataAcquisitionProjectId);

  /**
   * Create a copy of the given object.
   * @param source       The object source to copy
   * @param version The version to use as part of the id for the copied object
   * @return Copied object
   */
  T createShadowCopy(T source, String version);

  /**
   * Returns a list of the latest existing shadow copies. Consumers are expected to close the
   * stream.
   * @param dataAcquisitionProjectId Project id used to retrieve shadow copies
   * @return List of existing shadow copies which don't have a successor
   */
  Stream<T> getLastShadowCopies(
      String dataAcquisitionProjectId);

  /**
   * Save the given shadow copies.
   * @param shadowCopies Shadow copies to save
   */
  void saveShadowCopies(List<T> shadowCopies);
}
