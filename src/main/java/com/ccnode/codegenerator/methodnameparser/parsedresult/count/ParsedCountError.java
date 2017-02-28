package com.ccnode.codegenerator.methodnameparser.parsedresult.count;

import com.ccnode.codegenerator.methodnameparser.parsedresult.base.ParsedErrorBase;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedCountError extends ParsedErrorBase {
    private ParsedCount parsedCount;

    public ParsedCount getParsedCount() {
        return parsedCount;
    }

    public void setParsedCount(ParsedCount parsedCount) {
        this.parsedCount = parsedCount;
    }
}
