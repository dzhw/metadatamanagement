package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

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
  public Stream<Variable> getLastShadowCopies(String dataAcquisitionProjectId) {
    return variableRepository.streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
        dataAcquisitionProjectId);
  }

  @Override
  public List<Variable> saveShadowCopies(List<Variable> shadowCopies) {
    return variableRepository.saveAll(shadowCopies);
  }

  private static List<String> createDerivedSurveyIds(List<String> surveyIds, String version) {
    return surveyIds.stream().map(studyId -> studyId + "-" + version).collect(Collectors.toList());
  }
}
