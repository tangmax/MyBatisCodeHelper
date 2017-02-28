package com.ccnode.codegenerator.database.handler.oracle;

import com.ccnode.codegenerator.database.handler.*;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
public class OracleDatabaseFactory implements DatabaseFactory{

    @Override
    public GenerateFileHandler getGenerateFileHandler() {
        return OracleGenerateFilesHandler.getInstance();
    }

    @Override
    public GenerateMethodXmlHandler getMethodXmlHandler() {
        return OracleGenerateMethodXmlHandler.getInstance();
    }

    @Override
    public UpdateFieldHandler getUpdateFieldHandler() {
        return OracleUpdateFiledHandler.getInstance();
    }

    @Override
    public AutoCompleteHandler getAutoCompleteHandler() {
        return OracleAutoComplateHandler.getInstance();
    }
}
