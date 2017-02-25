package com.ccnode.codegenerator.database.handler;

import com.ccnode.codegenerator.nextgenerationparser.buidler.MethodNameParsedResult;
import com.ccnode.codegenerator.nextgenerationparser.buidler.QueryInfo;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
public interface QueryBuilder {
    QueryInfo buildQueryInfoByMethodNameParsedResult(MethodNameParsedResult result);
}
