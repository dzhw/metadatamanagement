/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service.reporter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestCreateDomainObjectUtils;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

/**
 * @author Daniel Katzberg
 *
 */
// TODO DKatzberg Prototyping
public class LatexServiceTest {

  // @Test
  public void testReplacementFreemarker() throws TemplateNotFoundException,
      MalformedTemplateNameException, ParseException, IOException, TemplateException {

    // Configuration
    Configuration cfg = new Configuration(new Version(2, 3, 23));
    Path currentRelativePath = Paths.get("");
    String basicPath = currentRelativePath.toAbsolutePath()
      .toString();
    File templatePath = new File(basicPath + "/src/test/resources/data/latexExample/");
    cfg.setDirectoryForTemplateLoading(templatePath);
    cfg.setDefaultEncoding("UTF-8");
    cfg.setLocale(Locale.GERMAN);
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

    // Wrap all values: with <@latexEscape> and close them with <@/latexEscape>
    cfg.setSharedVariable("latexEscape", new LatexEscaper());

    // Read Template and escape elements
    Map<String, Object> input = new HashMap<String, Object>();
    Template template = cfg.getTemplate("ExampleTexTemplate.tex");

    // Write output to console and file
    Writer consoleWriter = new OutputStreamWriter(System.out);
    template.process(input, consoleWriter);
  }

  // TODO DKatzberg Delete? Old Prototype without Freemarker
  public void testReplacement() {

    // Tex template file
    String template = "Dies ist die Project Test ID: {{id}}";

    // Get Values from the Texfile
    Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
    Matcher matcher = pattern.matcher(template);
    String key = "";
    if (matcher.find()) {
      key = matcher.group(1);
    }

    // Got the key of the brackets. receive the value
    ExpressionParser parser = new SpelExpressionParser();
    DataAcquisitionProject acquisitionProject =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    StandardEvaluationContext projectContext = new StandardEvaluationContext(acquisitionProject);

    // Two example how to access the data / paths
    String id = parser.parseExpression(key)
      .getValue(projectContext, String.class);
    System.out.println("Id:" + id);
    String notesDe = parser.parseExpression("releases[0].notes.de")
      .getValue(projectContext, String.class);
    System.out.println("DE, Notes: " + notesDe);

    // Replace
    System.out.println("Original: " + template);
    String newTemplate = template.replaceAll("\\{\\{" + key + "\\}\\}", id);
    System.out.println("New: " + newTemplate);
  }

}
