package com.ccnode.codegenerator.database.handler;

import com.ccnode.codegenerator.methodnameparser.buidler.MethodNameParsedResult;
import com.ccnode.codegenerator.methodnameparser.buidler.QueryInfo;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
public interface QueryBuilderHandler {
    void handleFindBeforeFromTable(QueryInfo info, MethodNameParsedResult parsedResult,boolean queryAllTable);

    void handleFindAfterFromTable(QueryInfo info, MethodNameParsedResult parsedResult);

    void handleUpdate(QueryInfo info, MethodNameParsedResult parsedResult);

    void handleDelete(QueryInfo info, MethodNameParsedResult parsedResult);

    void handleCount(QueryInfo queryInfo, MethodNameParsedResult parsedResult);
}
