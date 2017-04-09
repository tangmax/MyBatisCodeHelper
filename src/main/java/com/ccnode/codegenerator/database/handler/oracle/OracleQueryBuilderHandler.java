package com.ccnode.codegenerator.database.handler.oracle;

import com.ccnode.codegenerator.database.handler.BaseQueryBuilder;
import com.ccnode.codegenerator.database.handler.QueryBuilderHandler;
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
public class OracleQueryBuilderHandler implements QueryBuilderHandler {

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
                        //todo when there is function in it.
                    }
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
                info.setSql(info.getSql() + " " + parsedResult.getRelation().getPropFormatColumn(rule.getProp()) + " " + rule.getOrder());
            }
        }
        if (find.getLimit() > 0) {
            if (find.getQueryRules() != null && find.getQueryRules().size() > 0) {
                info.setSql(info.getSql() + " and ROWNUM " + BaseQueryBuilder.cdata("<=") + find.getLimit());
            } else {
                info.setSql(info.getSql() + " ROWNUM " + BaseQueryBuilder.cdata("<=") + find.getLimit());
            }
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
