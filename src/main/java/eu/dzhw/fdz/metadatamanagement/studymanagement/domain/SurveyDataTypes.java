package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

import java.util.Set;

/**
 * List of types of data, which a {@link Study} can consist of. It will be computed from the
 * {@link Survey}s of a {@link Study}.
 */
public class SurveyDataTypes extends DataTypes {

  public static final I18nString MIXED_METHODS =
      new ImmutableI18nString("Mixed Methods", "Mixed Methods");

  public static final Set<I18nString> ALL = Set.of(QUANTITATIVE_DATA, QUALITATIVE_DATA,
      MIXED_METHODS);
}
