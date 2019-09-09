package eu.dzhw.fdz.metadatamanagement.variablemanagement.service.helper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
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

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Override
  public Stream<Variable> getMasters(String dataAcquisitionProjectId) {
    return variableRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Variable createShadowCopy(Variable source, Release release) {
    String derivedId = source.getId() + "-" + release.getVersion();
    Variable copy = crudHelper.read(derivedId).orElseGet(Variable::new);
    BeanUtils.copyProperties(source, copy, "version", "surveyIds", "relatedQuestions",
        "relatedVariables");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setDataSetId(source.getDataSetId() + "-" + release.getVersion());
    copy.setStudyId(source.getStudyId() + "-" + release.getVersion());
    copy.setSurveyIds(createDerivedSurveyIds(source.getSurveyIds(), release.getVersion()));
    copy.setRelatedQuestions(
        createDerivedRelatedQuestions(source.getRelatedQuestions(), release.getVersion()));
    copy.setRelatedVariables(
        createDerivedRelatedVariables(source.getRelatedVariables(), release.getVersion()));
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
        BeanUtils.copyProperties(relatedQuestion, copy, "variableId", "questionId");
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

  @Override
  public void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      return variableRepository
          .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + releaseVersion);
    }, ElasticsearchType.variables);
    if (!StringUtils.isEmpty(previousVersion)) {
      elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
        return variableRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + previousVersion);
      }, ElasticsearchType.variables);
    }
  }
  
  @Override
  public void hideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, true);
  }

  private void setHiddenState(String projectId, String version, boolean hidden) {
    String shadowId = projectId + "-" + version;
    try (Stream<Variable> variables =
        variableRepository.findByDataAcquisitionProjectIdAndShadowIsTrue(shadowId)) {
      variables.forEach(shadow -> {
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
