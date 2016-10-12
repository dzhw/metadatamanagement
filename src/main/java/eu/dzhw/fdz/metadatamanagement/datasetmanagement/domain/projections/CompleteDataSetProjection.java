package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;

/**
 * The 'complete' Projection of a data set domain object. 'complete' means all attributes will be
 * displayed.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = DataSet.class)
public interface CompleteDataSetProjection
    extends AbstractRdcDomainObjectProjection {

  I18nString getDescription();

  String getDataAcquisitionProjectId();

  List<String> getVariableIds();
  
  List<String> getSurveyIds();
  
  List<SubDataSet> getSubDataSets();
  
  I18nString getType();
  
}
