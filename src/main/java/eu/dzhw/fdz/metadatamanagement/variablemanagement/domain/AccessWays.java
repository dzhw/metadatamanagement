package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;

/**
 * An access way of a {@link Variable} or a {@link DataSet} indicates how the data user will be able
 * to work with the data.
 */
public class AccessWays {

  public static final String DOWNLOAD_CUF = "download-cuf";
  public static final String DOWNLOAD_SUF = "download-suf";
  public static final String REMOTE_DESKTOP = "remote-desktop-suf";
  public static final String ONSITE_SUF = "onsite-suf";
  public static final String NOT_ACCESSIBLE = "not-accessible";
  
  public static final List<String> ALL =
      Collections.unmodifiableList(Arrays.asList(DOWNLOAD_CUF, DOWNLOAD_SUF, 
          REMOTE_DESKTOP, ONSITE_SUF, NOT_ACCESSIBLE));
}
