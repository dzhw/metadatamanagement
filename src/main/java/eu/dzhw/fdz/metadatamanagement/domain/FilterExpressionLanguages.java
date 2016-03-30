package eu.dzhw.fdz.metadatamanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter Expression Languages.
 * 
 * @author Daniel Katzberg
 *
 */
public class FilterExpressionLanguages {
  public static final String STATA = "stata";
  public static final String SPEL = "spel";
  public static final Set<String> ALL = Collections.unmodifiableSet(
      new HashSet<String>(Arrays.asList(STATA, SPEL)));
}
