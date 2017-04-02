package com.ccnode.codegenerator.database;

/**
 * @Author bruce.ge
 * @Date 2017/3/6
 * @Description
 */
public class FieldInfo {
    private String fieldName;

    private String fieldType;

    private String columnName;

    private String fieldModifier;


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldModifier() {
        return fieldModifier;
    }

    public void setFieldModifier(String fieldModifier) {
        this.fieldModifier = fieldModifier;
    }
}
