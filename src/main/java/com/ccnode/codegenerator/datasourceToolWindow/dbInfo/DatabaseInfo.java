package com.ccnode.codegenerator.datasourceToolWindow.dbInfo;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/26
 * @Description
 */
public class DatabaseInfo {
    private String databaseName;

    private List<TableInfo> tableInfoList;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    public void setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
    }
}
