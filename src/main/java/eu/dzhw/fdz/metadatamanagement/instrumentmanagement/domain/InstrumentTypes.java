package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * All valid types of an instrument.
 */
public class InstrumentTypes {
  public static final String PAPI = "PAPI";
  public static final String CAPI = "CAPI";
  public static final String CATI = "CATI";
  public static final String CAWI = "CAWI";
  public static final Set<String> ALL =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList(PAPI, CAPI, CATI, CAWI)));
}
