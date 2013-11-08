package liquibase.datatype.core;

import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.LiquibaseDataType;

@DataTypeInfo(name = "timestamp", aliases = {"timestamp", "java.sql.Types.TIMESTAMP", "java.sql.Timestamp"}, minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class TimestampType extends DateTimeType {

}
