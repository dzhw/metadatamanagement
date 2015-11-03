package eu.dzhw.fdz.metadatamanagement.repository;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Survey entity.
 */
public interface SurveyRepository extends JpaRepository<Survey,Long> {

}
