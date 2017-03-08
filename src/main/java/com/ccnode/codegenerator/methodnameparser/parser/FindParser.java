package com.ccnode.codegenerator.methodnameparser.parser;

import com.ccnode.codegenerator.dialog.ParseTypeEnum;
import com.ccnode.codegenerator.methodnameparser.KeyWordConstants;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFindError;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFind;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFindDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class FindParser extends BaseParser {

    private List<ParsedFind> finds = new ArrayList<>();
    private List<ParsedFindError> errors = new ArrayList<>();

    public FindParser(String methodName, List<String> props) {
        super(methodName, props);
    }

    public ParsedFindDto parse() {
        int state = 0;
        int len = KeyWordConstants.FIND.length();
        ParsedFind parsedFind = new ParsedFind();
        parseMethods(state, methodName, len, parsedFind);
        ParsedFindDto dto = new ParsedFindDto();
        dto.setParsedFinds(finds);
        dto.setParsedFindErrors(errors);
        return dto;
    }

    private void parseMethods(int state, String methodName, int len, ParsedFind parsedFind) {
        if (methodName.length() == len) {
            //means there is no other letter to parse. // need to exit.
            if (isValidEndState(state)) {
                finds.add(parsedFind);
            } else {
                ParsedFindError error = new ParsedFindError();
                error.setParsedFind(parsedFind);
                error.setLastState(state);
                error.setRemaining("");
                errors.add(error);
            }
            return;
        }
        String remaining = methodName.substring(len);
        boolean newParseFind = false;
        //check with state.
        switch (state) {
            case 0: {
                if (remaining.startsWith(KeyWordConstants.DISTINCT)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.setDistinct(true);
                    newFind.addParsePart(ParseTypeEnum.DISTINCT,KeyWordConstants.DISTINCT);
                    parseMethods(1, remaining, KeyWordConstants.DISTINCT.length(), newFind);
                    newParseFind = true;
                }
                if (remaining.startsWith(KeyWordConstants.FIRST)) {
                    if (remaining.length() == KeyWordConstants.FIRST.length()) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.setLimit(1);
                        newFind.addParsePart(ParseTypeEnum.FIRST,KeyWordConstants.FIRST);
                        parseMethods(2, remaining, KeyWordConstants.FIRST.length(), newFind);
                        newParseFind = true;
                    } else {
                        int limitCount = 0;
                        int i;
                        for (i = KeyWordConstants.FIRST.length(); i < remaining.length(); i++) {
                            char c = remaining.charAt(i);
                            if (c >= '0' && c <= '9') {
                                limitCount = limitCount * 10 + (c - '0');
                            } else {
                                break;
                            }
                        }
                        if (limitCount == 0) {
                            limitCount = 1;
                        }
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.setLimit(limitCount);
                        newFind.addParsePart(ParseTypeEnum.FIRST,remaining.substring(0,i));
                        parseMethods(2, remaining, i, newFind);
                        newParseFind = true;
                    }
                }
//                check for props.
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFetchProps(props[i]);
                        newFind.addParsePart(ParseTypeEnum.PROPERTY,props[i]);
                        parseMethods(3, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.ORDERBY,KeyWordConstants.ORDERBY);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }

                if (remaining.startsWith(KeyWordConstants.BY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.BY,KeyWordConstants.BY);
                    parseMethods(8, remaining, KeyWordConstants.BY.length(), newFind);
                    newParseFind = true;
                }

                for (String function : functionOp) {
                    if (remaining.startsWith(function)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFunction(function);
                        newFind.addParsePart(ParseTypeEnum.FUNCTION,function);
                        parseMethods(11, remaining, function.length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }
            case 1: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFetchProps(props[i]);
                        newFind.addParsePart(ParseTypeEnum.PROPERTY,props[i]);
                        parseMethods(3, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }
            case 2: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFetchProps(props[i]);
                        newFind.addParsePart(ParseTypeEnum.PROPERTY,props[i]);
                        parseMethods(3, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }

                if (remaining.startsWith(KeyWordConstants.BY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.BY,KeyWordConstants.BY);
                    parseMethods(8, remaining, KeyWordConstants.BY.length(), newFind);
                    newParseFind = true;
                }
                break;
            }
            case 3: {
                if (remaining.startsWith(KeyWordConstants.AND)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.AND,KeyWordConstants.AND);
                    parseMethods(7, remaining, KeyWordConstants.AND.length(), newFind);
                    newParseFind = true;
                }
                if (remaining.startsWith(KeyWordConstants.BY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.BY,KeyWordConstants.BY);
                    parseMethods(8, remaining, KeyWordConstants.BY.length(), newFind);
                    newParseFind = true;
                }
                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.ORDERBY,KeyWordConstants.ORDERBY);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }
                break;
            }
            case 4: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addOrderByProp(props[i]);
                        newFind.addParsePart(ParseTypeEnum.PROPERTY,props[i]);
                        parseMethods(5, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }
            case 5: {
                for (String orderbyw : order) {
                    if (remaining.startsWith(orderbyw)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addOrderByPropOrder(orderbyw);
                        newFind.addParsePart(ParseTypeEnum.ORDER,orderbyw);
                        parseMethods(6, remaining, orderbyw.length(), newFind);
                        newParseFind = true;
                    }
                }

                if (remaining.startsWith(KeyWordConstants.AND)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.AND,KeyWordConstants.AND);
                    parseMethods(4, remaining, KeyWordConstants.AND.length(), newFind);
                    newParseFind = true;
                }
                break;
            }
            case 6: {
                if (remaining.startsWith(KeyWordConstants.AND)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.AND,KeyWordConstants.AND);
                    parseMethods(4, remaining, KeyWordConstants.AND.length(), newFind);
                    newParseFind = true;
                }
                break;
            }

            case 7: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFetchProps(props[i]);
                        newFind.addParsePart(ParseTypeEnum.PROPERTY,props[i]);
                        parseMethods(3, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }

                for (String function : functionOp) {
                    if (remaining.startsWith(function)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFunction(function);
                        newFind.addParsePart(ParseTypeEnum.FUNCTION,function);
                        parseMethods(11, remaining, function.length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }

            case 8: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addQueryProp(props[i]);
                        newFind.addParsePart(ParseTypeEnum.PROPERTY,props[i]);
                        parseMethods(9, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }

            case 9: {
                for (String comp : compareOp) {
                    if (remaining.startsWith(comp)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addParsePart(ParseTypeEnum.COMPARATOR,comp);
                        newFind.addQueryOperator(comp);
                        parseMethods(10, remaining, comp.length(), newFind);
                        newParseFind = true;
                    }
                }

                for (String link : linkOp) {
                    if (remaining.startsWith(link)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addConnector(link);
                        newFind.addParsePart(ParseTypeEnum.LINKOP,link);
                        parseMethods(8, remaining, link.length(), newFind);
                    }
                }

                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.ORDERBY,KeyWordConstants.ORDERBY);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }
                break;

            }

            case 10: {
                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.addParsePart(ParseTypeEnum.ORDERBY,KeyWordConstants.ORDERBY);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }
                for (String link : linkOp) {
                    if (remaining.startsWith(link)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addConnector(link);
                        newFind.addParsePart(ParseTypeEnum.LINKOP,link);
                        parseMethods(8, remaining, link.length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }

            case 11:{
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFunctionProp(props[i]);
                        newFind.addParsePart(ParseTypeEnum.PROPERTY,props[i]);
                        parseMethods(3, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }
        }

        if (!newParseFind) {
            //means there can find no match for the current info.
            ParsedFindError error = new ParsedFindError();
            error.setParsedFind(parsedFind);
            error.setRemaining(remaining);
            error.setLastState(state);
            //no need for the depth. get the remaining is ok.
            errors.add(error);
        }

    }

    private ParsedFind createParseFind(ParsedFind parsedFind) {
        return parsedFind.clone();
    }

    private boolean isValidEndState(int state) {
        if (state == 0 || state == 2 || state == 3 || state == 5 || state == 6 || state == 9 || state == 10) {
            return true;
        }
        return false;
    }
}