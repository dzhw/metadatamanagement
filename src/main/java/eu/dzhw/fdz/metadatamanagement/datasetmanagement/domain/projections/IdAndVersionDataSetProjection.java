package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;

/**
 * {@link IdAndVersionProjection} for {@link DataSet}s.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = DataSet.class)
public interface IdAndVersionDataSetProjection extends IdAndVersionProjection {
  
}

