package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.Optional;
import java.util.stream.Stream;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;

/**
 * Provides domain objects for {@link ShadowCopyHelper} which are used to create shadow copies.
 * 
 * @param <T> The domain object to be copied.
 */
public interface ShadowCopyDataSource<T extends AbstractShadowableRdcDomainObject> {

  /**
   * Returns the master objects which are used to derive shadow copies from. Consumers are expected
   * to close the stream.
   * 
   * @param dataAcquisitionProjectId Project id used to find master objects
   * @return List of master objects
   */
  Stream<T> getMasters(String dataAcquisitionProjectId);

  /**
   * Create a copy of the given object.
   * 
   * @param source The object source to copy
   * @param release The release object containing the version to use as part of the id for the
   *        copied object
   * @return Copied object
   */
  T createShadowCopy(T source, Release release);

  /**
   * Find the predecessor of the given shadow copy.
   * 
   * @param shadowCopy Shadow copy for which the predecessor should be found
   * @param previousVersion The previous version of the project
   * @return Predecessor of the given shadow copy if existing
   */
  Optional<T> findPredecessorOfShadowCopy(T shadowCopy, String previousVersion);

  /**
   * Update an existing predecessor shadow copy.
   */
  void updatePredecessor(T predecessor);

  /**
   * Save the (new) shadow copy.
   * 
   * @param shadowCopy New shadow copy to save
   */
  void saveShadowCopy(T shadowCopy);

  /**
   * Find shadow copies where the master has been deleted between the last and the current project
   * release.
   * 
   * @param projectId Project id
   * @param previousVersion Previous project release version
   * @return Stream of shadow copies with deleted masters
   */
  Stream<T> findShadowCopiesWithDeletedMasters(String projectId, String previousVersion);

  /**
   * Delete all shadow copies. Only the shadow copies of the current version can be deleted.
   * 
   * @param projectId The id of the project.
   * @param version The current version of the project.
   */
  void deleteExistingShadowCopies(String projectId, String version);

  /**
   * Index all shadow copies for both versions.
   */
  void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion);
  
  /**
   * Hide the shadow copies of the given version.
   * @param projectId The id of the project.
   * @param version The version of the project to be hidden.
   */
  void hideExistingShadowCopies(String projectId, String version);
  
  /**
   * Unhide the shadow copies of the given version.
   * @param projectId The id of the project.
   * @param version The version of the project to be hidden.
   */
  void unhideExistingShadowCopies(String projectId, String version);
}
