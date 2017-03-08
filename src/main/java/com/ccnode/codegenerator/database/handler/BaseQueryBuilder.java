package com.ccnode.codegenerator.database.handler;

import com.ccnode.codegenerator.constants.QueryTypeConstants;
import com.ccnode.codegenerator.database.DbUtils;
import com.ccnode.codegenerator.database.GenClassDialog;
import com.ccnode.codegenerator.database.GenClassInfo;
import com.ccnode.codegenerator.genCode.GenClassService;
import com.ccnode.codegenerator.methodnameparser.KeyWordConstants;
import com.ccnode.codegenerator.methodnameparser.buidler.MethodNameParsedResult;
import com.ccnode.codegenerator.methodnameparser.buidler.ParamInfo;
import com.ccnode.codegenerator.methodnameparser.buidler.QueryInfo;
import com.ccnode.codegenerator.methodnameparser.parsedresult.base.QueryRule;
import com.ccnode.codegenerator.methodnameparser.parsedresult.count.ParsedCount;
import com.ccnode.codegenerator.methodnameparser.parsedresult.delete.ParsedDelete;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.FetchProp;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFind;
import com.ccnode.codegenerator.methodnameparser.parsedresult.update.ParsedUpdate;
import com.ccnode.codegenerator.pojo.FieldToColumnRelation;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.MyPsiXmlUtils;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.xml.XmlFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
public class BaseQueryBuilder implements QueryBuilder {

    private QueryBuilderHandler queryBuilderHandler;

    public BaseQueryBuilder(QueryBuilderHandler handler) {
        this.queryBuilderHandler = handler;
    }

    @Override
    public QueryInfo buildQueryInfoByMethodNameParsedResult(MethodNameParsedResult result) {
        switch (result.getParsedType()) {
            case FIND:
                return buildWithFind(result);

            case UPDATE:
                return buildWithUpdate(result);

            case DELETE:
                return buildWithDelete(result);

            case COUNT:
                return buildWithCount(result);
        }
        return null;
    }

    private QueryInfo buildWithCount(MethodNameParsedResult result) {
        QueryInfo info = new QueryInfo();
        info.setImportList(new HashSet<>());
        ParsedCount count = result.getParsedCount();
        Map<String, String> fieldMap = result.getFieldMap();
        FieldToColumnRelation relation = result.getRelation();
        String tableName = result.getTableName();
        info.setType(QueryTypeConstants.SELECT);
        String idType = fieldMap.get("id");
        if (idType != null) {
            info.setReturnClass(idType);
            String returnType = extractLast(idType);
            info.getImportList().add(idType);
            info.setMethodReturnType(returnType);
        } else {
            info.setReturnClass("java.lang.Integer");
            info.setMethodReturnType("Integer");
        }

        StringBuilder builder = new StringBuilder();
        builder.append("\n" + GenCodeUtil.ONE_RETRACT + "select count(");
        if (count.isDistinct()) {
            builder.append("distinct(");
            for (int i = 0; i < count.getFetchProps().size(); i++) {
                builder.append(relation.getPropFormatColumn(count.getFetchProps().get(i)));
                if (i != count.getFetchProps().size() - 1) {
                    builder.append(",");
                }
            }
            builder.append(")");
        } else {
            if (count.getFetchProps() == null) {
                builder.append("1");
            } else {
                for (int i = 0; i < count.getFetchProps().size(); i++) {
                    builder.append(relation.getPropFormatColumn(count.getFetchProps().get(i)));
                    if (i != count.getFetchProps().size() - 1) {
                        builder.append(",");
                    }
                }
            }
        }
        builder.append(")");
        builder.append("\n" + GenCodeUtil.ONE_RETRACT + "from " + tableName);
        info.setParamInfos(new ArrayList<>());
        info.setSql(builder.toString());
        if (count.getQueryRules() != null) {
            buildQuerySqlAndParam(count.getQueryRules(), info, fieldMap, relation);
        }
        return info;
    }

    private QueryInfo buildWithDelete(MethodNameParsedResult result) {
        QueryInfo info = new QueryInfo();
        info.setImportList(new HashSet<>());
        ParsedDelete delete =
                result.getParsedDelete();
        String tableName = result.getTableName();
        FieldToColumnRelation relation = result.getRelation();
        Map<String, String> fieldMap = result.getFieldMap();
        info.setType(QueryTypeConstants.DELETE);
        info.setMethodReturnType("int");
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + GenCodeUtil.ONE_RETRACT + "delete from  " + tableName);
        info.setParamInfos(new ArrayList<>());
        info.setSql(builder.toString());
        if (delete.getQueryRules() != null) {
            buildQuerySqlAndParam(delete.getQueryRules(), info, fieldMap, relation);
        }
        return info;
    }

    private QueryInfo buildWithUpdate(MethodNameParsedResult result) {
        QueryInfo info = new QueryInfo();
        info.setImportList(new HashSet<>());
        ParsedUpdate update = result.getParsedUpdate();
        String tableName = result.getTableName();
        Map<String, String> fieldMap = result.getFieldMap();
        FieldToColumnRelation relation = result.getRelation();
        info.setType(QueryTypeConstants.UPDATE);
        info.setMethodReturnType("int");
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + GenCodeUtil.ONE_RETRACT + "update " + tableName + "\n" + GenCodeUtil.ONE_RETRACT + "set");
        info.setParamInfos(new ArrayList<>());
        for (int i = 0; i < update.getUpdateProps().size(); i++) {
            String prop = update.getUpdateProps().get(i);
            String updateQualifyType = fieldMap.get(prop);
            info.getImportList().add(updateQualifyType);
            ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("updated" + firstCharUpper(prop)).
                    withParamType(extractLast(updateQualifyType)).withParamValue("updated" + firstCharUpper(prop)).build();
            info.getParamInfos().add(paramInfo);
            builder.append(" " + relation.getPropFormatColumn(prop) + "=#{" + paramInfo.getParamAnno() + "}");
            if (i != update.getUpdateProps().size() - 1) {
                builder.append(",");
            }
        }
        info.setSql(builder.toString());
        if (update.getQueryRules() != null) {
            buildQuerySqlAndParam(update.getQueryRules(), info, fieldMap, relation);
        }
        return info;
    }


    public static String cdata(String s) {
        return "<![CDATA[" + s + "]]>";
    }

    private static String firstCharUpper(String prop) {
        return prop.substring(0, 1).toUpperCase() + prop.substring(1);
    }


    public void buildQuerySqlAndParam(List<QueryRule> queryRules, QueryInfo info, Map<String, String> fieldMap, FieldToColumnRelation relation) {
        info.setSql(info.getSql() + "\n" + GenCodeUtil.ONE_RETRACT + "where");
        StringBuilder builder = new StringBuilder();
        for (QueryRule rule : queryRules) {
            String prop = rule.getProp();
            String operator = rule.getOperator();
            String connector = rule.getConnector();
            String propColumn = relation.getPropFormatColumn(prop);
            String paramQualtifyType = fieldMap.get(prop);
            info.getImportList().add(paramQualtifyType);
            String paramShortType = extractLast(paramQualtifyType);
            if (operator == null) {
                ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno(prop).withParamType(paramShortType).withParamValue(prop).build();
                info.getParamInfos().add(paramInfo);
                builder.append(" " + propColumn + "=#{" + paramInfo.getParamAnno() + "}");
            } else {
                switch (operator) {
                    case KeyWordConstants.GREATERTHAN: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("min" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("min" + firstCharUpper(prop)).build();
                        info.getParamInfos().add(paramInfo);
                        builder.append(" " + propColumn + cdata(">") + " #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }

                    case KeyWordConstants.GREATERTHANOREQUALTO: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("min" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("min" + firstCharUpper(prop)).build();
                        info.getParamInfos().add(paramInfo);
                        builder.append(" " + propColumn + cdata(">=") + " #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }
                    case KeyWordConstants.LESSTHAN: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("max" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("max" + firstCharUpper(prop)).build();
                        info.getParamInfos().add(paramInfo);
                        builder.append(" " + propColumn + cdata("<") + " #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }

                    case KeyWordConstants.LESSTHANOREQUALTO: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("max" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("max" + firstCharUpper(prop)).build();
                        info.getParamInfos().add(paramInfo);
                        builder.append(" " + propColumn + cdata("<=") + " #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }
                    case KeyWordConstants.BETWEEN: {
                        ParamInfo min = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("min" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("min" + firstCharUpper(prop)).build();
                        ParamInfo max = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("max" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("max" + firstCharUpper(prop)).build();
                        info.getParamInfos().add(min);
                        info.getParamInfos().add(max);
                        builder.append(" " + propColumn + cdata(">") + " #{" + min.getParamAnno() + "} and " + propColumn + " " + cdata("<") + " #{" + (max.getParamAnno()) + "}");
                        break;
                    }
                    case KeyWordConstants.BETWEENOREQUALTO: {
                        ParamInfo min = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("min" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("min" + firstCharUpper(prop)).build();
                        ParamInfo max = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("max" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("max" + firstCharUpper(prop)).build();
                        info.getParamInfos().add(min);
                        info.getParamInfos().add(max);
                        builder.append(" " + propColumn + cdata(">=") + " #{" + min.getParamAnno() + "} and " + propColumn + " " + cdata("<=") + " #{" + (max.getParamAnno()) + "}");
                        break;
                    }

                    case KeyWordConstants.ISNOTNULL: {
                        builder.append(" " + prop + " is not null");
                        break;
                    }
                    case KeyWordConstants.ISNULL: {
                        builder.append(" " + prop + " is null");
                        break;
                    }
                    case KeyWordConstants.NOT: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("not" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("not" + firstCharUpper(prop)).build();
                        info.getParamInfos().add(paramInfo);
                        builder.append(" " + propColumn + "<> #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }
                    case KeyWordConstants.NOTIN: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno(prop + "List").withParamType("List<" + paramShortType + ">").withParamValue(prop + "List").build();
                        info.getImportList().add("java.util.List");
                        info.getParamInfos().add(paramInfo);
                        builder.append(" " + propColumn + " not in \n" + GenCodeUtil.ONE_RETRACT + "<foreach item=\"item\" index=\"index\" collection=\"" + paramInfo.getParamAnno() + "\"\n" + GenCodeUtil.ONE_RETRACT + "" +
                                "open=\"(\" separator=\",\" close=\")\">\n" + GenCodeUtil.ONE_RETRACT + "" +
                                "#{item}\n" + GenCodeUtil.ONE_RETRACT + "" +
                                "</foreach>\n");
                        break;
                    }
                    case KeyWordConstants.IN: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno(prop + "List").withParamType("List<" + paramShortType + ">").withParamValue(prop + "List").build();
                        info.getImportList().add("java.util.List");
                        info.getParamInfos().add(paramInfo);
                        builder.append(" " + propColumn + " in \n" + GenCodeUtil.ONE_RETRACT + "<foreach item=\"item\" index=\"index\" collection=\"" + paramInfo.getParamAnno() + "\"\n" + GenCodeUtil.ONE_RETRACT + "" +
                                "open=\"(\" separator=\",\" close=\")\">\n" + GenCodeUtil.ONE_RETRACT + "" +
                                "#{item}\n" + GenCodeUtil.ONE_RETRACT + "" +
                                "</foreach>\n");
                        break;
                    }
                    case KeyWordConstants.NOTLIKE: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("notlike" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("notlike" + firstCharUpper(prop)).build();
                        info.getParamInfos().add(paramInfo);
                        builder.append(" " + propColumn + "not like #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }
                    case KeyWordConstants.LIKE: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("like" + firstCharUpper(prop)).withParamType(paramShortType).withParamValue("like" + firstCharUpper(prop)).build();
                        info.getParamInfos().add(paramInfo);
                        builder.append(" " + propColumn + "like #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }
                }
            }
            if (connector != null) {
                builder.append(" " + connector);
            }
        }
        info.setSql(info.getSql() + builder.toString());
    }

    private QueryInfo buildWithFind(MethodNameParsedResult result) {
        QueryInfo info = new QueryInfo();
        info.setImportList(new HashSet<>());
        ParsedFind find = result.getParsedFind();
        //shall support with function
        FieldToColumnRelation relation = result.getRelation();
        Map<String, String> fieldMap = result.getFieldMap();
        info.setType(QueryTypeConstants.SELECT);
        boolean queryAllTable = false;
        boolean returnList = true;
        if (find.getFetchProps() != null && find.getFetchProps().size() > 0) {
            if (find.getFetchProps().size() > 1) {
//need to handle when fetch two property let user choose to create class for it.
                //let user to generate class for it.
                //check the return result.
                boolean checkHasFunction = checkHasFunction(find);
                if(checkHasFunction&&find.getGroupByProps()==null){
                    returnList = false;
                }
                GenClassDialog genClassDialog = new GenClassDialog(result.getProject(), find.getFetchProps(), fieldMap, result.getMethodName(), relation,result.getSrcClass());
                boolean b = genClassDialog.showAndGet();
                if(!b){
                    //todo make it here.
                    return null;
                } else{
                    GenClassInfo genClassInfo = genClassDialog.getGenClassInfo();
                    GenClassService.generateClassFileUsingFtl(genClassInfo);
                    VirtualFileManager.getInstance().syncRefresh();
                    PsiDocumentManager manager = PsiDocumentManager.getInstance(result.getProject());
                    //gonna build the resultMap for it.
                    XmlFile mybatisXmlFile = result.getMybatisXmlFile();
                    MyPsiXmlUtils.buildAllColumnMap(result.getProject(),manager.getDocument(mybatisXmlFile),mybatisXmlFile.getRootTag(),
                            manager,genClassDialog.getExtractFieldToColumnRelation(),genClassDialog.getClassQutifiedName());
                    info.setReturnClass(genClassDialog.getClassQutifiedName());
                    info.setReturnMap(genClassDialog.getExtractFieldToColumnRelation().getResultMapId());
                }
            } else {
                //说明等于1
                FetchProp prop = find.getFetchProps().get(0);
                if (prop.getFetchFunction() == null) {
                    String returnClass = fieldMap.get(prop.getFetchProp());
                    info.setReturnClass(returnClass);
                } else {
                    returnList = false;
                    String fetchFunction = prop.getFetchFunction();
                    String returnClass = DbUtils.getReturnClassFromFunction(fieldMap, fetchFunction, prop.getFetchProp());
                    info.setReturnClass(returnClass);
                }
            }
        } else {
            queryAllTable = true;
            info.setReturnMap(relation.getResultMapId());
        }
//later will check wether it is the same with method.
        if (find.getQueryRules() != null) {
            //will build with params.
            for (QueryRule rule : find.getQueryRules()) {
                String prop = rule.getProp();
                //say findById not return list.
                if (prop.toLowerCase().equals("id") && rule.getOperator() == null) {
                    returnList = false;
                }

            }
        }
        if (find.getLimit() == 1) {
            returnList = false;
        }
        if (info.getReturnClass() == null) {
            info.setReturnClass(result.getPsiClassFullName());
        }
        if (returnList) {
            info.setMethodReturnType("List<" + extractLast(info.getReturnClass()) + ">");
            info.getImportList().add("java.util.List");
        } else {
            info.setMethodReturnType(extractLast(info.getReturnClass()));
        }
        info.getImportList().add(info.getReturnClass());
        queryBuilderHandler.handleFindBeforeFromTable(info, result, queryAllTable);
        info.setSql(info.getSql() + "\n" + GenCodeUtil.ONE_RETRACT + " from " + result.getTableName());
        queryBuilderHandler.handleFindAfterFromTable(info, result);
        return info;

    }

    private static boolean checkHasFunction(ParsedFind find) {
        List<FetchProp> fetchProps = find.getFetchProps();
        if(fetchProps==null||fetchProps.size()==0){
            return false;
        }
        for (FetchProp fetchProp : fetchProps) {
            if(fetchProp.getFetchFunction()!=null){
                return true;
            }
        }
        return false;
    }

    private static String extractLast(String returnClass) {
        int s = returnClass.lastIndexOf(".");
        return returnClass.substring(s + 1);
    }
}
