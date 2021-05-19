package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.Arrays;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import eu.dzhw.fdz.metadatamanagement.common.rest.filter.CachingHttpHeadersFilter;
import eu.dzhw.fdz.metadatamanagement.common.rest.filter.StaticResourcesProductionFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
@Slf4j
public class WebConfigurer implements ServletContextInitializer, WebMvcConfigurer,
    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
  @Autowired
  private Environment env;

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    log.info("Web application configuration, using profiles: {}",
        Arrays.toString(env.getActiveProfiles()));
    EnumSet<DispatcherType> disps =
        EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
    initCachingHttpHeadersFilter(servletContext, disps);
    if (env.acceptsProfiles(Profiles.of("!" + Constants.SPRING_PROFILE_LOCAL + " | ("
        + Constants.SPRING_PROFILE_LOCAL + " & " + Constants.SPRING_PROFILE_MINIFIED + ")"))) {
      initStaticResourcesProductionFilter(servletContext, disps);
    }
    log.info("Web application fully configured");
  }

  /**
   * Set up Mime types.
   */
  @Override
  public void customize(ConfigurableServletWebServerFactory container) {
    MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
    // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
    mappings.add("html", "text/html;charset=utf-8");
    // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
    mappings.add("json", "application/json;charset=utf-8");

    mappings.add("svg", "image/svg+xml");
    mappings.add("ttf", "application/x-font-ttf");
    mappings.add("otf", "application/x-font-opentype");
    mappings.add("woff", "application/font-woff");
    mappings.add("woff2", "application/font-woff2");
    mappings.add("eot", "application/vnd.ms-fontobject");
    mappings.add("sfnt", "application/font-sfnt");
    mappings.add("odt", "application/vnd.oasis.opendocument.text");
    mappings.add("pdf", "application/pdf");
    mappings.add("jpg", "image/jpeg");
    mappings.add("jpeg", "image/jpeg");
    mappings.add("png", "image/png");
    mappings.add("gif", "image/gif");
    mappings.add("txt", "text/plain");

    container.setMimeMappings(mappings);
  }

  /**
   * Initializes the static resources production Filter.
   */
  private void initStaticResourcesProductionFilter(ServletContext servletContext,
      EnumSet<DispatcherType> disps) {
    log.debug("Registering static resources production Filter");
    FilterRegistration.Dynamic staticResourcesProductionFilter = servletContext
        .addFilter("staticResourcesProductionFilter", new StaticResourcesProductionFilter());

    staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/");
    staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/index.html");
    staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/manifest.json");
    staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
    staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
    staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/node_modules/*");
    staticResourcesProductionFilter.setAsyncSupported(true);
  }

  /**
   * Initializes the caching HTTP Headers Filter.
   */
  private void initCachingHttpHeadersFilter(ServletContext servletContext,
      EnumSet<DispatcherType> disps) {
    log.debug("Registering Caching HTTP Headers Filter");
    FilterRegistration.Dynamic cachingHttpHeadersFilter =
        servletContext.addFilter("cachingHttpHeadersFilter", new CachingHttpHeadersFilter());

    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/dist/*");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/index.html");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/robots.txt");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/robots-prod.txt");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/robots-test.txt");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/manifest.json");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/node_modules/*");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
    cachingHttpHeadersFilter.setAsyncSupported(true);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (env.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_LOCAL))
        && !env.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_MINIFIED))) {
      registry.addResourceHandler("/node_modules/**").addResourceLocations("file:node_modules/");
    }
  }
}
