package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.LiquibaseDataType;

@DataTypeInfo(name="clob", aliases = {"text", "longtext", "java.sql.Types.CLOB"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class ClobType extends LiquibaseDataType {

    @Override
    public String objectToSql(Object value, Database database) {
        if (value == null || value.toString().equalsIgnoreCase("null")) {
            return null;
        }
        String val = String.valueOf(value);
        // postgres type character varying gets identified as a char type
        // simple sanity check to avoid double quoting a value
        if (val.startsWith("'")) {
            return val;
        } else {
            return "'"+database.escapeStringForDatabase(val)+"'";
        }
    }



    //sqlite
    //        } else if (columnTypeString.equals("TEXT") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("uuid") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("uniqueidentifier") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).equals("uniqueidentifier") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).equals("datetime") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("timestamp") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("char") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("clob") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("text")) {
//            type = new CustomType("TEXT",0,0);

}
