package eu.dzhw.fdz.metadatamanagement.domain;

import java.io.Serializable;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Class representing Strings that can be internationalized.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class I18nString implements Serializable {
  private static final long serialVersionUID = 3035702867033098482L;

  private String de;

  private String en;

  /**
   * Default Constructor.
   */
  public I18nString() {}

  /**
   * A constructor with the possibility to set the german and english translation.
   * 
   * @param de German translation
   * @param en English translation.
   */
  public I18nString(String de, String en) {
    this.de = de;
    this.en = en;
  }

  /**
   * German version of the string.
   * 
   * @return the german version of the string.
   */
  public String getDe() {
    return de;
  }

  public void setDe(String de) {
    this.de = de;
  }

  /**
   * English version of the string.
   * 
   * @return the english version of the string.
   */
  public String getEn() {
    return en;
  }

  public void setEn(String en) {
    this.en = en;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((de == null) ? 0 : de.hashCode());
    result = prime * result + ((en == null) ? 0 : en.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    I18nString other = (I18nString) obj;
    if (de == null) {
      if (other.de != null) {
        return false;
      }
    } else if (!de.equals(other.de)) {
      return false;
    }
    if (en == null) {
      if (other.en != null) {
        return false;
      }
    } else if (!en.equals(other.en)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "I18nString{de='" + de + "', en='" + en + "'}";
  }
}
