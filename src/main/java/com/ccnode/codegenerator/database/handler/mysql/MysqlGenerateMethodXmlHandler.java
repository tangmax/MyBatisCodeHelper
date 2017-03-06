package com.ccnode.codegenerator.database.handler.mysql;

import com.ccnode.codegenerator.database.handler.BaseQueryBuilder;
import com.ccnode.codegenerator.database.handler.GenerateMethodXmlHandler;
import com.ccnode.codegenerator.methodnameparser.buidler.MethodNameParsedResult;
import com.ccnode.codegenerator.methodnameparser.buidler.QueryInfo;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class MysqlGenerateMethodXmlHandler implements GenerateMethodXmlHandler {
    private static volatile MysqlGenerateMethodXmlHandler instance;

    private MysqlGenerateMethodXmlHandler() {
    }

    private static volatile BaseQueryBuilder baseQueryBuilder;

    public static MysqlGenerateMethodXmlHandler getInstance() {
        if (instance == null) {
            synchronized (MysqlGenerateMethodXmlHandler.class) {
                if (instance == null) {
                    instance = new MysqlGenerateMethodXmlHandler();
                }
            }
        }
        return instance;
    }


    @Override
    public QueryInfo buildQueryInfoByMethodNameParsedResult(MethodNameParsedResult result) {
        if (baseQueryBuilder == null) {
            synchronized (MysqlGenerateMethodXmlHandler.class) {
                if (baseQueryBuilder == null) {
                    baseQueryBuilder = new BaseQueryBuilder(new MysqlQueryBuilderHandler());
                }
            }
        }
        return baseQueryBuilder.buildQueryInfoByMethodNameParsedResult(result);
    }

}
