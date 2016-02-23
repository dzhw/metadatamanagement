package eu.dzhw.fdz.metadatamanagement.domain.projections;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.domain.I18nString;

/**
 * The 'complete' Projection of a data set domain object. 'complete' means all attributes will be
 * displayed.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = DataSet.class)
public interface CompleteDataSetProjection
    extends AbstractRdcDomainObjectWithProjectSurveyProjection {

  I18nString getDescription();

  List<String> getVariableIds();

}
