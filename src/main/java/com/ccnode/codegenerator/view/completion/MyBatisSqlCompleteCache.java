package com.ccnode.codegenerator.view.completion;

import com.ccnode.codegenerator.datasourceToolWindow.NewDatabaseInfo;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.DatabaseConnector;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.DatabaseInfo;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.TableColumnInfo;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.TableInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

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
public class MyBatisSqlCompleteCache implements MysqlCompleteCacheInteface{

    private  List<String> tables = new ArrayList<>();
    //tableName and field map

    private  Multimap<String, String> tableToField = ArrayListMultimap.create();

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
    public void cleanAll() {
        this.fieldMap = new HashMap<>();
        this.tables = Lists.newArrayList();
        this.tableToField = ArrayListMultimap.create();
    }
}
