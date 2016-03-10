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

import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestCreateDomainObjectUtils;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

/**
 * @author Daniel Katzberg
 *
 */
// TODO DKatzberg Prototyping
public class LatexServiceTest {

  // @Test
  public void testReplacementFreemarker() throws TemplateNotFoundException,
      MalformedTemplateNameException, ParseException, IOException, TemplateException {

    Map<String, Object> input = new HashMap<String, Object>();
    DataAcquisitionProject dataAcquisitionProject =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProject.setId("{Another \\ _ ~ # $ % ^ & id}");
    input.put("dataAcquisitionProject", dataAcquisitionProject);

    // Configuration
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
    Path currentRelativePath = Paths.get("");
    String basicPath = currentRelativePath.toAbsolutePath()
      .toString();
    File templatePath = new File(basicPath + "/src/test/resources/data/latexExample/");
    cfg.setDirectoryForTemplateLoading(templatePath);
    cfg.setDefaultEncoding("UTF-8");
    cfg.setLocale(Locale.GERMAN);
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setTemplateLoader(new LatexTemplateLoader(cfg.getTemplateLoader()));


    // Read Template and escape elements
    Template template = cfg.getTemplate("ExampleTexTemplate.tex");

    // Write output to console and file
    Writer consoleWriter = new OutputStreamWriter(System.out);
    template.process(input, consoleWriter);
  }
}
