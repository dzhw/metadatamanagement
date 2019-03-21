package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.stream.Stream;

/**
 * Spring Data MongoDB repository for the data acquisitionProject entity.
 * @author Daniel Katzberg
 */
@JaversSpringDataAuditable
@RepositoryRestResource(path = "/data-acquisition-projects")
public interface DataAcquisitionProjectRepository
    extends BaseRepository<DataAcquisitionProject, String>, DataAcquisitionProjectRepositoryCustom {

  List<DataAcquisitionProject> findByIdLikeOrderByIdAsc(@Param("id") String id);

  List<DataAcquisitionProject> findByIdLikeAndShadowIsFalseAndSuccessorIdIsNull(String id);

  List<DataAcquisitionProject>
      findAllByConfigurationPublishersContainsOrConfigurationDataProvidersContains(
      @Param("login") String publishers, @Param("login") String dataProviders);

  List<DataAcquisitionProject> findAllByConfigurationPublishersContains(
      @Param("login") String publishers);

  List<DataAcquisitionProject> findAllByConfigurationDataProvidersContains(
      @Param("login") String dataProviders);

  @RestResource(exported = false)
  Stream<DataAcquisitionProject> streamByIdAndShadowIsFalse(@Param("id") String id);

  @RestResource(exported = false)
  Stream<DataAcquisitionProject> streamByIdAndShadowIsTrueAndSuccessorIdIsNull(
      String dataAcquisitionProjectId);
}
