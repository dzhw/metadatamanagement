package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Order (DTO) containing all relevant information of a customer and her data products.
 * 
 * @author René Reitmann
 */
@Document(collection = "orders")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Order extends AbstractRdcDomainObject {
  @Id
  private String id;

  @NotEmpty
  private String languageKey;

  @Indexed
  private OrderState state;

  @Valid
  @NotNull
  private Customer customer;

  @Valid
  @NotEmpty
  private List<Product> products;
}
