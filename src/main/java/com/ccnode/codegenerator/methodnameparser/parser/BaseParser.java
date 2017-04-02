package com.ccnode.codegenerator.methodnameparser.parser;

import com.ccnode.codegenerator.methodnameparser.KeyWordConstants;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class BaseParser {

    protected String methodName;

    protected String[] props;

    protected String[] lowerProps;


    protected static String[] linkOp = {KeyWordConstants.AND, KeyWordConstants.OR};

    protected static String[] compareOp = {KeyWordConstants.BETWEEN, KeyWordConstants.GREATERTHAN, KeyWordConstants.GREATERTHANOREQUALTO,
            KeyWordConstants.LESSTHANOREQUALTO, KeyWordConstants.LESSTHAN, KeyWordConstants.BETWEENOREQUALTO,
            KeyWordConstants.ISNOTNULL, KeyWordConstants.ISNULL, KeyWordConstants.NOTNULL, KeyWordConstants.NOTLIKE, KeyWordConstants.LIKE
            , KeyWordConstants.NOTIN, KeyWordConstants.NOT, KeyWordConstants.IN,KeyWordConstants.STARTING_WITH,KeyWordConstants.ENDING_WTIH
            ,KeyWordConstants.BEFORE,KeyWordConstants.AFTER,KeyWordConstants.CONTAINING};

    protected static String[] functionOp = {KeyWordConstants.MAX,KeyWordConstants.MIN,KeyWordConstants.AVG,KeyWordConstants.SUM};

    protected static String[] order = {KeyWordConstants.ASC, KeyWordConstants.DESC};


    public BaseParser(String methodName, List<String> props) {
        this.methodName = methodName;
        this.props = new String[props.size()];
        this.lowerProps = new String[props.size()];
        for (int i = 0; i < props.size(); i++) {
            this.props[i] = props.get(i);
            this.lowerProps[i] = props.get(i).toLowerCase();
        }
    }
}
