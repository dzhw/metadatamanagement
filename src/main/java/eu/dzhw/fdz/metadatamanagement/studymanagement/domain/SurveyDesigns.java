package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cglib.beans.ImmutableBean;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * List of currently supported survey designs.
 */
public class SurveyDesigns {
  public static final I18nString CROSS_SECTION =
      (I18nString) ImmutableBean.create(new I18nString("Querschnitt", "Cross-Section"));
  public static final I18nString PANEL =
      (I18nString) ImmutableBean.create(new I18nString("Panel", "Panel"));
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList(CROSS_SECTION, PANEL)));
}
