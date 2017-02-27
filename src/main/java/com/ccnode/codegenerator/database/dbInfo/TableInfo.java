package com.ccnode.codegenerator.database.dbInfo;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/26
 * @Description
 */
public class TableInfo {

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private List<TableColumnInfo>  tableColumnInfos;

    public List<TableColumnInfo> getTableColumnInfos() {
        return tableColumnInfos;
    }

    public void setTableColumnInfos(List<TableColumnInfo> tableColumnInfos) {
        this.tableColumnInfos = tableColumnInfos;
    }
}
