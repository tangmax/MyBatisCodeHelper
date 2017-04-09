package com.ccnode.codegenerator.methodnameparser.buidler;

import com.ccnode.codegenerator.database.DatabaseComponenent;
import com.ccnode.codegenerator.dialog.ChooseParsedResultToUseDialog;
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
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryBuilder {

    public static QueryParseDto buildFindResult(List<ParsedFind> parsedFinds, List<ParsedFindError> errors, MethodXmlPsiInfo info) {
        QueryParseDto dto = new QueryParseDto();
        if (parsedFinds.size() == 0) {
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

        List<String> strings = Lists.newArrayList();
        ParsedFind choosedFind = null;
        if (parsedFinds.size() > 1) {
            for (int i = 0; i < parsedFinds.size(); i++) {
                strings.add(parsedFinds.get(i).getParsedResult());
            }
            //go get the choosed one.
            ChooseParsedResultToUseDialog chooseParsedResultToUseDialog = new ChooseParsedResultToUseDialog(info.getProject(), strings);
            boolean b = chooseParsedResultToUseDialog.showAndGet();
            if (!b) {
                dto.setHasMatched(false);
                dto.setDisplayErrorMsg(false);
                return dto;
            } else {
                choosedFind = parsedFinds.get(chooseParsedResultToUseDialog.getChoosedIndex());
            }

        } else {
            choosedFind = parsedFinds.get(0);
        }
        QueryInfo e = DatabaseComponenent.currentHandler().getMethodXmlHandler().buildQueryInfoByMethodNameParsedResult(convertFromParsedFind(choosedFind, info));
        //say this is not an method.
        if (e != null) {
            dto.setQueryInfo(e);
            dto.setHasMatched(true);
        } else {
            dto.setHasMatched(false);
            dto.setDisplayErrorMsg(false);
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
        methodNameParsedResult.setSrcClass(info.getSrcClass());
        methodNameParsedResult.setProject(info.getProject());
        methodNameParsedResult.setMybatisXmlFile(info.getMybatisXmlFile());
        methodNameParsedResult.setAllColumnName(info.getAllColumnName());
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
        QueryParseDto dto = new QueryParseDto();
        if (updateList.size() == 0) {
            dto.setHasMatched(false);
            List<String> errorMsgs = new ArrayList<>();
            for (ParsedUpdateError error : errorList) {
                errorMsgs.add(buildErrorMsg(error));
            }
            dto.setErrorMsg(errorMsgs);
            return dto;
        }

        List<String> strings = Lists.newArrayList();
        ParsedUpdate choosedFind = null;
        if (updateList.size() > 1) {
            for (int i = 0; i < updateList.size(); i++) {
                strings.add(updateList.get(i).getParsedResult());
            }
            //go get the choosed one.
            ChooseParsedResultToUseDialog chooseParsedResultToUseDialog = new ChooseParsedResultToUseDialog(info.getProject(), strings);
            boolean b = chooseParsedResultToUseDialog.showAndGet();
            if (!b) {
                dto.setHasMatched(false);
                dto.setDisplayErrorMsg(false);
                return dto;
            } else {
                choosedFind = updateList.get(chooseParsedResultToUseDialog.getChoosedIndex());
            }

        } else {
            choosedFind = updateList.get(0);
        }
        QueryInfo e = DatabaseComponenent.currentHandler().getMethodXmlHandler().buildQueryInfoByMethodNameParsedResult(convertFromParsedUpdate(choosedFind, info));
        //say this is not an method.
        if (e != null) {
            dto.setQueryInfo(e);
            dto.setHasMatched(true);
        } else {
            dto.setHasMatched(false);
            dto.setDisplayErrorMsg(false);
        }
        return dto;
    }


    public static QueryParseDto buildDeleteResult(List<ParsedDelete> parsedDeletes, List<ParsedDeleteError> errors, MethodXmlPsiInfo info) {
        QueryParseDto dto = new QueryParseDto();
        if (parsedDeletes.size() == 0) {
            dto.setHasMatched(false);
            List<String> errorMsgs = new ArrayList<>();
            for (ParsedDeleteError error : errors) {
                errorMsgs.add(buildErrorMsg(error));
            }
            dto.setErrorMsg(errorMsgs);
        }

        List<String> strings = Lists.newArrayList();
        ParsedDelete choosedFind = null;
        if (parsedDeletes.size() > 1) {
            for (int i = 0; i < parsedDeletes.size(); i++) {
                strings.add(parsedDeletes.get(i).getParsedResult());
            }
            //go get the choosed one.
            ChooseParsedResultToUseDialog chooseParsedResultToUseDialog = new ChooseParsedResultToUseDialog(info.getProject(), strings);
            boolean b = chooseParsedResultToUseDialog.showAndGet();
            if (!b) {
                dto.setHasMatched(false);
                dto.setDisplayErrorMsg(false);
                return dto;
            } else {
                choosedFind = parsedDeletes.get(chooseParsedResultToUseDialog.getChoosedIndex());
            }

        } else {
            choosedFind = parsedDeletes.get(0);
        }
        QueryInfo e = DatabaseComponenent.currentHandler().getMethodXmlHandler().buildQueryInfoByMethodNameParsedResult(convertFromParsedDelete(choosedFind, info));
        //say this is not an method.
        if (e != null) {
            dto.setQueryInfo(e);
            dto.setHasMatched(true);
        } else {
            dto.setHasMatched(false);
            dto.setDisplayErrorMsg(false);
        }
        return dto;
    }

    public static QueryParseDto buildCountResult(List<ParsedCount> parsedCounts, List<ParsedCountError> errors, MethodXmlPsiInfo info) {
        QueryParseDto dto = new QueryParseDto();
        if (parsedCounts.size() == 0) {
            dto.setHasMatched(false);
            List<String> errorMsgs = new ArrayList<>();
            for (ParsedCountError error : errors) {
                errorMsgs.add(buildErrorMsg(error));
            }
            dto.setErrorMsg(errorMsgs);
            return dto;
        }
        List<String> strings = Lists.newArrayList();
        ParsedCount choosedFind = null;
        if (parsedCounts.size() > 1) {
            for (int i = 0; i < parsedCounts.size(); i++) {
                strings.add(parsedCounts.get(i).getParsedResult());
            }
            //go get the choosed one.
            ChooseParsedResultToUseDialog chooseParsedResultToUseDialog = new ChooseParsedResultToUseDialog(info.getProject(), strings);
            boolean b = chooseParsedResultToUseDialog.showAndGet();
            if (!b) {
                dto.setHasMatched(false);
                dto.setDisplayErrorMsg(false);
                return dto;
            } else {
                choosedFind = parsedCounts.get(chooseParsedResultToUseDialog.getChoosedIndex());
            }

        } else {
            choosedFind = parsedCounts.get(0);
        }
        QueryInfo e = DatabaseComponenent.currentHandler().getMethodXmlHandler().buildQueryInfoByMethodNameParsedResult(convertFromParsedCount(choosedFind, info));
        //say this is not an method.
        if (e != null) {
            dto.setQueryInfo(e);
            dto.setHasMatched(true);
        } else {
            dto.setHasMatched(false);
            dto.setDisplayErrorMsg(false);
        }
        return dto;

    }

}
