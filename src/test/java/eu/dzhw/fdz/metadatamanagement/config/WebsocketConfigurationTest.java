/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Daniel Katzberg
 *
 */
public class WebsocketConfigurationTest {
	
	@Test
	public void testHttpSessionHandshakeInterceptor() throws Exception {
		//Arrange
		WebsocketConfiguration websocketConfiguration = new WebsocketConfiguration();
		HandshakeInterceptor handshakeInterceptor = websocketConfiguration.httpSessionHandshakeInterceptor();
		Mockito.mock(ServletServerHttpRequest.class);
				
		//Act
		boolean before = handshakeInterceptor.beforeHandshake(Mockito.mock(ServletServerHttpRequest.class), null, null, new HashMap<>());
		handshakeInterceptor.afterHandshake(null, null, null, null);
		
		//Assert
		assertThat(before, is(true));
	}

}
