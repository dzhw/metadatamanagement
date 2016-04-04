package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Access Ways.
 * 
 * @author Daniel Katzberg
 *
 */
public class AccessWays {

  public static final String CUF = "cuf";
  public static final String SUF = "suf";
  public static final String REMOTE = "remote";
  public static final Set<String> ALL =
      Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(CUF, SUF, REMOTE)));
}
