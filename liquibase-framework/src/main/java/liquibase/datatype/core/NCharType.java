package liquibase.datatype.core;

import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.LiquibaseDataType;

@DataTypeInfo(name="nchar", aliases = "java.sql.Types.NCHAR", minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class NCharType extends CharType {


}