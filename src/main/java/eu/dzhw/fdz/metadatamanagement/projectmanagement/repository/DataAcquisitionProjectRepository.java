package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import java.util.List;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;

/**
 * Spring Data MongoDB repository for the data acquisitionProject entity.
 * 
 * @author Daniel Katzberg
 */
@RepositoryRestResource(path = "/data-acquisition-projects")
public interface DataAcquisitionProjectRepository
    extends BaseRepository<DataAcquisitionProject, String> {
  
  @RestResource(path = "findAll", rel = "findAll")
  List<DataAcquisitionProject> findAllBy();  
}
