package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

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
  public static final String STATA = "Stata";
  public static final String SPEL = "SpEL";
  public static final Set<String> ALL = Collections.unmodifiableSet(
      new HashSet<String>(Arrays.asList(STATA, SPEL)));
}
