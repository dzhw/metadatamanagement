package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cglib.beans.ImmutableBean;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * The technical type which the {@link ValidResponse}s have.
 */
public class DataTypes {

  public static final I18nString STRING =
      (I18nString) ImmutableBean.create(new I18nString("string", "string"));
  public static final I18nString NUMERIC =
      (I18nString) ImmutableBean.create(new I18nString("numerisch", "numeric"));
  public static final I18nString DATE =
      (I18nString) ImmutableBean.create(new I18nString("datum", "date"));
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<I18nString>(Arrays.asList(STRING, NUMERIC, DATE)));
}
