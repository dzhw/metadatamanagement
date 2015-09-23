package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.AggregationAndHighlightingResultMapper;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import eu.dzhw.fdz.metadatamanagement.data.common.utils.PopulatorUtils;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.NoEditableId;

/**
 * The AbstractDocument is used for creating of the index for ElasticSearch. This is the abstract
 * document for all other types.
 * 
 * @author Daniel Katzberg
 * @see PopulatorUtils
 */
@Document(indexName = "#{'" + AbstractDocument.METADATA_INDEX + "_'"
    + "+T(org.springframework.context.i18n.LocaleContextHolder).getLocale().getLanguage()}")
@Setting(settingPath = "data/settings/settings.json")
public abstract class AbstractDocument {

  /**
   * The basic index name.
   */
  public static final String METADATA_INDEX = "metadata";
  public static final DocumentField ID_FIELD = new DocumentField("id");

  /**
   * A fdzID as primary key for the identification of the variable of a survey.
   */
  @Id
  @NoEditableId(groups = {Edit.class})
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  @Pattern(regexp = "^[0-9a-zA-Z_-]*", groups = {Create.class, Edit.class})
  private String id;
  
  /**
   * This map values are highlighted elements by elastic search. This maps will be set by the
   * Resultmapper ({@link AggregationAndHighlightingResultMapper}.
   */
  private Map<String, String> highlightedFields;
  
   
  /**
   * Basic Constructor.
   */
  public AbstractDocument() {
    this.highlightedFields = new HashMap<>();
  }
  
  /**
   * @param absolutePath The absolute path of a document field
   * @return if there is a highlight variant of the value of a document field.
   * @see DocumentField
   */
  public boolean isFieldHighlighted(String absolutePath) {
    return this.getHighlightedFields().containsKey(absolutePath);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", Arrays.deepToString(new Object[] {id}))
        .toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Arrays.deepHashCode(new Object[] {id});
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      AbstractDocument that = (AbstractDocument) object;
      return Arrays.deepEquals(new Object[] {this.id}, new Object[] {that.id});
    }
    return false;
  }

  /* GETTER / SETTER */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  public Map<String, String> getHighlightedFields() {
    return highlightedFields;
  }

  public void setHighlightedFields(Map<String, String> highlightedFields) {
    this.highlightedFields = highlightedFields;
  }
}
