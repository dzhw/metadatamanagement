package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Interface for custom repository methods.
 * 
 * @author René Reitmann
 */
public interface DataPackageRepositoryCustom {
  List<I18nString> findAllStudySerieses();
}
