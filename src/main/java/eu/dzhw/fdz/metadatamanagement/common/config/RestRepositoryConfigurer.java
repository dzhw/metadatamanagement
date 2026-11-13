package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy.RepositoryDetectionStrategies;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Configure Spring Data Rest to validate on PUT and POST. Spring data rest converts the validation
 * message in json and send a 400 when validation fails.
 * 
 * @author René Reitmann
 */
@Component
public class RestRepositoryConfigurer implements RepositoryRestConfigurer {
  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
      CorsRegistry cors) {
    config.setExposeRepositoryMethodsByDefault(true);

    // Only export repositories annotated with @RepositoryRestResource. This hides the internal
    // repositories (task, queue and websocket-session repositories) which would otherwise be
    // exported with full CRUD to any authenticated user.
    config.setRepositoryDetectionStrategy(RepositoryDetectionStrategies.ANNOTATED);

    // Disable all Spring Data Rest write operations. Writes must go through the custom controllers
    // and their @Secured *ManagementService classes. This also closes the PATCH item handler, which
    // the custom controllers do not override and which would otherwise bypass the security checks.
    // Only Spring Data Rest's own handlers are affected; the custom @(Repository)RestController write
    // mappings are regular MVC handlers and keep working.
    config.getExposureConfiguration()
        .withItemExposure((metadata, httpMethods) ->
            httpMethods.disable(HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE))
        .withCollectionExposure((metadata, httpMethods) ->
            httpMethods.disable(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE));

    config.exposeIdsFor(IdAndVersionProjection.class);
    final ClassPathScanningCandidateComponentProvider provider =
        new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AssignableTypeFilter(AbstractRdcDomainObject.class));
    provider.addIncludeFilter(new AssignableTypeFilter(AbstractShadowableRdcDomainObject.class));
    provider.addIncludeFilter(new AssignableTypeFilter(AbstractRdcDomainObjectProjection.class));

    final Set<BeanDefinition> beans =
        provider.findCandidateComponents("eu.dzhw.fdz.metadatamanagement.**.domain");

    for (final BeanDefinition bean : beans) {
      try {
        config.exposeIdsFor(Class.forName(bean.getBeanClassName()));
      } catch (final ClassNotFoundException e) {
        // Can't throw ClassNotFoundException due to the method signature. Need to cast it
        throw new IllegalStateException("Failed to expose `id` field due to", e);
      }
    }
  }
}
