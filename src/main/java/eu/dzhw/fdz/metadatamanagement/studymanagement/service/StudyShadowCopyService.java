package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.helper.StudyShadowCopyDataSource;

/**
 * Service which generates shadow copies of all studies of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class StudyShadowCopyService extends ShadowCopyHelper<Study> {
  public StudyShadowCopyService(StudyShadowCopyDataSource studyShadowCopyDataSource) {
    super(studyShadowCopyDataSource);
  }
}

