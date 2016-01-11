/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * No integration test, no application context needed.
 * 
 * @author Daniel Katzberg
 *
 */
public class CustomUserDetailsTest {

  @Test
  public void testCustomUserDetails() {
    // Arrange
    Set<GrantedAuthority> set = new HashSet<>();
    set.add(new SimpleGrantedAuthority("role"));
    CustomUserDetails customUserDetails =
        new CustomUserDetails("id", "username", "password", set, true, true, true, true);

    // Act

    // Assert
    assertThat(customUserDetails.getAuthorities(), not(nullValue()));
    assertThat(customUserDetails.getId(), is("id"));
    assertThat(customUserDetails.getUsername(), is("username"));
    assertThat(customUserDetails.getPassword(), is("password"));
    assertThat(customUserDetails.isAccountNonExpired(), is(true));
    assertThat(customUserDetails.isAccountNonLocked(), is(true));
    assertThat(customUserDetails.isCredentialsNonExpired(), is(true));
    assertThat(customUserDetails.isEnabled(), is(true));
    assertThat(customUserDetails.isUserInRole("role"), is(true));
    assertThat(customUserDetails.isUserInRole("otherrole"), is(false));
    assertThat(customUserDetails.toString(), is("CustomUserDetails{id,username,[role]}"));

  }
}
