/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.OAuth2AuthenticationReadConverter;

/**
 * @author Daniel Katzberg
 *
 */
public class OAuth2AuthenticationReadConverterTest {

  @Test
  public void testConvert() {
    // Arrange
    OAuth2AuthenticationReadConverter readConverter = new OAuth2AuthenticationReadConverter();
    DBObject dbObject = this.buildDBObject(true);

    // Act
    OAuth2Authentication auth2Authentication = readConverter.convert(dbObject);

    // Assert
    assertThat(auth2Authentication, not(nullValue()));
    assertThat(auth2Authentication.getName(), is("username"));
    assertThat(auth2Authentication.isAuthenticated(), is(true));
  } 
  
  @Test
  public void testConvertWithNoDBObjectAsPrincipal() {
    // Arrange
    OAuth2AuthenticationReadConverter readConverter = new OAuth2AuthenticationReadConverter();
    DBObject dbObject = this.buildDBObject(false);

    // Act
    OAuth2Authentication auth2Authentication = readConverter.convert(dbObject);

    // Assert
    assertThat(auth2Authentication, not(nullValue()));
    assertThat(auth2Authentication.getName(), is("principal"));
    assertThat(auth2Authentication.isAuthenticated(), is(true));
  } 
  
  
  private DBObject buildDBObject(boolean correctPrincipal) {
    
    DBObject source = new BasicDBObject();    
        
    //StoredRequest
    DBObject storedRequest = new BasicDBObject();
    storedRequest.put("requestParameters", new HashMap<>());
    storedRequest.put("clientId", "clientId");
    storedRequest.put("scope", new ArrayList<>());
    
    //Principal
    DBObject principal = new BasicDBObject();
    principal.put("username", "username");
    principal.put("enabled", true);
    principal.put("accountNonExpired", true);
    principal.put("credentialsNonExpired", true);
    principal.put("accountNonLocked", true);
    
    String principalStr = "principal";
    
    //User Authentication
    DBObject userAuthentication = new BasicDBObject();
    List<Map<String, String>> authorities = new ArrayList<>();
    Map<String, String> role = new HashMap<>();
    role.put("role", "USER");
    authorities.add(role);
    userAuthentication.put("credentials", new Object());
    userAuthentication.put("authorities", authorities);
    
    if(correctPrincipal) {
      userAuthentication.put("principal", principal);
    } else {
      userAuthentication.put("principal", principalStr);
    }
    
    //create source element
    source.put("storedRequest", storedRequest);
    source.put("userAuthentication", userAuthentication);

    return source;
  }
}
