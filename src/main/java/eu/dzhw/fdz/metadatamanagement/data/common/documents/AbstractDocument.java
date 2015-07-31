package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import org.springframework.data.elasticsearch.annotations.Document;

import eu.dzhw.fdz.metadatamanagement.data.common.utils.PopulatorUtils;

/**
 * The AbstractDocument is used for creating of the index for ElasticSearch. This is the abstract
 * document for all other types.
 * 
 * @author Daniel Katzberg
 * @see PopulatorUtils
 */
@Document(indexName = "#{'" + AbstractDocument.METADATA_INDEX + "_'"
    + "+T(org.springframework.context.i18n.LocaleContextHolder).getLocale().getLanguage()}")
public class AbstractDocument {
  
  /**
   * The basic index name.
   */
  public static final String METADATA_INDEX = "metadata";
  
}
