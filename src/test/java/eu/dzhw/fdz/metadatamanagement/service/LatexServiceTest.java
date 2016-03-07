/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestCreateDomainObjectUtils;

/**
 * @author Daniel Katzberg
 *
 */
// TODO DKatzberg
public class LatexServiceTest {

  // @Test
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
