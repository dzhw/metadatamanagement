package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.service.AbstractShadowCopyDataProvider;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides data for creating shadow copies of {@link Variable}.
 */
@Service
public class VariableShadowCopyDataProvider extends AbstractShadowCopyDataProvider<Variable> {

  private VariableRepository variableRepository;

  public VariableShadowCopyDataProvider(VariableRepository variableRepository) {
    super(Variable.class);
    this.variableRepository = variableRepository;
  }

  @Override
  protected void internalSave(List<Variable> shadowCopies) {
    variableRepository.saveAll(shadowCopies);
  }

  @Override
  protected AbstractShadowableRdcDomainObject internalCopy(Variable source, String version) {
    Variable copy = new Variable(source);
    copy.setId(source.getId() + "-" + version);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    return copy;
  }

  @Override
  protected Stream<Variable> internalGetMasters(String dataAcquisitionProjectId) {
    return variableRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  protected Stream<Variable> internalGetLastShadowCopies(String dataAcquisitionProjectId) {
    return variableRepository.streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
        dataAcquisitionProjectId);
  }
}
