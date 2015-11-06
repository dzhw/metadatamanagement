package eu.dzhw.fdz.metadatamanagement.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Class representing the Spring Security authenticated user.
 * 
 * @see UserDetails
 *
 */
public class CustomUserDetails implements UserDetails {

  private static final long serialVersionUID = 1L;

  private final Long id;
  private final String password;
  private final String username;
  private final Set<GrantedAuthority> authorities;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;

  /**
   * Constructor of the custom user details.
   * @param id the id of an user
   * @param username the username of an user
   * @param password the password of an user
   * @param authorities the authorities  of an user
   * @param accountNonExpired is an account non expired of an user
   * @param accountNonLocked is an account locked of an user
   * @param credentialsNonExpired are the credentials non expired of an user
   * @param enabled is the user enabled
   */
  public CustomUserDetails(Long id, String username, String password,
      Set<GrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked,
      boolean credentialsNonExpired, boolean enabled) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.accountNonExpired = accountNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.credentialsNonExpired = credentialsNonExpired;
    this.enabled = enabled;
  }

  public Long getId() {
    return id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public boolean isUserInRole(String authority) {
    return authorities.contains(new SimpleGrantedAuthority(authority));
  }

  @Override
  public String toString() {
    return "CustomUserDetails{" + id + ',' + username + ',' + authorities + '}';
  }
}
