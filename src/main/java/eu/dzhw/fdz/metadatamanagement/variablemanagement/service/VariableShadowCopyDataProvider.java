package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataProvider;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * Provides data for creating shadow copies of {@link Variable}.
 */
@Service
public class VariableShadowCopyDataProvider implements ShadowCopyDataProvider<Variable> {

  private VariableRepository variableRepository;

  public VariableShadowCopyDataProvider(VariableRepository variableRepository) {
    this.variableRepository = variableRepository;
  }

  @Override
  public Stream<Variable> getMasters(String dataAcquisitionProjectId) {
    return variableRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Variable createShadowCopy(Variable source, String version) {
    Variable copy = new Variable(source);
    copy.setId(source.getId() + "-" + version);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    // TODO es-flavia: missing dataSetId, surveyIds
    return copy;
  }

  @Override
  public Stream<Variable> getLastShadowCopies(String dataAcquisitionProjectId) {
    return variableRepository.streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
        dataAcquisitionProjectId);
  }

  @Override
  public void saveShadowCopies(List<Variable> shadowCopies) {
    variableRepository.saveAll(shadowCopies);
  }
}
