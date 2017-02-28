package com.ccnode.codegenerator.database.handler;

/**
 * @Author bruce.ge
 * @Date 2017/2/23
 * @Description
 */
//shall make it easy to test.
public interface DatabaseFactory{

    GenerateFileHandler getGenerateFileHandler();

    GenerateMethodXmlHandler  getMethodXmlHandler();

    UpdateFieldHandler getUpdateFieldHandler();

    AutoCompleteHandler getAutoCompleteHandler();

}
