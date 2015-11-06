package eu.dzhw.fdz.metadatamanagement.domain.util;

import java.sql.Types;

import org.hibernate.dialect.H2Dialect;

/**
 * Extension to a H2Dialect for Float/real columns.
 */
public class FixedH2Dialect extends H2Dialect {

  public FixedH2Dialect() {
    super();
    registerColumnType(Types.FLOAT, "real");
  }
}
