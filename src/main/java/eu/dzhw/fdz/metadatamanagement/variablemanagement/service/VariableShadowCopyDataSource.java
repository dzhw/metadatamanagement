package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides data for creating shadow copies of {@link Variable}.
 */
@Service
public class VariableShadowCopyDataSource implements ShadowCopyDataSource<Variable> {

  private VariableRepository variableRepository;

  public VariableShadowCopyDataSource(VariableRepository variableRepository) {
    this.variableRepository = variableRepository;
  }

  @Override
  public Stream<Variable> getMasters(String dataAcquisitionProjectId) {
    return variableRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Variable createShadowCopy(Variable source, String version) {
    String derivedId = source.getId() + "-" + version;
    Variable copy = variableRepository.findById(derivedId).orElseGet(Variable::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setDataSetId(source.getDataSetId() + "-" + version);
    copy.setStudyId(source.getStudyId() + "-" + version);
    copy.setSurveyIds(createDerivedSurveyIds(source.getSurveyIds(), version));
    return copy;
  }

  @Override
  public Optional<Variable> findPredecessorOfShadowCopy(String masterId) {
    return variableRepository.findByMasterIdAndSuccessorIdIsNullAndShadowIsTrue(masterId);
  }

  @Override
  public void updatePredecessor(Variable predecessor) {
    variableRepository.save(predecessor);
  }

  @Override
  public void saveShadowCopy(Variable shadowCopy) {
    variableRepository.save(shadowCopy);
  }

  @Override
  public Stream<Variable> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectId = projectId + "-" + previousVersion;
    return variableRepository
        .findByDataAcquisitionProjectIdAndSuccessorIdIsNullAndShadowIsTrue(oldProjectId);
  }

  private static List<String> createDerivedSurveyIds(List<String> surveyIds, String version) {
    return surveyIds.stream().map(studyId -> studyId + "-" + version).collect(Collectors.toList());
  }
}
