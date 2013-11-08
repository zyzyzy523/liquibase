package liquibase.datatype.core;

import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.LiquibaseDataType;

@DataTypeInfo(name="double", aliases = {"java.sql.Types.DOUBLE", "java.lang.Double"}, minParameters = 0, maxParameters = 2, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class DoubleType  extends LiquibaseDataType {
}
