package com.ccnode.codegenerator.database.handler.mysql;

import com.ccnode.codegenerator.database.handler.*;

/**
 * @Author bruce.ge
 * @Date 2017/2/23
 * @Description
 */
public class MysqlDatabaseFactory implements DatabaseFactory {

    @Override
    public GenerateFileHandler getGenerateFileHandler() {
        return MysqlGenerateFileHandler.getInstance();
    }

    @Override
    public GenerateMethodXmlHandler getMethodXmlHandler() {
        return MysqlGenerateMethodXmlHandler.getInstance();
    }

    @Override
    public UpdateFieldHandler getUpdateFieldHandler() {
        return MysqlUpdateFieldHandler.getInstance();
    }

    @Override
    public AutoCompleteHandler getAutoCompleteHandler() {
        return MysqlAutoCompleteHandler.getInstance();
    }
}
