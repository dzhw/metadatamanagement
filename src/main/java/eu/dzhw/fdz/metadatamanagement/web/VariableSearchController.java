package eu.dzhw.fdz.metadatamanagement.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Controller for searching variables.
 * 
 * @author Amine Limouri
 */
@Controller
public class VariableSearchController {

  /**
   * Show variables page.
   * 
   * @return variables.html
   */
  @RequestMapping(value = "/variables", method = RequestMethod.GET)
  public DeferredResult<String> showVariableSearch() {
    DeferredResult<String> result = new DeferredResult<String>();
    result.setResult("variables/variableSearch");
    return result;
  }
}
