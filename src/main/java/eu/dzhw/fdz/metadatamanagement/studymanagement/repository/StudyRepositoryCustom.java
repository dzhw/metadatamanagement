package eu.dzhw.fdz.metadatamanagement.studymanagement.repository;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Interface for custom repository methods.
 * 
 * @author René Reitmann
 */
public interface StudyRepositoryCustom {
  List<I18nString> findAllStudySerieses();
}
