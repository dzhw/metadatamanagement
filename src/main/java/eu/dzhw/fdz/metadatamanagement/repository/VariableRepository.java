package eu.dzhw.fdz.metadatamanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;

/**
 * Spring Data JPA repository for the Variable entity.
 */
public interface VariableRepository extends JpaRepository<Variable, Long> {

}
