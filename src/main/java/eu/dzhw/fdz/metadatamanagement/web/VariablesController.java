package eu.dzhw.fdz.metadatamanagement.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Controller for searching variables.
 * 
 * @author Amine Limouri
 */
@Controller
public class VariablesController {

  /**
   * Show variables page.
   * 
   * @return variables.html
   */
  @RequestMapping("/variables")
  public DeferredResult<String> showVariablesFile() {
    DeferredResult<String> result = new DeferredResult<String>();
    result.setResult("variables/variables");
    return result;
  }
}
