package eu.dzhw.fdz.metadatamanagement.searchmanagement.rest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

import org.springframework.http.HttpMethod;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;

/**
 * Decides which requests the {@link SearchResource} search proxy is allowed to forward to
 * Elasticsearch.
 *
 * <p>The proxy forwards requests with the server's own Elasticsearch credentials, so without this
 * allowlist any client (anonymous ones included, because {@code GET /api/search/**} bypasses the
 * security filters) could reach administrative or destructive Elasticsearch APIs such as
 * {@code _cat/indices}, {@code _cluster/*}, {@code _nodes}, {@code DELETE /<index>} or
 * {@code _delete_by_query}.</p>
 *
 * <p>Only the read operations the frontend actually performs are permitted:</p>
 * <ul>
 * <li>{@code GET|POST /<indices>/_search}</li>
 * <li>{@code GET|POST /<indices>/_count}</li>
 * <li>{@code GET|POST /_msearch} and {@code GET|POST /<indices>/_msearch}</li>
 * <li>{@code GET /<index>/_source/<id>}</li>
 * </ul>
 *
 * <p>where every {@code <index>} segment is a (comma separated list of) known
 * {@link ElasticsearchType}. Everything else is rejected.</p>
 */
final class SearchProxyAllowlist {

  private static final Set<String> ALLOWED_INDICES = Set.of(ElasticsearchType.names());

  private static final Set<String> READ_ENDPOINTS =
      Set.of("_search", "_count", "_msearch", "_mget");

  /** Read endpoints that may be called at the root (indices are carried in the request body). */
  private static final Set<String> ROOT_ENDPOINTS = Set.of("_msearch", "_mget");

  private SearchProxyAllowlist() {}

  /**
   * Check whether the proxy may forward the given request to Elasticsearch.
   *
   * @param method the HTTP method of the request
   * @param path the request path with the {@code /api/search} prefix already removed (query string
   *        excluded), e.g. {@code /variables/_search}
   * @return {@code true} if the request is a permitted read operation on a known index
   */
  static boolean isAllowed(HttpMethod method, String path) {
    if (method == null || path == null) {
      return false;
    }
    if (!HttpMethod.GET.equals(method) && !HttpMethod.POST.equals(method)) {
      return false;
    }
    // The frontend joins multiple indices with a comma and url-encodes it (%2C), so decode before
    // parsing. '/' (%2F) inside a segment is already rejected by Spring's StrictHttpFirewall.
    String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
    String[] segments = Arrays.stream(decodedPath.split("/")).filter(segment -> !segment.isEmpty())
        .toArray(String[]::new);
    if (segments.length == 0) {
      return false;
    }
    // GET /<index>/_source/<id>
    if (segments.length == 3 && "_source".equals(segments[1])) {
      return HttpMethod.GET.equals(method) && isIndexList(segments[0]);
    }
    String last = segments[segments.length - 1];
    if (!READ_ENDPOINTS.contains(last)) {
      return false;
    }
    // POST /_msearch or /_mget (indices are carried in the request body)
    if (segments.length == 1) {
      return ROOT_ENDPOINTS.contains(last);
    }
    // /<indices>/_search|_count|_msearch
    return segments.length == 2 && isIndexList(segments[0]);
  }

  private static boolean isIndexList(String indices) {
    String[] parts = indices.split(",");
    return parts.length > 0 && Arrays.stream(parts).allMatch(ALLOWED_INDICES::contains);
  }
}
