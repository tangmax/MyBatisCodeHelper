package com.ccnode.codegenerator.datasourceToolWindow.dbInfo;

/**
 * @Author bruce.ge
 * @Date 2017/2/26
 * @Description
 */
public class TableColumnInfo {
    private String fieldName;

    private String fieldType;


    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
