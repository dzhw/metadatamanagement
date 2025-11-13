package eu.dzhw.fdz.metadatamanagement.searchmanagement.dao;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ExpandWildcard;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.IndexState;
import co.elastic.clients.elasticsearch.indices.RefreshRequest;
import co.elastic.clients.elasticsearch.indices.get_mapping.IndexMappingRecord;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ElasticsearchDao {

  private final ElasticsearchClient client;

  /**
   * Create an index with the given settings as json.
   *
   * @param index the name of the index
   * @param settings the settings json
   */
  public void createIndex(String index, String settings) {
    try {
      this.client.indices().create(r -> r
        .index(index)
        .settings(s -> s.withJson(new StringReader(settings)))
      );
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
  public Map<String, IndexState> getSettings(String index) {
    try {
      return this.client.indices().getSettings(r -> r.index(index)).result();
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
  }

  /**
   * Create or update the mapping for the given type.
   *
   * @param index the name of the index holding the type
   * @param mapping the mapping json
   */
  public void putMapping(String index, String mapping) {
    try {
      this.client.indices().putMapping(r -> r
        .index(index)
        .withJson(new StringReader(mapping))
      );
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
  public Map<String, IndexMappingRecord> getMapping(String index) {
    try {
      return this.client.indices().getMapping(r -> r.index(index)).result();
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
  }

  /**
   * Check if the given index exists.
   *
   * @param index the name of the index
   * @return true if the index exists.
   */
  public boolean exists(String index) {
    try {
      return this.client.indices().exists(r -> r.index(index)).value();
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
    try {
      this.client.indices().refresh(RefreshRequest.of(r -> r
        .index(List.of(indices))
        .ignoreUnavailable(true)
        .allowNoIndices(true)
        .expandWildcards(ExpandWildcard.None)
      ));
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
    try {
      this.client.indices().delete(DeleteIndexRequest.of(dir -> dir.index(index)));
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
    try {
      return this.client.count(CountRequest.of(r -> r
        .query(q -> q.matchAll(m -> m))
      )).count();
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
      final var response = this.client.bulk(bulkRequest);
      if (response.errors()) {
        log.error("Bulk request returned with errors:");
        response.items().forEach(item -> log.error(item.toString()));
      }
    } catch (IOException e) {
      throw new ElasticsearchBulkOperationException(e);
    }
  }
}
