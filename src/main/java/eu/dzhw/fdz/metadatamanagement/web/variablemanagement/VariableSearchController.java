package eu.dzhw.fdz.metadatamanagement.web.variablemanagement;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableResource;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableResourceAssembler;

/**
 * Controller for searching variables.
 * 
 * @author Amine Limouri
 */
@Controller
@RequestMapping(value = "/{language}/variables")
public class VariableSearchController {

  @Autowired
  private VariableResourceAssembler variableResourceAssembler;

  @Autowired
  private PagedResourcesAssembler<VariableDocument> assembler;

  @Autowired
  private VariableService variableService;


  /**
   * Show variable search page.
   * 
   * @return variableSearch.html
   */
  @RequestMapping(method = RequestMethod.GET)
  public final Callable<String> showVariableSearch() {
    return () -> {
      return "variables/variableSearch";
    };
  }

  /**
   * Show variable search page.
   * 
   * @return variableSearch.html
   */
  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public Callable<String> search(String query, Pageable pageable, Model model) {
    return () -> {
      Page<VariableDocument> variableDocument = variableService.search(query, pageable);
      PagedResources<VariableResource> pagedVariableResource =
          assembler.toResource(variableDocument, variableResourceAssembler);

      System.err.println(pagedVariableResource.getContent().size());
      model.addAttribute("size", pagedVariableResource.getContent().size());
      model.addAttribute("query", query);
      model.addAttribute("variables", pagedVariableResource);
      return "variables/variableSearch";
    };
  }
}
