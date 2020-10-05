package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;

/**
 * List of currently supported survey designs.
 */
public class SurveyDesigns {
  public static final I18nString CROSS_SECTION =
      new ImmutableI18nString("Querschnitt", "Cross-Section");
  public static final I18nString PANEL = new ImmutableI18nString("Panel", "Panel");
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList(CROSS_SECTION, PANEL)));
}
