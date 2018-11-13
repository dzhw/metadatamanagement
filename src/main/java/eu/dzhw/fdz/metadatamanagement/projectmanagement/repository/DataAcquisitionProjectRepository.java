package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Spring Data MongoDB repository for the data acquisitionProject entity.
 *
 * @author Daniel Katzberg
 */
@JaversSpringDataAuditable
@RepositoryRestResource(path = "/data-acquisition-projects")
public interface DataAcquisitionProjectRepository
  extends BaseRepository<DataAcquisitionProject, String>, DataAcquisitionProjectRepositoryCustom {

  List<DataAcquisitionProject> findByIdLikeOrderByIdAsc(@Param("id") String id);
}
