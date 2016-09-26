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

  public static final String DOWNLOAD_CUF = "download-cuf";
  public static final String DOWNLOAD_SUF = "download-suf";
  public static final String REMOTE_DESKTOP = "remote-desktop-suf";
  public static final String ONSITE_SUF = "onsite-suf";
  public static final String NOT_ACCESSIBLE = "not-accessible";
  
  public static final Set<String> ALL =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList(DOWNLOAD_CUF, DOWNLOAD_SUF, 
          REMOTE_DESKTOP, ONSITE_SUF, NOT_ACCESSIBLE)));
}
