package eu.dzhw.fdz.metadatamanagement.repository;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Variable entity.
 */
public interface VariableRepository extends JpaRepository<Variable,Long> {

}
