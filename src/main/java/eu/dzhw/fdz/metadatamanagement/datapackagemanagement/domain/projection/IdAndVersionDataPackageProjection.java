package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.projection;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;

/**
 * {@link IdAndVersionProjection} for {@link DataPackage}s.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = DataPackage.class)
public interface IdAndVersionDataPackageProjection extends IdAndVersionProjection {

}
