package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.datatype.DatabaseDataType;

public class VarcharTypeCore extends VarcharType {
    @Override
    public boolean supports(Database database) {
        return true;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE - 1;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("VARCHAR2", getParameters());
        }

        if (database instanceof InformixDatabase && getSize() > 255) {
            return new DatabaseDataType("LVARCHAR", getParameters());
        }

        return super.toDatabaseDataType(database);
    }

    //oracle
    //			if (columnTypeString.toUpperCase().startsWith("VARCHAR2")) {
//				// Varchar2 type pattern: VARCHAR2(50 BYTE) | VARCHAR2(50 CHAR)
//				returnTypeName = getVarcharType();
//				if (precision != null) {
//					String[] typeParams = precision.split(" ");
//					returnTypeName.setFirstParameter(typeParams[0].trim());
//					if (typeParams.length > 1) {
//						returnTypeName.setUnit(typeParams[1]);
//					}
//				}
//			} else if (columnTypeString.toUpperCase().startsWith("NVARCHAR2")) {
//				// NVarchar2 type pattern: VARCHAR2(50 BYTE) | VARCHAR2(50 CHAR)
//				returnTypeName = getNVarcharType();
//				if (precision != null) {
//					String[] typeParams = precision.split(" ");
//					returnTypeName.setFirstParameter(typeParams[0].trim());
//					if (typeParams.length > 1) {
//						returnTypeName.setUnit(typeParams[1]);
//					}
//				}
//			}

}
