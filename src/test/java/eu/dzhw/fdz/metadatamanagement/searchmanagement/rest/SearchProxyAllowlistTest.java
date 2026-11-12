package eu.dzhw.fdz.metadatamanagement.searchmanagement.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;

/**
 * Unit tests for the search proxy allowlist. These need neither Spring nor Elasticsearch.
 */
public class SearchProxyAllowlistTest {

  @ParameterizedTest
  @ValueSource(strings = {"/variables/_search", "/surveys/_count", "/data_packages/_msearch",
      "/_msearch", "/_mget", "/variables/_mget", "/variables,surveys/_search",
      "/data_acquisition_projects/_search",
      // the combined search the frontend fires after login, with url-encoded (%2C) commas
      "/data_packages%2Canalysis_packages%2Cvariables%2Csurveys%2Cdata_sets%2Cinstruments"
          + "%2Crelated_publications%2Cquestions%2Cconcepts/_search"})
  public void allowsReadOperationsOnKnownIndices(String path) {
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.POST, path)).isTrue();
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.GET, path)).isTrue();
  }

  @Test
  public void allowsGetSourceById() {
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.GET, "/variables/_source/var-1")).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"/_cat/indices", "/_cluster/health", "/_cluster/settings", "/_nodes",
      "/_all/_search", "/_search", "/variables", "/variables/_doc/var-1",
      "/variables/_delete_by_query", "/unknown_index/_search", "/variables,secrets/_search",
      "/variables/_source", "/variables/_mapping", "/.kibana/_search"})
  public void rejectsAdministrativeOrUnknownPaths(String path) {
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.GET, path)).isFalse();
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.POST, path)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"/variables", "/variables/_search", "/variables/_delete_by_query"})
  public void rejectsWriteMethods(String path) {
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.DELETE, path)).isFalse();
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.PUT, path)).isFalse();
  }

  @Test
  public void getSourceIsReadOnly() {
    // _source must not be reachable with a write method
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.POST, "/variables/_source/var-1"))
        .isFalse();
  }

  @Test
  public void handlesNullAndEmpty() {
    assertThat(SearchProxyAllowlist.isAllowed(null, "/variables/_search")).isFalse();
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.GET, null)).isFalse();
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.GET, "/")).isFalse();
    assertThat(SearchProxyAllowlist.isAllowed(HttpMethod.GET, "")).isFalse();
  }
}
