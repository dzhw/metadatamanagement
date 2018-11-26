package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;

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
}
