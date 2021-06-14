package eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link Question}.
 */
@Component
@RequiredArgsConstructor
public class QuestionShadowCopyDataSource implements ShadowCopyDataSource<Question> {

  private final QuestionRepository questionRepository;

  private final QuestionCrudHelper crudHelper;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Override
  public Stream<Question> getMasters(String dataAcquisitionProjectId) {
    return questionRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Question createShadowCopy(Question source, Release release) {
    String derivedId = source.getId() + "-" + release.getVersion();
    Question copy = crudHelper.read(derivedId).orElseGet(Question::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setDataPackageId(source.getDataPackageId() + "-" + release.getVersion());
    copy.setInstrumentId(source.getInstrumentId() + "-" + release.getVersion());
    copy.setSuccessors(createDerivedSuccessorIds(source.getSuccessors(), release.getVersion()));
    return copy;
  }

  @Override
  public Optional<Question> findPredecessorOfShadowCopy(Question shadowCopy,
      String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(Question predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(Question shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
  }

  @Override
  public Stream<Question> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectId = projectId + "-" + previousVersion;
    return questionRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !questionRepository.existsById(shadowCopy.getMasterId()));
  }

  private List<String> createDerivedSuccessorIds(List<String> successorIds, String version) {
    return successorIds.stream().map(successorId -> successorId + "-" + version)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    try (Stream<Question> questions = questionRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)) {
      questions.forEach(crudHelper::deleteShadow);
    }
  }

  @Override
  public void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      return questionRepository
          .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + releaseVersion);
    }, ElasticsearchType.questions);
    if (!StringUtils.isEmpty(previousVersion)) {
      elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
        return questionRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + previousVersion);
      }, ElasticsearchType.questions);
    }
  }

  @Override
  public void hideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, true);
  }

  private void setHiddenState(String projectId, String version, boolean hidden) {
    String shadowId = projectId + "-" + version;
    try (Stream<Question> questions =
        questionRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(shadowId)) {
      questions.forEach(shadow -> {
        shadow.setHidden(hidden);
        crudHelper.saveShadow(shadow);
      });
    }
  }

  @Override
  public void unhideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, false);
  }
}
