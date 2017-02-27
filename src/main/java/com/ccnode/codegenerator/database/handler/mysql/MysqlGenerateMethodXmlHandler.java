package com.ccnode.codegenerator.database.handler.mysql;

import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.database.handler.BaseQueryBuilder;
import com.ccnode.codegenerator.database.handler.GenerateMethodXmlHandler;
import com.ccnode.codegenerator.database.handler.QueryBuilderHandler;
import com.ccnode.codegenerator.nextgenerationparser.buidler.MethodNameParsedResult;
import com.ccnode.codegenerator.nextgenerationparser.buidler.QueryInfo;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.find.OrderByRule;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.find.ParsedFind;
import com.ccnode.codegenerator.pojo.FieldToColumnRelation;
import com.ccnode.codegenerator.util.GenCodeUtil;

import java.util.ArrayList;

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
                    baseQueryBuilder = new BaseQueryBuilder(new MysqlQueryBuilderHander());
                }
            }
        }
        return baseQueryBuilder.buildQueryInfoByMethodNameParsedResult(result);
    }


    class MysqlQueryBuilderHander implements QueryBuilderHandler {
        @Override
        public void handleFindBeforeFromTable(QueryInfo info, MethodNameParsedResult parsedResult, boolean queryAllTable) {
            ParsedFind find = parsedResult.getParsedFind();
            FieldToColumnRelation relation = parsedResult.getRelation();
            StringBuilder builder = new StringBuilder();
            //will notice it.
            if (queryAllTable) {
                builder.append("\n" + GenCodeUtil.ONE_RETRACT + "select <include refid=\"" + MapperConstants.ALL_COLUMN + "\"/>");
            } else {
                builder.append("\n" + GenCodeUtil.ONE_RETRACT + "select");
                if (find.getDistinct()) {
                    builder.append(" distinct(");
                    for (String prop : find.getFetchProps()) {
                        builder.append(relation.getPropColumn(prop) + ",");
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    builder.append(")");
                } else {
                    for (String prop : find.getFetchProps()) {
                        builder.append(" " + relation.getPropColumn(prop) + ",");
                    }
                    builder.deleteCharAt(builder.length() - 1);
                }
            }
            info.setSql(builder.toString());
        }

        @Override
        public void handleFindAfterFromTable(QueryInfo info, MethodNameParsedResult parsedResult) {
            ParsedFind find = parsedResult.getParsedFind();
            if (find.getQueryRules() != null) {
                info.setParamInfos(new ArrayList<>());
                new BaseQueryBuilder(this).buildQuerySqlAndParam(find.getQueryRules(), info, parsedResult.getFieldMap(), parsedResult.getRelation());
            }
            if (find.getOrderByProps() != null) {
                info.setSql(info.getSql() + " order by");
                for (OrderByRule rule : find.getOrderByProps()) {
                    info.setSql(info.getSql() + " " + parsedResult.getRelation().getPropColumn(rule.getProp()) + " " + rule.getOrder());
                }
            }
            if (find.getLimit() > 0) {
                info.setSql(info.getSql() + " limit " + find.getLimit());
            }

        }


        @Override
        public void handleUpdate(QueryInfo info, MethodNameParsedResult parsedResult) {

        }

        @Override
        public void handleDelete(QueryInfo info, MethodNameParsedResult parsedResult) {

        }

        @Override
        public void handleCount(QueryInfo queryInfo, MethodNameParsedResult parsedResult) {

        }
    }
}
