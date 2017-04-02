package com.ccnode.codegenerator.view.completion;

import com.ccnode.codegenerator.datasourceToolWindow.NewDatabaseInfo;
import com.ccnode.codegenerator.sqlparse.TableNameAndFieldName;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
public interface MysqlCompleteCacheInteface {

    void addDatabaseCache(NewDatabaseInfo newDatabaseInfo);

    List<String> getRecommendFromCache(String currentText, String allText);


    List<String> getAllTables();

    List<String> getAllFields();

    List<TableNameAndFieldName> getAllFieldsWithTable();


    List<TableNameAndFieldName> getTableAllFields(String tableName);


    String getFieldType();
    void cleanAll();

}
