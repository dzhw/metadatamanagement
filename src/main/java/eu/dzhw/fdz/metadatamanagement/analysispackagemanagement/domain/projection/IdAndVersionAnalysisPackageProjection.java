package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.projection;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * {@link IdAndVersionProjection} for {@link AnalysisPackage}s.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = AnalysisPackage.class)
public interface IdAndVersionAnalysisPackageProjection extends IdAndVersionProjection {

}
