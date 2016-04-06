package eu.dzhw.fdz.metadatamanagement.common.domain.projections;

/**
 * The extended version of a abstract rdc domain object with a survey and projection id.
 * 
 * @author Daniel Katzberg
 *
 */
public interface AbstractRdcDomainObjectWithProjectSurveyProjection
    extends AbstractRdcDomainObjectProjection {

  String getDataAcquisitionProjectId();

  String getSurveyId();

}
