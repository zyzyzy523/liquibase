package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;
import liquibase.datatype.DatabaseDataType;

import java.util.Arrays;

public class UnknownTypeMySQL extends UnknownType {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(Database database) {
        return database instanceof MySQLDatabase;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        int dataTypeMaxParameters = database.getDataTypeMaxParameters(getName());
        Object[] parameters = getParameters();
        if (database instanceof MySQLDatabase && (
                getName().equalsIgnoreCase("TINYBLOB")
                        || getName().equalsIgnoreCase("MEDIUMBLOB")
                        || getName().equalsIgnoreCase("TINYTEXT")
                        || getName().equalsIgnoreCase("MEDIUMTEXT")
                        || getName().equalsIgnoreCase("REAL")
        )) {
            parameters = new Object[0];
        }

        if (dataTypeMaxParameters < parameters.length) {
            parameters = Arrays.copyOfRange(parameters, 0, dataTypeMaxParameters);
        }
        DatabaseDataType type = new DatabaseDataType(getName().toUpperCase(), parameters);
        type.addAdditionalInformation(getAdditionalInformation());

        return type;
    }
}
