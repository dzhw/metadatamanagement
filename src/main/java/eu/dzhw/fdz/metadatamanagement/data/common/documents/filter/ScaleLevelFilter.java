package eu.dzhw.fdz.metadatamanagement.data.common.documents.filter;

/**
 * The scale level filter represents a scale level button / checkbox at the web layer. It holds the
 * status and the value of the filter.
 * 
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelFilter extends AbstractFilter<String> {
  
  public ScaleLevelFilter() {
    super();
  }
  
  public ScaleLevelFilter(boolean choosen, String value, long docCount, String name) {
    super(choosen, value, docCount, name);
  }

}
