package com.ccnode.codegenerator.methodnameparser.parsedresult.update;

import com.ccnode.codegenerator.methodnameparser.parsedresult.base.ParsedErrorBase;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedUpdateError extends ParsedErrorBase {
    private ParsedUpdate parsedUpdate;

    public ParsedUpdate getParsedUpdate() {
        return parsedUpdate;
    }

    public void setParsedUpdate(ParsedUpdate parsedUpdate) {
        this.parsedUpdate = parsedUpdate;
    }
}
