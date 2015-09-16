package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * This is a field representation of fields in elasticsearch documents. Fields can be organized as
 * chains meaning the can have one subfield.
 * 
 * @author Daniel Katzberg
 *
 */
public class Field implements Cloneable {

  /*
   * A slf4j logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(Field.class);

  /**
   * The path of the field in the document Path (e.g. "variableSurvey.title"). This path is used in
   * elastic search queries, filters and aggregations
   */
  private String path;

  /**
   * A sub field to support recursively any depth of subfields.
   */
  private Field subField;

  /**
   * This is the constructor for a field without subfields. It has only the field path.
   * 
   * @param path the complete path of this field (e.g. variableSurvey.title)
   */
  public Field(String path) {
    this(path, null);
  }

  /**
   * This is the constructor for field with a subfield. It has a field path and a sub field.
   * 
   * @param path the complete path of this field (e.g. variableSurvey.title)
   * @param subField the sub field.
   */
  public Field(String path, Field subField) {
    this.path = path;
    this.subField = subField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#clone()
   */
  @Override
  public Field clone() {
    try {
      Field clone = (Field) super.clone();
      clone.path = this.path;
      if (this.hasSubfield()) {
        clone.subField = this.subField.clone();
      }
      return clone;
    } catch (CloneNotSupportedException e) {
      LOG.error("Cannot clone Field (" + this.path + ")", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * @param subField A nested field.
   * @return Fluent Filter for easy readable extensions of the given field.
   */
  public Field withSubField(Field subField) {
    this.subField = subField;
    return this;
  }

  /**
   * Go to the current leaf subfield and add a new subfield with the given path attached to the
   * complete path.
   * 
   * @param path of the subfield.
   * @return this
   */
  public Field withNewLeafSubField(String path) {
    if (this.hasSubfield()) {
      this.subField.withNewLeafSubField(path);
    } else {
      this.subField = new Field(this.path + "." + path);
    }
    return this;
  }

  /**
   * Return the path of the last subfield (which has no subfields).
   * 
   * @return the path of the last subfield (which has no subfields).
   */
  public String getLeafSubFieldPath() {
    if (this.hasSubfield()) {
      return this.subField.getLeafSubFieldPath();
    } else {
      return path;
    }
  }

  /**
   * Return the last subfield (which has no subfields).
   * 
   * @return the last subfield (which has no subfields).
   */
  public Field getLeafSubField() {
    if (this.hasSubfield()) {
      return this.subField.getLeafSubField();
    } else {
      return this;
    }
  }

  /**
   * @return Returns true if there is a nested Field in this field.
   */
  public boolean hasSubfield() {
    return this.subField != null;
  }

  /* GETTER / SETTER */
  public String getPath() {
    return path;
  }

  public Field getSubField() {
    return subField;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(path, subField);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof Field) {
      Field that = (Field) object;
      return Objects.equal(this.path, that.path) && Objects.equal(this.subField, that.subField);
    }
    return false;
  }
}
