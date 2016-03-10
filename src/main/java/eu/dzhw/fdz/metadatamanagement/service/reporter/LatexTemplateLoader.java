package eu.dzhw.fdz.metadatamanagement.service.reporter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;

import freemarker.cache.TemplateLoader;

/**
 * The latex template loader is a wrapper for the default template loader. It puts a prefix and
 * suffix to the latex templates.
 * 
 * @author Daniel Katzberg
 *
 */
public class LatexTemplateLoader implements TemplateLoader {

  public static final String ESCAPE_PREFIX =
      "<#escape x as x?replace(\"\\\\\", \"\\\\textbackslash\")"
          + "?replace(\"{\", \"\\\\{\")?replace(\"}\", \"\\\\}\")"
          + "?replace(\"#\", \"\\\\#\")?replace(\"$\", \"\\\\$\")"
          + "?replace(\"%\", \"\\\\%\")?replace(\"&\", \"\\\\&\")"
          + "?replace(\"^\", \"\\\\textasciicircum\")?replace(\"_\", \"\\\\_\")"
          + "?replace(\"~\", \"\\\\textasciitilde\")>";

  public static final String ESCAPE_SUFFIX = "</#escape>";

  private final TemplateLoader templateLoader;

  /**
   * Constructor.
   * 
   * @param templateLoader A general template loader.
   */
  public LatexTemplateLoader(TemplateLoader templateLoader) {
    this.templateLoader = templateLoader;
  }

  /*
   * (non-Javadoc)
   * 
   * @see freemarker.cache.TemplateLoader#findTemplateSource(java.lang.String)
   */
  @Override
  public Object findTemplateSource(String name) throws IOException {
    return this.templateLoader.findTemplateSource(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see freemarker.cache.TemplateLoader#getLastModified(java.lang.Object)
   */
  @Override
  public long getLastModified(Object templateSource) {
    return this.templateLoader.getLastModified(templateSource);
  }

  /*
   * (non-Javadoc)
   * 
   * @see freemarker.cache.TemplateLoader#getReader(java.lang.Object, java.lang.String)
   */
  @Override
  public Reader getReader(Object templateSource, String encoding) throws IOException {
    Reader reader = this.templateLoader.getReader(templateSource, encoding);
    try {
      String templateText = IOUtils.toString(reader);
      return new StringReader(ESCAPE_PREFIX + templateText + ESCAPE_SUFFIX);
    } finally {
      IOUtils.closeQuietly(reader);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see freemarker.cache.TemplateLoader#closeTemplateSource(java.lang.Object)
   */
  @Override
  public void closeTemplateSource(Object templateSource) throws IOException {
    this.templateLoader.closeTemplateSource(templateSource);
  }

}
