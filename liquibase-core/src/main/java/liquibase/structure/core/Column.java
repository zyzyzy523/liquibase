package liquibase.structure.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DataType;
import liquibase.structure.DatabaseObject;

import java.math.BigInteger;
import java.util.List;

public class Column extends AbstractDatabaseObject {

    private String name;

    public Column() {
    }


    /**
     * Create a ColumnConfig object based on this object.
     * It will attempt to set as much as possible based on the information in the snapshot.
     */
    public ColumnConfig toColumnConfig() {
        ColumnConfig config = new ColumnConfig();
        config.setName(this.getName());
        config.setType(this.getType().toString());

        if (this.getRelation() != null && this.getRelation() instanceof Table) {
            if (this.getDefaultValue() != null) {
                config.setDefaultValue(this.getDefaultValue().toString());
            }
            ConstraintsConfig constraints = new ConstraintsConfig();

            constraints.setNullable(this.isNullable());

            if (this.isAutoIncrement()) {
                config.setAutoIncrement(true);
                config.setStartWith(this.getAutoIncrementInformation().getStartWith());
                config.setIncrementBy(this.getAutoIncrementInformation().getIncrementBy());
            } else {
                config.setAutoIncrement(false);
            }


            Table table = (Table) this.getRelation();
            PrimaryKey primaryKey = table.getPrimaryKey();
            if (primaryKey != null && primaryKey.getColumnNamesAsList().contains(this.getName())) {
                constraints.setPrimaryKey(true);
                constraints.setPrimaryKeyName(primaryKey.getName());
                constraints.setPrimaryKeyTablespace(primaryKey.getTablespace());
            }

            List<UniqueConstraint> uniqueConstraints = table.getUniqueConstraints();
            if (uniqueConstraints != null) {
                for (UniqueConstraint constraint : uniqueConstraints) {
                    if (constraint.getColumnNames().contains(getName())) {
                        constraints.setUnique(true);
                        constraints.setUniqueConstraintName(constraint.getName());
                    }
                }
            }

            List<ForeignKey> fks = table.getOutgoingForeignKeys();
            if (fks != null) {
                for (ForeignKey fk : fks) {
                    if (fk.getForeignKeyColumns().equals(getName())) {
                        constraints.setForeignKeyName(fk.getName());
                        constraints.setReferences(fk.getPrimaryKeyTable().getName() + "(" + fk.getPrimaryKeyColumns() + ")");
                    }
                }
            }

            if (constraints.isPrimaryKey() == null) {
                constraints.setPrimaryKey(false);
            }
            if (constraints.isUnique() == null) {
                constraints.setUnique(false);
            }
            config.setConstraints(constraints);
        }

        config.setRemarks(this.getRemarks());

        return config;
    }

    public Relation getRelation() {
        return getAttribute("relation", Relation.class);
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        return new DatabaseObject[] {
                getRelation()
        };
    }

    public Column setRelation(Relation relation) {
        setAttribute("relation", relation);

        return this;
    }


    @Override
    public Schema getSchema() {
        Relation relation = getRelation();
        if (relation == null) {
            return null;
        }
        return relation.getSchema();
    }

	@Override
    public String getName() {
        return name;
    }

    @Override
    public Column setName(String name) {
        this.name = name;
        setAttribute("name", name);

        return this;
    }

    public Boolean isNullable() {
        return getAttribute("nullable", Boolean.class);
    }

    public Column setNullable(Boolean nullable) {
        setAttribute("nullable", nullable);

        return this;
    }


    public DataType getType() {
        return getAttribute("type", DataType.class);
    }

    public Column setType(DataType type) {
        setAttribute("type", type);

        return this;
    }

    public Object getDefaultValue() {
        return getAttribute("defaultValue", Object.class);
    }

    public Column setDefaultValue(Object defaultValue) {
        setAttribute("defaultValue", defaultValue);

        return this;
    }

    public boolean isAutoIncrement() {
       return getAutoIncrementInformation() != null;
    }

    public AutoIncrementInformation getAutoIncrementInformation() {
        return getAttribute("autoIncrementInformation", AutoIncrementInformation.class);
    }

    public void setAutoIncrementInformation(AutoIncrementInformation autoIncrementInformation) {
        setAttribute("autoIncrementInformation", autoIncrementInformation);
    }

    @Override
    public String toString() {
        String tableOrViewName = getRelation().getName();
        return tableOrViewName +"."+getName();
    }


    @Override
    public int compareTo(Object other) {
        Column o = (Column) other;
        try {
            //noinspection UnusedAssignment
            int returnValue = 0;
            if (this.getRelation() != null && o.getRelation() == null) {
                return 1;
            } else if (this.getRelation() == null && o.getRelation() != null) {
                return -1;
            } else {
                returnValue = this.getRelation().compareTo(o.getRelation());
            }

            if (returnValue == 0) {
                returnValue = this.getName().compareTo(o.getName());
            }

            return returnValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean equals(Object o) {
        try {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Column column = (Column) o;

            return getName().equalsIgnoreCase(column.getName()) && !(getRelation() != null ? !getRelation().equals(column.getRelation()) : column.getRelation() != null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int hashCode() {
        try {
            int result;
            result = (getRelation() != null ? getRelation().hashCode() : 0);
            result = 31 * result + getName().toUpperCase().hashCode();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isDataTypeDifferent(Column otherColumn) {
        if (!this.isCertainDataType() || !otherColumn.isCertainDataType()) {
            return false;
        } else {
            return !this.getType().equals(otherColumn.getType());
        }
    }

    @SuppressWarnings({"SimplifiableIfStatement"})
    public boolean isNullabilityDifferent(Column otherColumn) {
        if (this.isNullable() == null && otherColumn.isNullable() == null) {
            return false;
        }
        if (this.isNullable() == null && otherColumn.isNullable() != null) {
            return true;
        }
        if (this.isNullable() != null && otherColumn.isNullable() == null) {
            return true;
        }
        return !this.isNullable().equals(otherColumn.isNullable());
    }

    public boolean isDifferent(Column otherColumn) {
        return isDataTypeDifferent(otherColumn) || isNullabilityDifferent(otherColumn);
    }


    public boolean isCertainDataType() {
        return getAttribute("certainDataType", Boolean.class);
    }

    public Column setCertainDataType(boolean certainDataType) {
        setAttribute("certainDataType", certainDataType);

        return this;
    }

    public String getRemarks() {
        return getAttribute("remarks", String.class);
    }

    public Column setRemarks(String remarks) {
        setAttribute("remarks", remarks);

        return this;
    }

    public static class AutoIncrementInformation {
        private BigInteger startWith;
        private BigInteger incrementBy;

        public AutoIncrementInformation() {
            this(1,1);
        }

        public AutoIncrementInformation(Number startWith, Number incrementBy) {
            this.startWith = BigInteger.valueOf(startWith.longValue());
            this.incrementBy = BigInteger.valueOf(incrementBy.longValue());
        }

        public BigInteger getStartWith() {
            return startWith;
        }

        public BigInteger getIncrementBy() {
            return incrementBy;
        }

        @Override
        public String toString() {
            return "AUTO INCREMENT START WITH "+startWith+" INCREMENT BY "+incrementBy;
        }
    }
}

