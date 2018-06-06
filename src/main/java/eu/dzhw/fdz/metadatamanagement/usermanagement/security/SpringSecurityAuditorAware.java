package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    String userName = SecurityUtils.getCurrentUserLogin();
    return (userName != null ? Optional.of(userName) 
        : Optional.of(Constants.SYSTEM_ACCOUNT));
  }
}
