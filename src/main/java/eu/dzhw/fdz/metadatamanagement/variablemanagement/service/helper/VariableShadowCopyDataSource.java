package eu.dzhw.fdz.metadatamanagement.variablemanagement.service.helper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link Variable}.
 */
@Component
@RequiredArgsConstructor
public class VariableShadowCopyDataSource implements ShadowCopyDataSource<Variable> {

  private final VariableRepository variableRepository;
  
  private final VariableCrudHelper crudHelper;

  @Override
  public Stream<Variable> getMasters(String dataAcquisitionProjectId) {
    return variableRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Variable createShadowCopy(Variable source, String version) {
    String derivedId = source.getId() + "-" + version;
    Variable copy = crudHelper.read(derivedId).orElseGet(Variable::new);
    BeanUtils.copyProperties(source, copy, "version", "surveyIds", "relatedQuestions",
        "relatedVariables");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setDataSetId(source.getDataSetId() + "-" + version);
    copy.setStudyId(source.getStudyId() + "-" + version);
    copy.setSurveyIds(createDerivedSurveyIds(source.getSurveyIds(), version));
    copy.setRelatedQuestions(createDerivedRelatedQuestions(source.getRelatedQuestions(), version));
    copy.setRelatedVariables(createDerivedRelatedVariables(source.getRelatedVariables(), version));
    return copy;
  }

  private List<String> createDerivedRelatedVariables(List<String> relatedVariables,
      String version) {
    if (relatedVariables != null) {
      return relatedVariables.stream().map(relatedVariable -> relatedVariable + "-" + version)
          .collect(Collectors.toList());
    } else {
      return null;
    }
  }

  private List<RelatedQuestion> createDerivedRelatedQuestions(
      List<RelatedQuestion> relatedQuestions, String version) {
    if (relatedQuestions != null) {
      return relatedQuestions.stream().map(relatedQuestion -> {
        RelatedQuestion copy = new RelatedQuestion();
        BeanUtils.copyProperties(relatedQuestion, copy, "instrumentId", "questionId");
        copy.setInstrumentId(relatedQuestion.getInstrumentId() + "-" + version);
        copy.setQuestionId(relatedQuestion.getQuestionId() + "-" + version);
        return copy;
      }).collect(Collectors.toList());
    } else {
      return null;
    }
  }

  @Override
  public Optional<Variable> findPredecessorOfShadowCopy(Variable shadowCopy,
      String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(Variable predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(Variable shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
  }

  @Override
  public Stream<Variable> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectId = projectId + "-" + previousVersion;
    return variableRepository.findByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !variableRepository.existsById(shadowCopy.getMasterId()));
  }

  private static List<String> createDerivedSurveyIds(List<String> surveyIds, String version) {
    return surveyIds.stream().map(studyId -> studyId + "-" + version).collect(Collectors.toList());
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    try (Stream<Variable> variables = variableRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(oldProjectId)) {
      variables.forEach(crudHelper::deleteShadow); 
    }
  }
}
