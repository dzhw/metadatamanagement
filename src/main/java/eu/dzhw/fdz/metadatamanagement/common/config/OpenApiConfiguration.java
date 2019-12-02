package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Enable swagger for all API endpoints.
 */
@Configuration
public class OpenApiConfiguration {

  /**
   * Expose only the docs for the order API.
   */
  @Bean
  public OpenAPI openApi(Environment env) {
    if (env.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_LOCAL))) {
      return new OpenAPI()
          .components(new Components())
         .info(new Info().title("Metadatamanagement API").description(
                  "This is the UI of the OpenAPI 3 documentation of the MDM."));
    }
    return new OpenAPI()
        .components(new Components())
        .info(new Info().title("Metadatamanagement API").description(
            "This is the UI of the OpenAPI 3 documentation of the MDM."));
  }
}
