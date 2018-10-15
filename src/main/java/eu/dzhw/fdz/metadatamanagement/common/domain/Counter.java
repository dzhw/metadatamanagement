package eu.dzhw.fdz.metadatamanagement.common.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Counter Document which can be used to get an incremented sequence number per document id.
 * 
 * @author René Reitmann
 */
@Document(collection = "counters")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Counter {
  @Id
  private String id;

  private long seq;
}
