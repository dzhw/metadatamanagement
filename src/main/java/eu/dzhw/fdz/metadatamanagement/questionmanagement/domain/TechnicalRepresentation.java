package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import javax.validation.constraints.Size;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Technical Representation of a Question. It contains the type, 
 * the language (e.g. XML) and the Source itself.
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
     intoPackage = "eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.builders")
public class TechnicalRepresentation {
  
  @Size(max = StringLengths.SMALL, 
      message = "question-management.error.technical-representation.type.size")
  private String type;
  
  @Size(max = StringLengths.SMALL, 
      message = "question-management.error.technical-representation.language.size")
  private String language;
  
  @Size(max = StringLengths.X_LARGE, 
      message = "question-management.error.technical-representation.source.size")
  private String source;

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("type", type)
      .add("language", language)
      .add("source", source)
      .toString();
  }

  /* GETTER / SETTER */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}
