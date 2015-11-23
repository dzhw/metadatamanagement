/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Daniel Katzberg
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JHipsterProperties.class,
		MetricsConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class MetricsConfigurationTest {

	@Inject
	private JHipsterProperties jHipsterProperties;

	@Inject
	private MetricsConfiguration.GraphiteRegistry injectedGraphiteRegistry;

	@Inject
	private MetricsConfiguration.SparkRegistry injectedSparkRegistry;

	@Test
	public void testJHipsterPropertiesGraphit() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// Arrange
		this.jHipsterProperties.getMetrics().getGraphite().setEnabled(true);
		MetricsConfiguration.GraphiteRegistry graphite = new MetricsConfiguration.GraphiteRegistry();

		// Act
		Method declaresInitMethodGraphite = graphite.getClass().getDeclaredMethod("init");
		declaresInitMethodGraphite.setAccessible(true);
		declaresInitMethodGraphite.invoke(injectedGraphiteRegistry);

		// Assert
		assertThat(jHipsterProperties.getMetrics().getGraphite().getHost(), is("localhost"));
		assertThat(jHipsterProperties.getMetrics().getGraphite().getPort(), is(2003));
		assertThat(jHipsterProperties.getMetrics().getGraphite().getPrefix(), is("metadatamanagement"));
	}

	@Test
	public void testJHipsterPropertiesSpark() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// Arrange
		this.jHipsterProperties.getMetrics().getSpark().setEnabled(true);
		MetricsConfiguration.SparkRegistry spark = new MetricsConfiguration.SparkRegistry();

		// Act
		Method declaresInitMethodSpark = spark.getClass().getDeclaredMethod("init");
		declaresInitMethodSpark.setAccessible(true);
		declaresInitMethodSpark.invoke(injectedSparkRegistry);

		// Assert
		assertThat(jHipsterProperties.getMetrics().getSpark().getHost(), is("localhost"));
		assertThat(jHipsterProperties.getMetrics().getSpark().getPort(), is(9999));
	}

}
