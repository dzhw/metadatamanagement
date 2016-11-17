package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * All valid types of an instrument attachment.
 */
public class InstrumentAttachmentTypes {
  public static final I18nString QUESTIONNAIRE = 
      new I18nString("Fragebogen", "Questionnaire");
  public static final I18nString QUESTION_FLOW = 
      new I18nString("Filterdiagramm", "Question Flow");
  public static final I18nString VARIABLE_QUESTIONNAIRE =
      new I18nString("Variablenfragebogen", "Variable Questionnaire");
  public static final I18nString OTHER = 
      new I18nString("Sonstige", "Other");
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(
          new HashSet<I18nString>(Arrays.asList(QUESTIONNAIRE, QUESTION_FLOW, OTHER)));
}
