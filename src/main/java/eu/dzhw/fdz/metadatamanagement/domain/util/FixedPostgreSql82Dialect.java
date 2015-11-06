package eu.dzhw.fdz.metadatamanagement.domain.util;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

/**
 * Extension to the postgres sql dialect. Supports blobs coloumns.
 */
public class FixedPostgreSql82Dialect extends PostgreSQL82Dialect {

  public FixedPostgreSql82Dialect() {
    super();
    registerColumnType(Types.BLOB, "bytea");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.hibernate.dialect.Dialect#remapSqlTypeDescriptor(org.hibernate.type.descriptor.sql.
   * SqlTypeDescriptor)
   */
  @Override
  public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
    if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) { // NOPMD
      return BinaryTypeDescriptor.INSTANCE;
    }
    return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
  }
}
