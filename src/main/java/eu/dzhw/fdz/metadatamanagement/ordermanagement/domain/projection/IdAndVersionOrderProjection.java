package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import org.springframework.data.rest.core.config.Projection;

/**
 * {@link IdAndVersionProjection} for {@link Order}s.
 */
@Projection(name = "id-and-version", types = Order.class)
public interface IdAndVersionOrderProjection extends IdAndVersionProjection {
}
