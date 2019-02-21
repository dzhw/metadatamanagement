package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cglib.beans.ImmutableBean;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * All possible formats of a {@link DataSet}.
 */
public class Format {

  public static final I18nString WIDE =
      (I18nString) ImmutableBean.create(new I18nString("breit", "wide"));
  public static final I18nString LONG =
      (I18nString) ImmutableBean.create(new I18nString("lang", "long"));
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList(WIDE, LONG)));

}
