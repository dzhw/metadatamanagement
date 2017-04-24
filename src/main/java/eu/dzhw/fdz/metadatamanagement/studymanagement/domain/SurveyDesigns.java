package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Survey Designs for the Scale Level field Survey Designs. Only the values in this class are 
 * allowed and will be checked by a validator.
 * 
 * @author Daniel Katzberg
 *
 */
public class SurveyDesigns {
  public static final I18nString CROSS_SECTION = new I18nString("Querschnitt", "Cross-Section");
  public static final I18nString PANEL = new I18nString("Panel", "Panel");
  public static final Set<I18nString> ALL = Collections
      .unmodifiableSet(new HashSet<>(Arrays.asList(CROSS_SECTION, PANEL)));
}
