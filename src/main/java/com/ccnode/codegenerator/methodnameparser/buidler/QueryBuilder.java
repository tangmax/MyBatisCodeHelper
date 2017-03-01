package com.ccnode.codegenerator.methodnameparser.buidler;

import com.ccnode.codegenerator.database.DatabaseComponenent;
import com.ccnode.codegenerator.methodnameparser.QueryParseDto;
import com.ccnode.codegenerator.methodnameparser.parsedresult.base.ParsedErrorBase;
import com.ccnode.codegenerator.methodnameparser.parsedresult.count.ParsedCount;
import com.ccnode.codegenerator.methodnameparser.parsedresult.count.ParsedCountError;
import com.ccnode.codegenerator.methodnameparser.parsedresult.delete.ParsedDelete;
import com.ccnode.codegenerator.methodnameparser.parsedresult.delete.ParsedDeleteError;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFind;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFindError;
import com.ccnode.codegenerator.methodnameparser.parsedresult.update.ParsedUpdate;
import com.ccnode.codegenerator.methodnameparser.parsedresult.update.ParsedUpdateError;
import com.ccnode.codegenerator.pojo.MethodXmlPsiInfo;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryBuilder {

    public static QueryParseDto buildFindResult(List<ParsedFind> parsedFinds, List<ParsedFindError> errors, MethodXmlPsiInfo info) {
        if (parsedFinds.size() == 0) {
            QueryParseDto dto = new QueryParseDto();
            dto.setHasMatched(false);
            List<String> errorMsgs = new ArrayList<>();
            for (ParsedFindError error : errors) {
                String errorMsg = buildErrorMsg(error);
                errorMsgs.add(errorMsg);
            }
            dto.setErrorMsg(errorMsgs);
            return dto;
        }
        //get pojo class all fields and their type do it cool.

        List<QueryInfo> queryInfos = new ArrayList<>();
        for (ParsedFind find : parsedFinds) {
            queryInfos.add(DatabaseComponenent.currentHandler().getMethodXmlHandler().buildQueryInfoByMethodNameParsedResult(convertFromParsedFind(find, info)));
        }
        //say this is not an method.
        QueryParseDto dto = new QueryParseDto();
        dto.setQueryInfos(queryInfos);
        if (queryInfos.size() > 0) {
            dto.setHasMatched(true);
        }
        return dto;
    }

    private static MethodNameParsedResult convertFromParsedFind(ParsedFind find, MethodXmlPsiInfo info) {
        MethodNameParsedResult parsedResult = convertFromInfo(info);
        parsedResult.setParsedType(ParsedTypeEnum.FIND);
        parsedResult.setParsedFind(find);
        return parsedResult;
    }


    private static MethodNameParsedResult convertFromParsedUpdate(ParsedUpdate update, MethodXmlPsiInfo info) {
        MethodNameParsedResult parsedResult = convertFromInfo(info);
        parsedResult.setParsedType(ParsedTypeEnum.UPDATE);
        parsedResult.setParsedUpdate(update);
        return parsedResult;
    }

    private static MethodNameParsedResult convertFromParsedDelete(ParsedDelete delete, MethodXmlPsiInfo info) {
        MethodNameParsedResult parsedResult = convertFromInfo(info);
        parsedResult.setParsedType(ParsedTypeEnum.DELETE);
        parsedResult.setParsedDelete(delete);
        return parsedResult;
    }

    private static MethodNameParsedResult convertFromParsedCount(ParsedCount count, MethodXmlPsiInfo info) {
        MethodNameParsedResult parsedResult = convertFromInfo(info);
        parsedResult.setParsedType(ParsedTypeEnum.COUNT);
        parsedResult.setParsedCount(count);
        return parsedResult;
    }

    private static MethodNameParsedResult convertFromInfo(MethodXmlPsiInfo info) {
        MethodNameParsedResult methodNameParsedResult = new MethodNameParsedResult();
        methodNameParsedResult.setMethodName(info.getMethodName());
        methodNameParsedResult.setRelation(info.getRelation());
        methodNameParsedResult.setTableName(info.getTableName());
        methodNameParsedResult.setFieldMap(info.getFieldMap());
        methodNameParsedResult.setPsiClassName(info.getPsiClassName());
        methodNameParsedResult.setPsiClassFullName(info.getPsiClassFullName());
        return methodNameParsedResult;
    }

    private static String buildErrorMsg(ParsedErrorBase error) {
        if (StringUtils.isEmpty(error.getRemaining())) {
            return "the query not end legal";
        } else {
            return "the remaining " + error.getRemaining() + " can't be parsed";
        }
    }


    public static QueryParseDto buildUpdateResult(List<ParsedUpdate> updateList, List<ParsedUpdateError> errorList, MethodXmlPsiInfo info) {
        if (updateList.size() == 0) {
            QueryParseDto dto = new QueryParseDto();
            dto.setHasMatched(false);
            List<String> errorMsgs = new ArrayList<>();
            for (ParsedUpdateError error : errorList) {
                errorMsgs.add(buildErrorMsg(error));
            }
            dto.setErrorMsg(errorMsgs);
            return dto;
        }

        List<QueryInfo> queryInfos = new ArrayList<>();
        for (ParsedUpdate update : updateList) {
            queryInfos.add(DatabaseComponenent.currentHandler().getMethodXmlHandler().buildQueryInfoByMethodNameParsedResult(convertFromParsedUpdate(update, info)));
        }
        QueryParseDto dto = new QueryParseDto();
        dto.setQueryInfos(queryInfos);
        dto.setHasMatched(true);
        return dto;
    }


    public static QueryParseDto buildDeleteResult(List<ParsedDelete> parsedDeletes, List<ParsedDeleteError> errors, MethodXmlPsiInfo info) {
        if (parsedDeletes.size() == 0) {
            QueryParseDto dto = new QueryParseDto();
            dto.setHasMatched(false);
            List<String> errorMsgs = new ArrayList<>();
            for (ParsedDeleteError error : errors) {
                errorMsgs.add(buildErrorMsg(error));
            }
            dto.setErrorMsg(errorMsgs);
        }

        List<QueryInfo> queryInfos = new ArrayList<>();
        for (ParsedDelete delete : parsedDeletes) {
            queryInfos.add(DatabaseComponenent.currentHandler().getMethodXmlHandler().buildQueryInfoByMethodNameParsedResult(convertFromParsedDelete(delete, info)));
        }
        QueryParseDto dto = new QueryParseDto();
        dto.setQueryInfos(queryInfos);
        dto.setHasMatched(true);
        return dto;

    }

    public static QueryParseDto buildCountResult(List<ParsedCount> parsedCounts, List<ParsedCountError> errors, MethodXmlPsiInfo info) {
        if (parsedCounts.size() == 0) {
            QueryParseDto dto = new QueryParseDto();
            dto.setHasMatched(false);
            List<String> errorMsgs = new ArrayList<>();
            for (ParsedCountError error : errors) {
                errorMsgs.add(buildErrorMsg(error));
            }
            dto.setErrorMsg(errorMsgs);
            return dto;
        }
        List<QueryInfo> queryInfos = new ArrayList<>();
        for (ParsedCount count : parsedCounts) {
            queryInfos.add(DatabaseComponenent.currentHandler().getMethodXmlHandler().buildQueryInfoByMethodNameParsedResult(convertFromParsedCount(count, info)));
        }
        QueryParseDto dto = new QueryParseDto();
        dto.setQueryInfos(queryInfos);
        dto.setHasMatched(true);
        return dto;

    }

}
