package com.ccnode.codegenerator.pojo;

/**
 * @Author bruce.ge
 * @Date 2017/3/5
 * @Description
 */
public class ExistXmlTagInfo {
    private String tableName;

    private FieldToColumnRelation fieldToColumnRelation;

    private boolean hasAllColumn;


    private String allColumnSqlId;


    private boolean hasResultMap;

    public boolean isHasResultMap() {
        return hasResultMap;
    }

    public void setHasResultMap(boolean hasResultMap) {
        this.hasResultMap = hasResultMap;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public FieldToColumnRelation getFieldToColumnRelation() {
        return fieldToColumnRelation;
    }

    public void setFieldToColumnRelation(FieldToColumnRelation fieldToColumnRelation) {
        this.fieldToColumnRelation = fieldToColumnRelation;
    }

    public boolean isHasAllColumn() {
        return hasAllColumn;
    }

    public void setHasAllColumn(boolean hasAllColumn) {
        this.hasAllColumn = hasAllColumn;
    }

    public String getAllColumnSqlId() {
        return allColumnSqlId;
    }

    public void setAllColumnSqlId(String allColumnSqlId) {
        this.allColumnSqlId = allColumnSqlId;
    }
}
