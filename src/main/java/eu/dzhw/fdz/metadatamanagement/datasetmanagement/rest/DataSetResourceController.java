package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ApprovedUsageEnum;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * DataSet REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@Controller
@Tag(name = "Dataset Resource", description = "Endpoints used by the MDM to manage datasets.")
@Slf4j
public class DataSetResourceController
    extends GenericDomainObjectResourceController<DataSet, CrudService<DataSet>> {

  @Autowired
  DataPackageRepository dataPackageRepository;

  public DataSetResourceController(CrudService<DataSet> crudService,
      UserInformationProvider userInformationProvider) {
    super(crudService, userInformationProvider);
  }

  @Override
  @Operation(
      summary = "Get the dataset. Public users will get the latest version of the dataset."
          + " If the id is postfixed with the version number it will return exactly the "
          + "requested version, if available.")
  @GetMapping(value = "/api/data-sets/{id:.+}")
  @ResponseBody
  public ResponseEntity<DataSet> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }

  @Override
  @PostMapping(value = "/api/data-sets")
  public ResponseEntity<?> postDomainObject(@RequestBody DataSet dataSet) {   
    addApprovedUsageIfDownloadableContent(dataSet);
    return super.postDomainObject(dataSet);
  }

  @Override
  @PutMapping(value = "/api/data-sets/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody DataSet dataSet) {
    addApprovedUsageIfDownloadableContent(dataSet);
    return super.putDomainObject(dataSet);
  }

  @Override
  @DeleteMapping("/api/data-sets/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(DataSet domainObject) {
    return UriComponentsBuilder.fromPath("/api/data-sets/" + domainObject.getId()).build().toUri();
  }

  private void addApprovedUsageIfDownloadableContent(DataSet dataSet) {
    boolean hasDownloadableContent = false;
    var subsets = dataSet.getSubDataSets();
    for (SubDataSet subDataSet : subsets) {
      String accessWay = subDataSet.getAccessWay();
      if (accessWay != null && accessWay.toLowerCase().contains("cuf")) {
          hasDownloadableContent = true;
      }
    }

    log.info("Data set has downloadable content, updating approved usages: " + hasDownloadableContent);
    if(hasDownloadableContent) {
      String projectId = dataSet.getDataAcquisitionProjectId();
      List<DataPackage> dataPackages = dataPackageRepository.findByDataAcquisitionProjectId(projectId);
      log.info("found " + dataPackages.size() + " data packages for project " + projectId);

      if(dataPackages.size() > 0 ) {
        log.info("updated approved usages for project " + projectId);
        DataPackage entity = dataPackages.get(0);
        List<ApprovedUsageEnum> approvedUsages = entity.getApprovedUsageList();
        if(approvedUsages == null) {
          approvedUsages =  new ArrayList<>();
          entity.setApprovedUsageList(approvedUsages);
        }
        approvedUsages.add(ApprovedUsageEnum.TEACHING_PURPOSES);
        
        dataPackageRepository.save(entity);
      }
    }
  }
}
