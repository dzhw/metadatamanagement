package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides data for creating shadow copies of {@link DataSet}.
 */
@Service
public class DataSetShadowCopyDataSource implements ShadowCopyDataSource<DataSet> {

  private DataSetRepository dataSetRepository;

  public DataSetShadowCopyDataSource(DataSetRepository dataSetRepository) {
    this.dataSetRepository = dataSetRepository;
  }

  @Override
  public Stream<DataSet> getMasters(String dataAcquisitionProjectId) {
    return dataSetRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public DataSet createShadowCopy(DataSet source, String version) {
    String derivedId = source.getId() + "-" + version;
    DataSet copy = dataSetRepository.findById(derivedId).orElseGet(DataSet::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setShadow(true);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setStudyId(source.getStudyId() + "-" + version);
    copy.setSurveyIds(createDerivedSurveyIds(source.getSurveyIds(), version));
    return copy;
  }

  @Override
  public Stream<DataSet> getLastShadowCopies(String dataAcquisitionProjectId) {
    return dataSetRepository.streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
        dataAcquisitionProjectId);
  }

  @Override
  public List<DataSet> saveShadowCopies(List<DataSet> shadowCopies) {
    return dataSetRepository.saveAll(shadowCopies);
  }

  private static List<String> createDerivedSurveyIds(List<String> surveyIds, String version) {
    return surveyIds.stream().map(surveyId -> surveyId + "-" + version)
        .collect(Collectors.toList());
  }

}
