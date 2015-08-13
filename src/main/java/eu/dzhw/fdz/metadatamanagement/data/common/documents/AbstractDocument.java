package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import eu.dzhw.fdz.metadatamanagement.data.common.PopulatorUtils;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.UniqueId;

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
public class AbstractDocument {

  /**
   * The basic index name.
   */
  public static final String METADATA_INDEX = "metadata";
  public static final String ID_FIELD = "id";

  /**
   * A fdzID as primary key for the identification of the variable of a survey.
   */
  @Id
  @UniqueId
  @Size(max = 32)
  @NotBlank
  @Pattern(regexp = "^[0-9a-zA-Z_-]*")
  private String id;

  /* GETTER / SETTER */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
