package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * List of currently supported survey designs.
 */
public class SurveyDesigns {
  public static final I18nString CROSS_SECTION = new I18nString("Querschnitt", "Cross-Section");
  public static final I18nString PANEL = new I18nString("Panel", "Panel");
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList(CROSS_SECTION, PANEL)));
}
