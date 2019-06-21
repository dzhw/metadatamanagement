package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * Provides data for creating shadow copies of {@link Study}.
 */
@Service
public class StudyShadowCopyDataSource implements ShadowCopyDataSource<Study> {

  private StudyRepository studyRepository;

  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  public StudyShadowCopyDataSource(StudyRepository studyRepository,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService) {
    this.studyRepository = studyRepository;
    this.elasticsearchUpdateQueueService = elasticsearchUpdateQueueService;
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
  public Optional<Study> findPredecessorOfShadowCopy(Study shadowCopy, String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return studyRepository.findById(shadowCopyId);
    }
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
    return studyRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !studyRepository.existsById(shadowCopy.getMasterId()));
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    List<IdAndVersionProjection> deletedIds = studyRepository
        .deleteByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(oldProjectId);
    for (IdAndVersionProjection study : deletedIds) {
      elasticsearchUpdateQueueService.enqueue(study.getId(), ElasticsearchType.studies,
          ElasticsearchUpdateQueueAction.DELETE);
    }
  }
}
