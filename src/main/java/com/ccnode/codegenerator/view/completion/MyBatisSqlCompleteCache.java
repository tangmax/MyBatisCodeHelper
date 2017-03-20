package com.ccnode.codegenerator.view.completion;

import com.ccnode.codegenerator.datasourceToolWindow.NewDatabaseInfo;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.DatabaseConnector;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.DatabaseInfo;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.TableColumnInfo;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.TableInfo;
import com.ccnode.codegenerator.sqlparse.TableNameAndFieldName;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
/*
* try to find the column type for the database.
*
* */
public class MyBatisSqlCompleteCache implements MysqlCompleteCacheInteface {

    private List<String> tables = new ArrayList<>();
    //tableName and field map

    private ArrayListMultimap<String, String> tableToField = ArrayListMultimap.create();

    private Map<CompleteField, String> fieldMap = new HashMap<>();

    public void addDatabaseCache(NewDatabaseInfo newDatabaseInfo) {
        //get it in the database.
        //a project could have multiple database.
        //then go to the cache.
        DatabaseInfo root = DatabaseConnector.getDataBaseInfoFromConnection(newDatabaseInfo.getDatabaseType(), newDatabaseInfo.getUrl(), newDatabaseInfo.getUserName(), newDatabaseInfo.getPassword(), newDatabaseInfo.getDatabase());
        if (root == null) {
            throw new RuntimeException("connect to db fail");
        }
        List<TableInfo> tableInfoList =
                root.getTableInfoList();
        if (tableInfoList == null || tableInfoList.size() == 0) {
            throw new RuntimeException("there is no table in the database");
        }

        for (TableInfo tableInfo : tableInfoList) {
            tables.add(tableInfo.getTableName());
            List<TableColumnInfo> tableColumnInfos =
                    tableInfo.getTableColumnInfos();
            if (tableColumnInfos == null || tableColumnInfos.size() == 0) {
                continue;
            }
            for (TableColumnInfo tableColumnInfo : tableColumnInfos) {
                tableToField.put(tableInfo.getTableName(), tableColumnInfo.getFieldName());
                fieldMap.put(new CompleteField(newDatabaseInfo.getDatabaseType(), tableInfo.getTableName(), tableColumnInfo.getFieldName()), tableColumnInfo.getFieldType());
            }
        }
    }

    public List<String> getRecommendFromCache(String currentText, String allText) {
        //
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllTables() {
        return tables;
    }

    @Override
    public List<String> getAllFields() {
        List<String> fields = Lists.newArrayList();
        for (Map.Entry<String, String> map : tableToField.entries()) {
            fields.add(map.getValue());
        }
        return fields;
    }

    @Override
    public List<TableNameAndFieldName> getAllFieldsWithTable() {
        List<TableNameAndFieldName> fields = Lists.newArrayList();
        for (Map.Entry<String, String> map : tableToField.entries()) {
            TableNameAndFieldName tableNameAndFieldName = new TableNameAndFieldName();
            tableNameAndFieldName.setTableName(map.getKey());
            tableNameAndFieldName.setFieldName(map.getValue());
            fields.add(tableNameAndFieldName);
        }
        return fields;
    }

    @Override
    public List<TableNameAndFieldName> getTableAllFields(String tableName) {
        List<String> strings = tableToField.get(tableName);
        List<TableNameAndFieldName> tableNameAndFieldNames = Lists.newArrayList();
        for (String string : strings) {
            TableNameAndFieldName tableNameAndFieldName = new TableNameAndFieldName();
            tableNameAndFieldName.setTableName(tableName);
            tableNameAndFieldName.setFieldName(string);
            tableNameAndFieldNames.add(tableNameAndFieldName);
        }
        return tableNameAndFieldNames;
    }


    @Override
    public String getFieldType() {
        return null;
    }

    @Override
    public void cleanAll() {
        this.fieldMap = new HashMap<>();
        this.tables = Lists.newArrayList();
        this.tableToField = ArrayListMultimap.create();
    }
}
