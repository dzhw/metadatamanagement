package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Provides data for creating shadow copies of {@link Study}.
 */
@Service
public class StudyShadowCopyDataSource implements ShadowCopyDataSource<Study> {

  private StudyRepository studyRepository;

  public StudyShadowCopyDataSource(StudyRepository studyRepository) {
    this.studyRepository = studyRepository;
  }

  @Override
  public Stream<Study> getMasters(String dataAcquisitionProjectId) {
    return studyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Study createShadowCopy(Study source, String version) {
    String derivedId = source.getId() + "-" + version;
    Study copy = studyRepository.findById(derivedId).orElseGet(Study::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    return copy;
  }

  @Override
  public Optional<Study> findPredecessorOfShadowCopy(String masterId) {
    return studyRepository.findByMasterIdAndSuccessorIdIsNullAndShadowIsTrue(masterId);
  }

  @Override
  public void updatePredecessor(Study predecessor) {
    studyRepository.save(predecessor);
  }

  @Override
  public void saveShadowCopy(Study shadowCopy) {
    studyRepository.save(shadowCopy);
  }

  @Override
  public Stream<Study> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectId = projectId + "-" + previousVersion;
    return studyRepository
        .streamByDataAcquisitionProjectIdAndSuccessorIdIsNullAndShadowIsTrue(oldProjectId);
  }
}
