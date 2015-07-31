package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import eu.dzhw.fdz.metadatamanagement.data.common.populator.PopulatorUtils;

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
  
}
