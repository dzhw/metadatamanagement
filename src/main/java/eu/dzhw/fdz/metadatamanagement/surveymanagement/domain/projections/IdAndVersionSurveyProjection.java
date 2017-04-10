package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * {@link IdAndVersionProjection} for {@link Survey}s.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = Survey.class)
public interface IdAndVersionSurveyProjection extends IdAndVersionProjection {

}
