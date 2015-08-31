package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;

/**
 * A Controller which returns a details page for a variable.
 * 
 */
@Controller
@RequestMapping(path = "/{language:de|en}/variables")
public class VariableDetailsController {

  private VariableResourceAssembler variableResourceAssembler;
  private VariableService variableService;

  /**
   * Create the controller.
   * 
   * @param variableService the service managing the variable state
   * @param variableResourceAssembler to transform a VariableDocument into a VariableResource.
   */
  @Autowired
  public VariableDetailsController(VariableService variableService,
          VariableResourceAssembler variableResourceAssembler) {
    this.variableService = variableService;
    this.variableResourceAssembler = variableResourceAssembler;
  }

  /**
   * Show variable datails page.
   * 
   * @return String as JSON Media type
   */
  @RequestMapping(path = "/{variableId}", method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public
          Callable<ResponseEntity<VariableResource>> get(
                  @PathVariable("variableId") String id) {
    return () -> {
      VariableDocument variableDocument = variableService.get(id);
      if (variableDocument != null) {
        return new ResponseEntity<VariableResource>(
                variableResourceAssembler.toResource(variableDocument), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    };
  }
}
