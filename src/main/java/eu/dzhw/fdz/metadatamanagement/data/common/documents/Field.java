package eu.dzhw.fdz.metadatamanagement.data.common.documents;

/**
 * This is a field representation of nested and not nested fields within filters and aggregations.
 * @author Daniel Katzberg
 *
 */
public class Field {
  
  private String path;
  
  private Field nestedField;
  
  /**
   * This is the constructor for a not nested field. It has only the field path.
   * @param path the name of a filter / aggregation field
   */
  public Field(String path) {
    this(path, null);
  }
  
  /**
   * This is the constructor for a nested field. It has a field path and a nested field.
   * @param path the name of a filter / aggregation field (basic path)
   * @param nestedField the nested field of a filter / aggregation.
   */
  public Field(String path, Field nestedField) {
    this.path = path;
    this.nestedField = nestedField;
  }
  
  /**
   * @return Returns true if there is a nested Field in this field.
   */
  public boolean isNested() {
    return this.nestedField != null;
  }
  
  /* GETTER / SETTER */
  public String getPath() {
    return path;
  }

  public Field getNestedField() {
    return nestedField;
  }
}
