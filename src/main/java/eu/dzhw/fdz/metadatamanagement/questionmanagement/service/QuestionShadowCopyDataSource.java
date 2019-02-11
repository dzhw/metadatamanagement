package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    copy.setShadow(true);
    copy.setInstrumentId(source.getInstrumentId() + "-" + version);
    copy.setSuccessors(createDerivedSuccessorIds(source.getSuccessors(), version));
    return copy;
  }

  @Override
  public Stream<Question> getLastShadowCopies(String dataAcquisitionProjectId) {
    return questionRepository
        .streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
            dataAcquisitionProjectId);
  }

  @Override
  public List<Question> saveShadowCopies(List<Question> shadowCopies) {
    return questionRepository.saveAll(shadowCopies);
  }

  private List<String> createDerivedSuccessorIds(List<String> successorIds, String version) {
    return successorIds.stream().map(successorId -> successorId + "-" + version)
        .collect(Collectors.toList());
  }
}
