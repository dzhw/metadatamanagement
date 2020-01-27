package eu.dzhw.fdz.metadatamanagement.searchmanagement.dao;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchBulkOperationException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIndexCreateException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIndexDeleteException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIoException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchPutMappingException;
import lombok.RequiredArgsConstructor;

/**
 * Data Access Object for accessing and manipulating elasticsearch.
 * 
 * @author René Reitmann
 */
@Component
@RequiredArgsConstructor
public class ElasticsearchDao {

  private final RestHighLevelClient client;

  /**
   * Create an index with the given settings as json.
   * 
   * @param index the name of the index
   * @param settings the settings json
   */
  public void createIndex(String index, String settings) {
    CreateIndexRequest request = new CreateIndexRequest(index);
    request.settings(settings, XContentType.JSON);
    try {
      client.indices().create(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchIndexCreateException(index, e);
    }
  }

  /**
   * Get the current settings for the given index.
   * 
   * @param index The name of the index.
   * @return the current settings json
   */
  public Settings getSettings(String index) {
    GetSettingsRequest request = new GetSettingsRequest().indices(index);
    GetSettingsResponse response;
    try {
      response = client.indices().getSettings(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
    return response.getIndexToSettings().get(index);
  }

  /**
   * Create or update the mapping for the given type.
   * 
   * @param index the name of the index holding the type
   * @param mapping the mapping json
   */
  public void putMapping(String index, String mapping) {
    PutMappingRequest request = new PutMappingRequest(index).source(mapping, XContentType.JSON);
    try {
      client.indices().putMapping(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchPutMappingException(index, e);
    }
  }

  /**
   * Get the mapping for the given type.
   * 
   * @param index the name of the index
   * @return the mapping json
   */
  public MappingMetaData getMapping(String index) {
    GetMappingsRequest request = new GetMappingsRequest().indices(index);
    GetMappingsResponse response;
    try {
      response = client.indices().getMapping(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
    return response.mappings().get(index);
  }

  /**
   * Check if the given index exists.
   * 
   * @param index the name of the index
   * @return true if the index exists.
   */
  public boolean exists(String index) {
    GetIndexRequest request = new GetIndexRequest(index);
    try {
      return client.indices().exists(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
  }

  /**
   * Refresh the given indices synchronously.
   * 
   * @param indices the indices to refresh.
   */
  public void refresh(String... indices) {
    RefreshRequest request = new RefreshRequest(indices);
    request.indicesOptions(IndicesOptions.fromOptions(true, true, false, false));
    try {
      client.indices().refresh(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
  }

  /**
   * Delete the given index and all of its documents.
   * 
   * @param index The name of the index to delete.
   */
  public void delete(String index) {
    DeleteIndexRequest request = new DeleteIndexRequest(index);
    try {
      client.indices().delete(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchIndexDeleteException(index, e);
    }
  }

  /**
   * Count all documents in all indices.
   * 
   * @return The number of all documents in all indices.
   */
  public long countAllDocuments() {
    CountRequest countRequest = new CountRequest();
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    countRequest.source(searchSourceBuilder);
    try {
      return client.count(countRequest, RequestOptions.DEFAULT).getCount();
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
  }

  /**
   * Execute a bulk of operations.
   * 
   * @param bulkRequest The bulk to be executed.
   */
  public void executeBulk(BulkRequest bulkRequest) {
    try {
      client.bulk(bulkRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchBulkOperationException(e);
    }
  }
}
