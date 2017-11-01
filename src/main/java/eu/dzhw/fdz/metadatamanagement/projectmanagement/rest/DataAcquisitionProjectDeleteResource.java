package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectService;
import lombok.extern.slf4j.Slf4j;

/**
 * If a data acquisition project has been released before, it can not be deleted by anyone.
 * @author Daniel Katzberg
 *
 */
@RepositoryRestController
@Slf4j
public class DataAcquisitionProjectDeleteResource {
  @Autowired
  private DataAcquisitionProjectService dataAcquisitionProjectService;

  /**
   * Overwriting the delete data acquisition proect api method from mongo db.
   * @param id The id of the data acquisition project.
   * @return Return a 200 (ok) if successfull deleted or a Bad Request,
   *     if it has been released before and deleting is forbidden.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}", method = RequestMethod.DELETE)
  @Timed
  public ResponseEntity<?> deleteDataAcquisitionProject(@PathVariable String id) {
    
    //load project
    DataAcquisitionProject dataAcquisitionProject = this.dataAcquisitionProjectService.findOne(id);
    
    //project could not be found
    if (dataAcquisitionProject == null) {
      log.warn("Project could not be found and deleted!");
      return ResponseEntity.badRequest().build();
    }
    
    //Check project, if it has been released before
    if (this.dataAcquisitionProjectService.deleteDataAcquisitionProject(dataAcquisitionProject)) {
      log.info("Project has not been released before. Project is deleted.");
      return ResponseEntity.ok().build(); 
    } else {
      log.warn("Project has been released before. It is forbidden to delete it!");
      return ResponseEntity.badRequest().build();      
    }   
  }
  
}
