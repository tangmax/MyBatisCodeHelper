package com.ccnode.codegenerator.view.completion;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
public class CompleteField {
    private String databaseType;

    private String tableName;

    private String fieldName;

    public CompleteField(String databaseType, String tableName, String fieldName) {
        this.databaseType = databaseType;
        this.tableName = tableName;
        this.fieldName = fieldName;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CompleteField that = (CompleteField) o;

        return new EqualsBuilder()
                .append(databaseType, that.databaseType)
                .append(tableName, that.tableName)
                .append(fieldName, that.fieldName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(databaseType)
                .append(tableName)
                .append(fieldName)
                .toHashCode();
    }
}
