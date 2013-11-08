package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;

@DataTypeInfo(name = "int", aliases = {"integer", "java.sql.Types.INTEGER", "java.lang.Integer", "serial"}, minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class IntType extends LiquibaseDataType {

    private boolean autoIncrement;

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }


    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return super.toDatabaseDataType(database);

        //sqllite
        //        if (columnTypeString.equals("INTEGER") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("int") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("bit")) {
//            type = new IntType("INTEGER");
    }
}
