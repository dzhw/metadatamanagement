package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import com.google.common.base.Objects;

/**
 * This is a field representation of fields in elasticsearch documents. Fields can be nested fields
 * (e.g. variableSurvey.title).
 * 
 * @author Daniel Katzberg
 *
 */
public class DocumentField {

  public static final String PATH_DELIMITER = ".";

  /**
   * The absolute path of the field in the document Path (e.g. "variableSurvey.title"). This path is
   * used in elastic search queries, filters and aggregations.
   */
  private String absolutePath;

  /**
   * The relative path of a field (e.g. "title" if absolutePath is "variableSurvey.title").
   */
  private String relativePath;

  /**
   * The parent Field of this field if it is a nested field.
   */
  private DocumentField parent;

  /**
   * Create a Field with its absolute path.
   * 
   * @param absolutePath the complete path of this field (e.g. variableSurvey.title)
   */
  public DocumentField(String absolutePath) {
    this.absolutePath = absolutePath;
    int index = absolutePath.lastIndexOf(PATH_DELIMITER);

    // case if there is a parent
    if (index > 0) {
      this.relativePath = absolutePath.substring(index + 1);
      parent = new DocumentField(absolutePath.substring(0, index));
      // case if there is no parent
    } else {
      this.relativePath = absolutePath;
    }
  }

  /* GETTER / SETTER */
  public String getAbsolutePath() {
    return absolutePath;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public DocumentField getParent() {
    return parent;
  }

  public boolean isNested() {
    return parent != null;
  }

  /**
   * Equality is determined solely be the absolute path.
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(absolutePath);
  }

  /**
   * Equality is determined solely be the absolute path.
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof DocumentField) {
      DocumentField that = (DocumentField) object;
      return Objects.equal(this.absolutePath, that.absolutePath);
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "DocumentField [absolutePath=" + this.absolutePath + ", relativePath="
        + this.relativePath + ", parent=" + this.parent + "]";
  }
}
