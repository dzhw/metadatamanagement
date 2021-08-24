package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
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
