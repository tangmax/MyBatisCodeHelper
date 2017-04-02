package com.ccnode.codegenerator.methodnameparser.parser;

import com.ccnode.codegenerator.dialog.ParseTypeEnum;
import com.ccnode.codegenerator.methodnameparser.KeyWordConstants;
import com.ccnode.codegenerator.methodnameparser.parsedresult.count.ParsedCount;
import com.ccnode.codegenerator.methodnameparser.parsedresult.count.ParsedCountDto;
import com.ccnode.codegenerator.methodnameparser.parsedresult.count.ParsedCountError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class CountParser extends BaseParser {
    private List<ParsedCount> counts = new ArrayList<>();
    private List<ParsedCountError> errors = new ArrayList<>();

    public CountParser(String methodName, List<String> props) {
        super(methodName, props);
    }

    public ParsedCountDto parse() {
        int state = 0;
        int len = KeyWordConstants.COUNT.length();
        ParsedCount count = new ParsedCount();
        parseMethods(state, methodName, len, count);
        ParsedCountDto dto = new ParsedCountDto();
        dto.setParsedCounts(this.counts);
        dto.setErrors(this.errors);
        return dto;
    }

    private void parseMethods(int state, String methodName, int len, ParsedCount count) {
        if (methodName.length() == len) {
            if (isValidEndState(state)) {
                counts.add(count);
            } else {
                ParsedCountError error = new ParsedCountError();
                error.setParsedCount(count);
                error.setLastState(state);
                error.setRemaining("");
                errors.add(error);
            }
            return;
        }

        String remaining = methodName.substring(len);
        boolean newParsedCount = false;

        switch (state) {
            case 0: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedCount clone = count.clone();
                        clone.addFetchProps(props[i]);
                        clone.addParsePart(ParseTypeEnum.PROPERTY, props[i]);
                        parseMethods(1, remaining, props[i].length(), clone);
                        newParsedCount = true;
                    }
                }
                if (remaining.startsWith(KeyWordConstants.DISTINCT)) {
                    ParsedCount clone = count.clone();
                    clone.setDistinct(true);
                    clone.addParsePart(ParseTypeEnum.DISTINCT, KeyWordConstants.DISTINCT);
                    parseMethods(2, remaining, KeyWordConstants.DISTINCT.length(), clone);
                    newParsedCount = true;
                }

                if (remaining.startsWith(KeyWordConstants.BY)) {
                    ParsedCount clone = count.clone();
                    clone.addParsePart(ParseTypeEnum.BY, KeyWordConstants.BY);
                    parseMethods(4, remaining, KeyWordConstants.BY.length(), clone);
                    newParsedCount = true;
                }
                break;
            }

            case 1: {
                if (remaining.startsWith(KeyWordConstants.BY)) {
                    ParsedCount clone = count.clone();
                    clone.addParsePart(ParseTypeEnum.BY, KeyWordConstants.BY);
                    parseMethods(4, remaining, KeyWordConstants.BY.length(), clone);
                    newParsedCount = true;
                }
                break;
            }

            case 2: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedCount clone = count.clone();
                        clone.addFetchProps(props[i]);
                        clone.addParsePart(ParseTypeEnum.PROPERTY, props[i]);
                        parseMethods(3, remaining, props[i].length(), clone);
                        newParsedCount = true;
                    }
                }
                break;
            }

            case 3: {
                if (remaining.startsWith(KeyWordConstants.AND)) {
                    ParsedCount clone = count.clone();
                    clone.addParsePart(ParseTypeEnum.AND, KeyWordConstants.AND);
                    parseMethods(2, remaining, KeyWordConstants.AND.length(), clone);
                    newParsedCount = true;
                }

                if (remaining.startsWith(KeyWordConstants.BY)) {
                    ParsedCount clone = count.clone();
                    clone.addParsePart(ParseTypeEnum.BY, KeyWordConstants.BY);
                    parseMethods(4, remaining, KeyWordConstants.BY.length(), clone);
                    newParsedCount = true;
                }
                break;
            }

            case 4: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedCount clone = count.clone();
                        clone.addQueryProp(props[i]);
                        clone.addParsePart(ParseTypeEnum.PROPERTY, props[i]);
                        parseMethods(5, remaining, props[i].length(), clone);
                        newParsedCount = true;
                    }
                }
                break;
            }

            case 5: {
                for (String link : linkOp) {
                    if (remaining.startsWith(link)) {
                        ParsedCount clone = count.clone();
                        clone.addConnector(link);
                        clone.addParsePart(ParseTypeEnum.LINKOP, link);
                        parseMethods(4, remaining, link.length(), clone);
                        newParsedCount = true;
                    }
                }
                for (String comp : compareOp) {
                    if (remaining.startsWith(comp)) {
                        ParsedCount clone = count.clone();
                        clone.addQueryOperator(comp);
                        clone.addParsePart(ParseTypeEnum.COMPARATOR, comp);
                        parseMethods(6, remaining, comp.length(), clone);
                        newParsedCount = true;
                    }
                }
                break;
            }

            case 6: {
                for (String link : linkOp) {
                    if (remaining.startsWith(link)) {
                        ParsedCount clone = count.clone();
                        clone.addConnector(link);
                        clone.addParsePart(ParseTypeEnum.LINKOP, link);
                        parseMethods(4, remaining, link.length(), clone);
                        newParsedCount = true;
                    }
                }
                break;
            }
        }
        if (!newParsedCount) {
            ParsedCountError error = new ParsedCountError();
            error.setParsedCount(count);
            error.setRemaining(remaining);
            error.setLastState(state);
            errors.add(error);
        }
    }

    private boolean isValidEndState(int state) {
        if (state == 0 || state == 1 || state == 3 || state == 5 || state == 6) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String methodName = "count";
        List<String> props = new ArrayList<>();
        props.add("id");
        props.add("name");
        props.add("username");
        ParsedCountDto parse = new CountParser(methodName.toLowerCase(), props).parse();
        parse.getParsedCounts();
    }
}
