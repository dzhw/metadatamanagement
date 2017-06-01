package eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;

/**
 * The Repository for {@link DataSet} domain object. The data will be insert with a REST API and
 * save in a mongo db.
 * 
 * @author Daniel Katzberg *
 */
@RepositoryRestResource(path = "/data-sets")
public interface DataSetRepository
    extends BaseRepository<DataSet, String> {

  @RestResource(exported = false)
  Stream<DataSet> streamByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  @RestResource(exported = true)
  List<DataSet> findByDataAcquisitionProjectId(
      @Param("dataAcquisitionProjectId") String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByStudyId(String studyId);
  
  @RestResource(exported = false)
  List<IdAndVersionProjection> findIdsByDataAcquisitionProjectIdAndNumber(
      String dataAcquisitionProjectId, Integer number);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsBySurveyIdsContaining(String surveyId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByIdIn(Collection<String> dataSetIds);

  @RestResource(exported = false)
  IdAndVersionProjection findOneIdById(String dataSetId);

  @RestResource(exported = false)
  List<DataSetSubDocumentProjection> findSubDocumentsByIdIn(Collection<String> dataSetIds);

  @RestResource(exported = false)
  List<DataSetSubDocumentProjection> findSubDocumentsByStudyId(String studyId);

  @RestResource(exported = false)
  List<DataSetSubDocumentProjection> findSubDocumentsBySurveyIdsContaining(String surveyId);

  @RestResource(exported = false)
  DataSetSubDocumentProjection findOneSubDocumentById(String dataSetId);

  @RestResource(exported = false)
  List<DataSet> findByStudyId(String id);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataAcquisitionProjectId(String projectId);  
}
