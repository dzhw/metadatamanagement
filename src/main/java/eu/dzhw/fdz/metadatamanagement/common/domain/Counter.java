package eu.dzhw.fdz.metadatamanagement.common.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Counter document which can be used to get an incremented sequence number per document id.
 */
@Document(collection = "counters")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Counter {
  /**
   * The id of the counter, e.g. "orders".
   */
  @Id
  private String id;

  /**
   * The current sequence number.
   */
  private long seq;
}
