package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;

/**
 * This is a list of Survey Data Types, which are displayed on a Study Detail Page.
 * @author Daniel Katzberg
 *
 */
public class SurveyDataTypes extends DataTypes {

  public static final I18nString MIXED_METHODS = 
      new I18nString("Mixed Methods", "Mixed Methods");
  
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<>(
          Arrays.asList(QUANTITATIVE_DATA, QUALITATIVE_DATA, MIXED_METHODS)));
}
