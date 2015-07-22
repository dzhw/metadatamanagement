package eu.dzhw.fdz.metadatamanagement.config;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import eu.dzhw.fdz.metadatamanagement.config.https.HttpsRedirectFilter;
import eu.dzhw.fdz.metadatamanagement.web.util.UrlHelper;

/**
 * Spring configuration which instantiates some beans if and only if the container is running on
 * cloudfoundry.
 * 
 * @author René Reitmann
 */
@Configuration
@Profile("cloud")
public class CloudConfiguration {

  /**
   * Register a filter which redirects any http request to https. Since ssl is being offloaded at
   * the load balancer we need to check the custom http header 'x-forwarded-proto'.
   * 
   * @param urlHelper Helper for constructing the redirect url
   * @return The filter which redirects to https.
   */
  @Bean
  public Filter httpsRedirectFilter(UrlHelper urlHelper) {
    return new HttpsRedirectFilter(urlHelper);
  }
}
