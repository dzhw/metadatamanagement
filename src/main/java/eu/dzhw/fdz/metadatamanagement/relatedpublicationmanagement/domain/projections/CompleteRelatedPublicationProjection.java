package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * The complete projection of a related publication domain object. It return all fields.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = RelatedPublication.class)
interface CompleteRelatedPublicationProjection extends AbstractRdcDomainObjectProjection {

  String getSourceReference();

  String getPublicationAbstract();

  String getDoi();

  String getSourceLink();

  String getTitle();
  
  Integer getYear();
  
  String getAuthors();
  
  I18nString getAbstractSource();

  List<String> getQuestionIds();

  List<String> getSurveyIds();

  List<String> getVariableIds();

  List<String> getDataSetIds();

  List<String> getStudyIds();

  List<String> getInstrumentIds();
  
}
