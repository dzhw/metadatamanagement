package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Analysis package REST Controller which overrides default spring data rest methods.
 *
 * @author René Reitmann
 */
@Controller
@Tag(name = "Analysis Package Resource",
    description = "Endpoints used by the MDM to manage analysis packages.")
@RequestMapping("/api")
public class AnalysisPackageResourceController
    extends GenericDomainObjectResourceController<AnalysisPackage, CrudService<AnalysisPackage>> {

  /**
   * Construct the controller.
   */
  public AnalysisPackageResourceController(CrudService<AnalysisPackage> crudService) {
    super(crudService);
  }

  @Override
  @Operation(summary = "Get the analysis package. Public users will get the latest version of the"
      + " analysis package. If the id is postfixed with the version number it will return"
      + " exactly the requested version, if available.")
  @GetMapping(value = "/analysis-packages/{id:.+}")
  @ResponseBody
  public ResponseEntity<AnalysisPackage> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }


  @Override
  @PostMapping(value = "/analysis-packages")
  public ResponseEntity<?> postDomainObject(@RequestBody AnalysisPackage analysisPackage) {
    return super.postDomainObject(analysisPackage);
  }

  @Override
  @PutMapping(value = "/analysis-packages/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody AnalysisPackage analysisPackage) {
    return super.putDomainObject(analysisPackage);
  }

  @Override
  @DeleteMapping("/analysis-packages/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(AnalysisPackage domainObject) {
    return UriComponentsBuilder.fromPath("/api/analysis-packages/" + domainObject.getId()).build()
        .toUri();
  }
}
