package eu.dzhw.fdz.metadatamanagement.service.reporter;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import freemarker.template.TemplateTransformModel;

/**
 * This class is a Latex escaper. It escapes special Latex signs from the given string for an input
 * into a latex templates, so a correct compilation by Latex is successful.
 * <p>
 * </p>
 * The list of special signs is from the page:
 * http://www.cespedes.org/blog/85/how-to-escape-latex-special-characters
 * 
 * @author Daniel Katzberg
 *
 */
public class LatexEscaper implements TemplateTransformModel {

  // TODO DKatzberg: Prototype phase, only 2 special signs for testing
  private static final char[] CURLY_BRACKET_OPEN = "\\{".toCharArray();
  private static final char[] CURLY_BRACKET_CLOSE = "\\}".toCharArray();

  /*
   * (non-Javadoc)
   * 
   * @see freemarker.template.TemplateTransformModel#getWriter(java.io.Writer, java.util.Map)
   */
  @Override
  public Writer getWriter(final Writer writer, @SuppressWarnings("rawtypes") Map args) {
    return new Writer() {

      /*
       * (non-Javadoc)
       * 
       * @see java.io.Writer#write(int)
       */
      @Override
      public void write(int charInt) throws IOException {

        // check the signs
        switch (charInt) {
          case '{':
            writer.write(CURLY_BRACKET_OPEN, 0, 2);
            break;
          case '}':
            writer.write(CURLY_BRACKET_CLOSE, 0, 2);
            break;
          default:
            writer.write(charInt);
        }
      }

      /*
       * (non-Javadoc)
       * 
       * @see java.io.Writer#write(char[], int, int)
       */
      @Override
      public void write(char[] charBuffer, int offset, int length) throws IOException {
        int lastOffset = offset;
        int lastPosition = offset + length;

        // iterate all signs
        for (int i = offset; i < lastPosition; i++) {
          switch (charBuffer[i]) {
            case '{':
              writer.write(charBuffer, lastOffset, i - lastOffset);
              writer.write(CURLY_BRACKET_OPEN, 0, 2);
              lastOffset = i + 1;
              break;
            case '}':
              writer.write(charBuffer, lastOffset, i - lastOffset);
              writer.write(CURLY_BRACKET_CLOSE, 0, 2);
              lastOffset = i + 1;
              break;
            default:
              break;
          }
        }

        // check by recursion the non checked signs
        int remaining = lastPosition - lastOffset;
        if (remaining > 0) {
          writer.write(charBuffer, lastOffset, remaining);
        }
      }

      /*
       * (non-Javadoc)
       * 
       * @see java.io.Writer#flush()
       */
      @Override
      public void flush() throws IOException {
        writer.flush();
      }

      /*
       * (non-Javadoc)
       * 
       * @see java.io.Writer#close()
       */
      @Override
      public void close() throws IOException {
        writer.close();
      }
    };
  }
}
