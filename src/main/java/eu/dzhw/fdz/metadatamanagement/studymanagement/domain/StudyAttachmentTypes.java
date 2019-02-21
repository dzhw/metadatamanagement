package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cglib.beans.ImmutableBean;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * All valid types of a {@link StudyAttachmentMetadata}.
 */
public class StudyAttachmentTypes {
  public static final I18nString METHOD_REPORT = (I18nString) ImmutableBean
      .create(new I18nString("Daten- und Methodenbericht", "Method Report"));
  public static final I18nString OTHER =
      (I18nString) ImmutableBean.create(new I18nString("Sonstiges", "Other"));

  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<I18nString>(Arrays.asList(METHOD_REPORT, OTHER)));
}
