package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;

/**
 * {@link IdAndVersionProjection} for {@link Study}s.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = Study.class)
public interface IdAndVersionStudyProjection extends IdAndVersionProjection {

}
