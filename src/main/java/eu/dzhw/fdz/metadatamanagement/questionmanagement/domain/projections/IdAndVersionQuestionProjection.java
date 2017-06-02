package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;

/**
 * {@link IdAndVersionProjection} for {@link Question}s.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = Question.class)
public interface IdAndVersionQuestionProjection extends IdAndVersionProjection {

}
