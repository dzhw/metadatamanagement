package eu.dzhw.fdz.metadatamanagement.common.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

import java.util.List;

/**
 * Provides domain objects for {@link ShadowCopyService} which are used to create shadow copies.
 */
public interface ShadowCopyDataProvider {

  /**
   * Returns the master objects which are used to derive shadow copies from.
   * @param dataAcquisitionProjectId Project id used to find master objects
   * @return List of master objects
   */
  List<AbstractShadowableRdcDomainObject> getMasters(String dataAcquisitionProjectId);

  /**
   * Create a copy of the given object.
   * @param source       The object source to copy
   * @param shadowCopyId The id to use for the copied object
   * @return Copied object
   */
  AbstractShadowableRdcDomainObject createShadowCopy(AbstractShadowableRdcDomainObject source,
                                                     String shadowCopyId);

  /**
   * Returns a list of the latest existing shadow copies.
   * @param dataAcquisitionProjectId Project id used to retrieve shadow copies
   * @return List of existing shadow copies which don't have a successor
   */
  List<AbstractShadowableRdcDomainObject> getLastShadowCopies(
      String dataAcquisitionProjectId);

  /**
   * Save the given shadow copies.
   * @param shadowCopies Shadow copies to save
   */
  void saveShadowCopies(List<? extends AbstractShadowableRdcDomainObject> shadowCopies);
}
