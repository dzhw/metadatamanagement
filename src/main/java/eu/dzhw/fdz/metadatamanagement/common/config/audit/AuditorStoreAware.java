package eu.dzhw.fdz.metadatamanagement.common.config.audit;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;

/**
 * Implementation of AuditorAware based the AuditorStore
 * ThreadLocalTargetSource logic.
 */
@Component
@RequiredArgsConstructor
public class AuditorStoreAware implements AuditorAware<String> {

  private final AuditorStore auditorStore;

  @Override
  public Optional<String> getCurrentAuditor() {
    String auditor = auditorStore.getAuditor();

    return auditor != null
        ? Optional.of(auditor)
        : Optional.of(Constants.SYSTEM_ACCOUNT);
  }
}
