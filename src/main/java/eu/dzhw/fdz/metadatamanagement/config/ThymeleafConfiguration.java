package eu.dzhw.fdz.metadatamanagement.config;

import org.apache.commons.lang.CharEncoding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Configure Thymeleaf for HTML mails.
 */
@Configuration
public class ThymeleafConfiguration {

  /**
   * Configure the template resolver for thymeleaf.
   */
  @Bean
  @Description("Thymeleaf template resolver serving HTML 5 emails")
  public ClassLoaderTemplateResolver emailTemplateResolver() {
    ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
    emailTemplateResolver.setPrefix("mails/");
    emailTemplateResolver.setSuffix(".html");
    emailTemplateResolver.setTemplateMode("HTML5");
    emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
    emailTemplateResolver.setOrder(1);
    return emailTemplateResolver;
  }
}
