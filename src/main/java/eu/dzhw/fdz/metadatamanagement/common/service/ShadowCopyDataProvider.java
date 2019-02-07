package eu.dzhw.fdz.metadatamanagement.common.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides domain objects for {@link ShadowCopyService} which are used to create shadow copies.
 */
public interface ShadowCopyDataProvider {

  /**
   * Returns the master objects which are used to derive shadow copies from. Consumers are expected
   * to close the stream.
   * @param dataAcquisitionProjectId Project id used to find master objects
   * @return List of master objects
   */
  Stream<AbstractShadowableRdcDomainObject> getMasters(String dataAcquisitionProjectId);

  /**
   * Create a copy of the given object.
   * @param source       The object source to copy
   * @param version The version to use as part of the id for the copied object
   * @return Copied object
   */
  AbstractShadowableRdcDomainObject createShadowCopy(AbstractShadowableRdcDomainObject source,
                                                     String version);

  /**
   * Returns a list of the latest existing shadow copies. Consumers are expected to close the
   * stream.
   * @param dataAcquisitionProjectId Project id used to retrieve shadow copies
   * @return List of existing shadow copies which don't have a successor
   */
  Stream<AbstractShadowableRdcDomainObject> getLastShadowCopies(
      String dataAcquisitionProjectId);

  /**
   * Save the given shadow copies.
   * @param shadowCopies Shadow copies to save
   */
  void saveShadowCopies(List<? extends AbstractShadowableRdcDomainObject> shadowCopies);
}
