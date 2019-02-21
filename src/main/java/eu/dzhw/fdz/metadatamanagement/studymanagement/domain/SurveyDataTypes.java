package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cglib.beans.ImmutableBean;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * List of types of data, which a {@link Study} can consist of. It will be computed from the
 * {@link Survey}s of a {@link Study}.
 */
public class SurveyDataTypes extends DataTypes {

  public static final I18nString MIXED_METHODS = 
      (I18nString) ImmutableBean.create(new I18nString("Mixed Methods", "Mixed Methods"));
  
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<>(
          Arrays.asList(QUANTITATIVE_DATA, QUALITATIVE_DATA, MIXED_METHODS)));
}
