/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.catalina.Wrapper;
import org.apache.catalina.core.ApplicationFilterRegistration;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockServletContext;

import eu.dzhw.fdz.metadatamanagement.AbstractBasicTest;

/**
 * @author Daniel Katzberg
 *
 */
public class WebConfigurerTest extends AbstractBasicTest {

	@Inject
	private Environment env;

	@Inject
	private WebConfigurer webConfigurer;

	@Test
	public void testWebConfigurationOnStartup() throws ServletException {
		
		// Arrange
		// Create mocked Servlet Context, with default override methods for
		// correct run of the web configurer
		MockServletContext context = new MockServletContext("") {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.springframework.mock.web.MockServletContext#addFilter(java.
			 * lang.String, javax.servlet.Filter)
			 */
			@Override
			public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
				FilterDef def = new FilterDef();
				def.setFilterName(filterName);
				StandardContext context = new StandardContext();
				context.addFilterDef(def);
				return new ApplicationFilterRegistration(def, context);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.springframework.mock.web.MockServletContext#addServlet(java.
			 * lang.String, javax.servlet.Servlet)
			 */
			@Override
			public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
				StandardContext context = new StandardContext();
				Wrapper wrapper = context.createWrapper();
				wrapper.setName(servletName);
				context.addChild(wrapper);
				wrapper.setServlet(servlet);
				return context.dynamicServletAdded(wrapper);
			}

		};

		// Act
		webConfigurer.onStartup(context);

		// Assert
		assertThat(env.getActiveProfiles().length, is(0));
	}

}
