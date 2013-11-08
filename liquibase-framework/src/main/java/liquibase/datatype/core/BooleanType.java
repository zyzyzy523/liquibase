package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.LiquibaseDataType;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.statement.DatabaseFunction;

@DataTypeInfo(name = "boolean", aliases = {"java.sql.Types.BOOLEAN", "java.lang.Boolean", "bit"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class BooleanType extends LiquibaseDataType {


    @Override
    public String objectToSql(Object value, Database database) {
        if (value == null || value.toString().equalsIgnoreCase("null")) {
            return null;
        }

        String returnValue;
        if (value instanceof String) {
            if (((String) value).equalsIgnoreCase("true") || value.equals("1") || value.equals("t") || ((String) value).equalsIgnoreCase(this.getTrueBooleanValue(database))) {
                returnValue = this.getTrueBooleanValue(database);
            } else if (((String) value).equalsIgnoreCase("false") || value.equals("0") || value.equals("f") || ((String) value).equalsIgnoreCase(this.getFalseBooleanValue(database))) {
                returnValue = this.getFalseBooleanValue(database);
            } else {
                throw new UnexpectedLiquibaseException("Unknown boolean value: " + value);
            }
        } else if (value instanceof Long) {
            if (Long.valueOf(1).equals(value)) {
                returnValue = this.getTrueBooleanValue(database);
            } else {
                returnValue = this.getFalseBooleanValue(database);
            }
        } else if (value instanceof DatabaseFunction) {
            return ((DatabaseFunction) value).toString();
        } else if (((Boolean) value)) {
            returnValue = this.getTrueBooleanValue(database);
        } else {
            returnValue = this.getFalseBooleanValue(database);
        }

        return returnValue;
    }

    protected boolean isNumericBoolean(Database database) {
        return false;
    }

    /**
     * The database-specific value to use for "false" "boolean" columns.
     */
    public String getFalseBooleanValue(Database database) {
        return "FALSE";
    }

    /**
     * The database-specific value to use for "true" "boolean" columns.
     */
    public String getTrueBooleanValue(Database database) {
        return "TRUE";
    }

    //sqllite
    //        } else if (columnTypeString.toLowerCase(Locale.ENGLISH).contains("boolean") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("binary")) {
//            type = new BooleanType("BOOLEAN");

}
