package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;


import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data-acquisition-projects")
public class DataAcquisitionProjectController {

    private final DataAcquisitionProjectService dataAcquisitionProjectService;

    public DataAcquisitionProjectController(DataAcquisitionProjectService dataAcquisitionProjectService) {
        this.dataAcquisitionProjectService = dataAcquisitionProjectService;
    }

    @GetMapping("/findByIdLikeOrderByIdAsc")
    public ResponseEntity<List<DataAcquisitionProject>> findDataAcquisitionProjectListById(@RequestParam("id") String id) {
        List<DataAcquisitionProject> projects = dataAcquisitionProjectService.findDataAcquisitionProjectListById(id);
        return ResponseEntity.ok(projects);
    }
}
