package eu.dzhw.fdz.metadatamanagement.common.domain;

/**
 * Immutable (constant) version of {@link I18nString}s.
 */
public class ImmutableI18nString extends I18nString {

  private static final long serialVersionUID = -348100501159179531L;

  public ImmutableI18nString() {
    super();
  }

  public ImmutableI18nString(String de, String en) {
    super(de, en);
  }

  @Override
  public void setDe(String de) {
    throw new IllegalAccessError("ImmutableI18nString must not be changed!");
  }

  @Override
  public void setEn(String en) {
    throw new IllegalAccessError("ImmutableI18nString must not be changed!");
  }
}
