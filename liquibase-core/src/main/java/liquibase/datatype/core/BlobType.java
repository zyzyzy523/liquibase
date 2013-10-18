package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;

@DataTypeInfo(name="blob", aliases = {"longblob", "longvarbinary", "java.sql.Types.BLOB", "java.sql.Types.LONGBLOB", "java.sql.Types.LONGVARBINARY", "java.sql.Types.VARBINARY", "varbinary"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class BlobType extends LiquibaseDataType {

    //sqlite
    //        } else if (columnTypeString.toLowerCase(Locale.ENGLISH).contains("blob") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("binary")) {
//            type = new BlobType("BLOB");

}
