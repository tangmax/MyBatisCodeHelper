package com.ccnode.codegenerator.methodnameparser.parsedresult.find;

import com.ccnode.codegenerator.methodnameparser.parsedresult.base.ParsedErrorBase;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedFindError extends ParsedErrorBase{
    private ParsedFind parsedFind;

    public ParsedFind getParsedFind() {
        return parsedFind;
    }

    public void setParsedFind(ParsedFind parsedFind) {
        this.parsedFind = parsedFind;
    }


}
