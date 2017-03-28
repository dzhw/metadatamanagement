package eu.dzhw.fdz.metadatamanagement.common.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Minimal projection which can be used when we only need the id of a
 * domain object.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = {Survey.class, Study.class, 
    Instrument.class, Question.class, DataSet.class, Variable.class})
public interface IdAndVersionProjection {
  String getId();
  
  Long getVersion();
}
