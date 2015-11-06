package eu.dzhw.fdz.metadatamanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;

/**
 * Spring Data JPA repository for the Survey entity.
 */
public interface SurveyRepository extends JpaRepository<Survey, Long> {

}
