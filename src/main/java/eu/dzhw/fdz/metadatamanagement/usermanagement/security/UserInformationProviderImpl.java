package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Service;

/**
 * Implementation for {@link UserInformationProvider}.
 */
@Service
class UserInformationProviderImpl implements UserInformationProvider {

  @Override
  public String getUserLogin() {
    return SecurityUtils.getCurrentUserLogin();
  }

  @Override
  public boolean isUserInRole(@NotEmpty String role) {
    return SecurityUtils.isUserInRole(role);
  }

  @Override
  public boolean isUserAnonymous() {
    return SecurityUtils.isUserAnonymous();
  }
}
