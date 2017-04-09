package com.ccnode.codegenerator.database.handler.mysql;

import com.ccnode.codegenerator.database.DbUtils;
import com.ccnode.codegenerator.database.handler.BaseQueryBuilder;
import com.ccnode.codegenerator.database.handler.QueryBuilderHandler;
import com.ccnode.codegenerator.methodnameparser.KeyWordConstants;
import com.ccnode.codegenerator.methodnameparser.buidler.MethodNameParsedResult;
import com.ccnode.codegenerator.methodnameparser.buidler.QueryInfo;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.FetchProp;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.OrderByRule;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFind;
import com.ccnode.codegenerator.pojo.FieldToColumnRelation;
import com.ccnode.codegenerator.util.GenCodeUtil;

import java.util.ArrayList;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
public class MysqlQueryBuilderHandler implements QueryBuilderHandler {

    @Override
    public void handleFindBeforeFromTable(QueryInfo info, MethodNameParsedResult parsedResult, boolean queryAllTable) {
        ParsedFind find = parsedResult.getParsedFind();
        FieldToColumnRelation relation = parsedResult.getRelation();
        StringBuilder builder = new StringBuilder();
        //will notice it.
        if (queryAllTable) {
            builder.append("\n" + GenCodeUtil.ONE_RETRACT + "select <include refid=\"" + parsedResult.getAllColumnName() + "\"/>");
        } else {
            builder.append("\n" + GenCodeUtil.ONE_RETRACT + "select");
            if (find.getDistinct()) {
                builder.append(" distinct(");
                for (FetchProp prop : find.getFetchProps()) {
                    builder.append(relation.getPropFormatColumn(prop.getFetchProp()) + ",");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append(")");
            } else {
                for (FetchProp prop : find.getFetchProps()) {
                    if (prop.getFetchFunction() == null) {
                        builder.append(" " + relation.getPropFormatColumn(prop.getFetchProp()) + ",");
                    } else {
                        handleWithFunction(relation, builder, prop);
                    }
                }
                builder.deleteCharAt(builder.length() - 1);
            }
        }
        info.setSql(builder.toString());
    }

    public static void handleWithFunction(FieldToColumnRelation relation, StringBuilder builder, FetchProp prop) {
        String returnVal = DbUtils.buildSelectFunctionVal(prop);
        switch (prop.getFetchFunction()) {
            case KeyWordConstants.MAX: {
                builder.append(String.format(" max(%s) as %s,", relation.getPropFormatColumn(prop.getFetchProp()),returnVal));
                break;
            }
            case KeyWordConstants.MIN: {
                builder.append(String.format(" min(%s) as %s,", relation.getPropFormatColumn(prop.getFetchProp()),returnVal));
                break;
            }
            case KeyWordConstants.AVG: {
                builder.append(String.format(" avg(%s) as %s,", relation.getPropFormatColumn(prop.getFetchProp()),returnVal));
                break;
            }
            case KeyWordConstants.SUM: {
                builder.append(String.format(" sum(%s) as %s,", relation.getPropFormatColumn(prop.getFetchProp()),returnVal));
                break;
            }
        }
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
                info.setSql(info.getSql() + " " + parsedResult.getRelation().getPropFormatColumn(rule.getProp()) + " " + rule.getOrder());
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
