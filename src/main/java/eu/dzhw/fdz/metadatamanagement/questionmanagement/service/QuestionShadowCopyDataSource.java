package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;

/**
 * Provides data for creating shadow copies of {@link Question}.
 */
@Service
public class QuestionShadowCopyDataSource implements ShadowCopyDataSource<Question> {

  private QuestionRepository questionRepository;

  public QuestionShadowCopyDataSource(QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  @Override
  public Stream<Question> getMasters(String dataAcquisitionProjectId) {
    return questionRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Question createShadowCopy(Question source, String version) {
    String derivedId = source.getId() + "-" + version;
    Question copy = questionRepository.findById(derivedId).orElseGet(Question::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setStudyId(source.getStudyId() + "-" + version);
    copy.setInstrumentId(source.getInstrumentId() + "-" + version);
    copy.setSuccessors(createDerivedSuccessorIds(source.getSuccessors(), version));
    return copy;
  }

  @Override
  public Optional<Question> findPredecessorOfShadowCopy(Question shadowCopy, String previousVersion) {
    String shadowCopyId = shadowCopy + "-" + previousVersion;
    return questionRepository.findById(shadowCopyId);
  }

  @Override
  public void updatePredecessor(Question predecessor) {
    questionRepository.save(predecessor);
  }

  @Override
  public void saveShadowCopy(Question shadowCopy) {
    questionRepository.save(shadowCopy);
  }

  @Override
  public Stream<Question> findShadowCopiesWithDeletedMasters(String projectId,
      String lastVersion) {
    String oldProjectId = projectId + "-" + lastVersion;
    return questionRepository
        .streamByDataAcquisitionProjectIdAndSuccessorIdIsNullAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !questionRepository.existsById(shadowCopy.getMasterId()));
  }

  private List<String> createDerivedSuccessorIds(List<String> successorIds, String version) {
    return successorIds.stream().map(successorId -> successorId + "-" + version)
        .collect(Collectors.toList());
  }
}
