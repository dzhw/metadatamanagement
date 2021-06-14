package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import java.util.List;
import java.util.stream.Stream;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

/**
 * Spring Data MongoDB repository for the data acquisitionProject entity.
 * 
 * @author Daniel Katzberg
 */
@JaversSpringDataAuditable
@RepositoryRestResource(path = "data-acquisition-projects")
public interface DataAcquisitionProjectRepository
    extends BaseRepository<DataAcquisitionProject, String>, DataAcquisitionProjectRepositoryCustom {

  @RestResource(exported = false)
  List<DataAcquisitionProject> findByIdLikeOrderByIdAsc(String id);

  @RestResource(exported = false)
  List<DataAcquisitionProject> findByIdLikeAndShadowIsFalseAndSuccessorIdIsNull(String id);

  @RestResource(exported = true)
  List<DataAcquisitionProject>
      findAllByConfigurationPublishersContainsOrConfigurationDataProvidersContainsAndShadowIsFalse(
      @Param("login") String publishers, @Param("login") String dataProviders);

  @RestResource(exported = true)
  List<DataAcquisitionProject> findAllByConfigurationPublishersContainsAndShadowIsFalse(
      @Param("login") String publishers);

  @RestResource(exported = true)
  List<DataAcquisitionProject> findAllByConfigurationDataProvidersContainsAndShadowIsFalse(
      @Param("login") String dataProviders);

  @RestResource(exported = false)
  Stream<DataAcquisitionProject> streamByIdAndShadowIsFalse(String id);

  @RestResource(exported = false)
  Stream<DataAcquisitionProject> streamByIdAndShadowIsTrueAndSuccessorIdIsNull(
      String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<DataAcquisitionProject> streamByIdAndShadowIsTrue(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  Stream<DataAcquisitionProject> findByMasterIdAndShadowIsTrue(String masterId);
}
