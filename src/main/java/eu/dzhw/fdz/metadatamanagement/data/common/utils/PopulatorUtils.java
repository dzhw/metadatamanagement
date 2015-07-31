package eu.dzhw.fdz.metadatamanagement.data.common.utils;

import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

/**
 * The PopulatorUtils reads all json files within a given classpath. The path includes as a
 * subfolder the locale/language.
 * 
 * @author Daniel Katzberg
 */
public class PopulatorUtils {

  /*
   * A slf4j logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(PopulatorUtils.class);

  /**
   * A resource loader, which loads/reads the json files by a given classpath.
   */
  private final ResourceLoader resourceLoader;

  @Autowired
  public PopulatorUtils(final ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  /**
   * Reads Resource files from the classpath data/(locale.getLanguage)/*.json.
   * 
   * @param locale A Locale for the setup of the used json files for the languages.
   * @return An array of resource files from the given data classpath.
   * 
   */
  public Resource[] loadJsonData(Locale locale) {
    try {
      return this.loadResources("classpath:data/" + locale.getLanguage() + "/*.json");
    } catch (IOException e) {
      LOG.error("Unable to load resources!", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * This method reads the resource files from the classpath.
   * 
   * @param pattern a location path for the json files in the classpath. for more information
   * @return An array of resource objects.
   * @throws IOException a given IO Exception for reading the resource files from the classpath.
   * @see ResourcePatternUtils
   */
  private Resource[] loadResources(String pattern) throws IOException {
    return ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader)
        .getResources(pattern);
  }

}
