package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * The DataType for Survey enumeration.
 * @author Daniel Katzberg
 */
public class DataTypes {

  //Valid Study Data Types
  public static final I18nString QUANTITATIVE_DATA = 
      new I18nString("Quantitative Daten", "Quantitative Data");
  public static final I18nString QUALITATIVE_DATA = 
      new I18nString("Qualitative Daten", "Qualitative Data");
  
  //Just for Study.
  public static final I18nString MIXED_METHODS = 
      new I18nString("Mixed Methods", "Mixed Methods");
  
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<>(
          Arrays.asList(QUANTITATIVE_DATA, QUALITATIVE_DATA, MIXED_METHODS)));
  
  public static final Set<I18nString> VALID_SURVEY_DATA_TYPES =
      Collections.unmodifiableSet(new HashSet<>(
          Arrays.asList(QUANTITATIVE_DATA, QUALITATIVE_DATA)));
}
