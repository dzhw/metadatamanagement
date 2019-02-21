package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cglib.beans.ImmutableBean;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Types of data, which a {@link Survey} can produce.
 */
public class DataTypes {

  // Valid Study Data Types
  public static final I18nString QUANTITATIVE_DATA =
      (I18nString) ImmutableBean.create(new I18nString("Quantitative Daten", "Quantitative Data"));
  public static final I18nString QUALITATIVE_DATA =
      (I18nString) ImmutableBean.create(new I18nString("Qualitative Daten", "Qualitative Data"));

  public static final Set<I18nString> ALL = Collections
      .unmodifiableSet(new HashSet<>(Arrays.asList(QUANTITATIVE_DATA, QUALITATIVE_DATA)));
}
