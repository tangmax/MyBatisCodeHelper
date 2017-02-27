package com.ccnode.codegenerator.database.handler.oracle;

import com.ccnode.codegenerator.database.handler.BaseQueryBuilder;
import com.ccnode.codegenerator.database.handler.GenerateMethodXmlHandler;
import com.ccnode.codegenerator.nextgenerationparser.buidler.MethodNameParsedResult;
import com.ccnode.codegenerator.nextgenerationparser.buidler.QueryInfo;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class OracleGenerateMethodXmlHandler implements GenerateMethodXmlHandler {


    private static volatile OracleGenerateMethodXmlHandler mInstance;

    private OracleGenerateMethodXmlHandler() {
    }

    public static OracleGenerateMethodXmlHandler getInstance() {
        if (mInstance == null) {
            synchronized (OracleGenerateMethodXmlHandler.class) {
                if (mInstance == null) {
                    mInstance = new OracleGenerateMethodXmlHandler();
                }
            }
        }
        return mInstance;
    }

    private static volatile BaseQueryBuilder baseQueryBuilder;


    @Override
    public QueryInfo buildQueryInfoByMethodNameParsedResult(MethodNameParsedResult result) {
        if (baseQueryBuilder == null) {
            synchronized (OracleGenerateMethodXmlHandler.class) {
                baseQueryBuilder = new BaseQueryBuilder(new OracleQueryBuilderHandler());
            }
        }
        return baseQueryBuilder.buildQueryInfoByMethodNameParsedResult(result);
    }
}
