package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Component helps parsing markdown strings.
 * 
 * @author René Reitmann
 */
@Component
public class MarkdownHelper {

  private final HtmlRenderer htmlRenderer;
  private final Parser parser;

  public MarkdownHelper() {
    MutableDataSet options = new MutableDataSet();
    options.set(Parser.EXTENSIONS, Arrays.asList(StrikethroughExtension.create()));
    htmlRenderer = HtmlRenderer.builder(options).build();
    parser = Parser.builder(options).build();
  }
  /**
   * Remove markdown markup from the given string.
   * 
   * @param markdown A string containing markdown markup.
   * @return The String without markdown markup.
   */
  public String getPlainText(String markdown) {
    String html = htmlRenderer.render(parser.parse(markdown));
    return Jsoup.parse(html).wholeText().trim();
  }

  /**
   * Create a freemarker method to be used in freemarker templates to remove markdown from the
   * domain model.
   * 
   * @return The {@link TemplateMethodModelEx} which is a freemarker method.
   */
  public TemplateMethodModelEx createRemoveMarkdownMethod() {
    return new TemplateMethodModelEx() {
      @SuppressWarnings("rawtypes")
      @Override
      public TemplateModel exec(List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
          throw new TemplateModelException("Wrong arguments");
        }
        return new SimpleScalar(getPlainText(((SimpleScalar) arguments.get(0)).getAsString()));
      }
    };
  }
}
