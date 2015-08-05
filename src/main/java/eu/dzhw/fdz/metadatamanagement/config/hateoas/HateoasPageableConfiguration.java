package eu.dzhw.fdz.metadatamanagement.config.hateoas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.HateoasAwareSpringDataWebConfiguration;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.hateoas.mvc.UriComponentsContributor;

/**
 * Configuration which sets the max page size for pageable requests and the default page request. We
 * also configure a {@link ControllerLinkBuilderFactory} which is able to link to methods with
 * {@link Pageable} as request param.
 * 
 * @author René Reitmann
 */
@Configuration
public class HateoasPageableConfiguration {

  private static final int MAX_PAGE_SIZE = 10;

  /**
   * Create the {@link ControllerLinkBuilderFactory} which is able to link to methods with
   * {@link Pageable} as request params.
   * 
   * @param pageableResolver the resolver as configured here
   *        {@link HateoasAwareSpringDataWebConfiguration}
   * @return the {@link ControllerLinkBuilderFactory}
   */
  @Bean
  public ControllerLinkBuilderFactory controllerLinkBuilderFactory(
      HateoasPageableHandlerMethodArgumentResolver pageableResolver) {
    // reconfigure pageable resolver here to avoid missing autoconfigured beans
    pageableResolver.setMaxPageSize(MAX_PAGE_SIZE);
    pageableResolver.setFallbackPageable(new PageRequest(0, MAX_PAGE_SIZE));

    // create a controller link builder factory which can resolve pageable request params
    ControllerLinkBuilderFactory controllerLinkBuilderFactory = new ControllerLinkBuilderFactory();
    List<UriComponentsContributor> uriComponentsContributors = new ArrayList<>();
    uriComponentsContributors.add(pageableResolver);
    controllerLinkBuilderFactory.setUriComponentsContributors(uriComponentsContributors);
    return controllerLinkBuilderFactory;
  }

}
