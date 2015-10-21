package eu.dzhw.fdz.metadatamanagement.web.common.search;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.service.common.BasicService;

/**
 * This is the abstract implementation of a details controller. The details controller handels the
 * details page
 * 
 * @param <D> Class of the {@link AbstractDocument}
 * @param <S> Class of the {@link BasicService}
 * @param <R> Class of the {@link ResourceSupport}
 * @param <A> Class of the {@link ResourceAssemblerSupport}
 * 
 * @author Daniel Katzberg
 *
 */
public abstract class AbstractDetailsController
    <D extends AbstractDocument, S extends BasicService<D>, 
    R extends ResourceSupport, A extends ResourceAssemblerSupport<D,R>> {

  protected S service;
  protected ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  protected A resourceAssembler;

  /**
   * Create the controller.
   * 
   * @param service the service managing the variable state
   * @param controllerLinkBuilderFactory a factory for building links to resources
   * @param resourceAssembler to transform a VariableDocument into a VariableResource.
   */
  @Autowired
  public AbstractDetailsController(S service,
      ControllerLinkBuilderFactory controllerLinkBuilderFactory,
      A resourceAssembler) {
    this.service = service;
    this.controllerLinkBuilderFactory = controllerLinkBuilderFactory;
    this.resourceAssembler = resourceAssembler;
  }

  /**
   * return the details or throw exception.
   * 
   * @return details.html
   */
  @RequestMapping(path = "/{id}", method = RequestMethod.GET)
  public abstract Callable<ModelAndView> get(@PathVariable("id") String id);


}
