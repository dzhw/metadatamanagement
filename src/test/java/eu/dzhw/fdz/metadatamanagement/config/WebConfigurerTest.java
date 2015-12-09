/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

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
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockServletContext;

import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestReflectionHelper;

/**
 * @author Daniel Katzberg
 *
 */
public class WebConfigurerTest {

  @Test
  public void testWebConfigurationOnStartup() throws ServletException, NoSuchFieldException,
      SecurityException, IllegalArgumentException, IllegalAccessException {
    
    // Arrange
    WebConfigurer webConfigurer = new WebConfigurer();
    Environment environment = Mockito.mock(Environment.class);
    when(environment.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)).thenReturn(true);
    Field envField = UnitTestReflectionHelper.getDeclaredFieldForTestInvocation(webConfigurer.getClass(), "env");
    envField.set(webConfigurer, environment);
    
    // Create mocked Servlet Context, with default override methods for
    // correct run of the web configurer
    MockServletContext context = new MockServletContext("") {

      /*
       * (non-Javadoc)
       * 
       * @see org.springframework.mock.web.MockServletContext#addFilter(java. lang.String,
       * javax.servlet.Filter)
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
       * @see org.springframework.mock.web.MockServletContext#addServlet(java. lang.String,
       * javax.servlet.Servlet)
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
    assertThat(webConfigurer, not(nullValue()));
  }

}
