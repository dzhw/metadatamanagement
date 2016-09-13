package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;

/**
 * The 'referenced' Projection of a study domain object.
 * 'referenced' means only some attributes will be
 * displayed.
 */
@Projection(name = "referenced", types = Study.class)
public interface ReferencedStudyProjection {
  String getDataAcquisitionProjectId();
  
  String getAuthors();
  
  I18nString getTitle();
}
