package com.ccnode.codegenerator.methodnameparser.parsedresult.delete;

import com.ccnode.codegenerator.methodnameparser.parsedresult.base.ParsedBase;
import com.rits.cloning.Cloner;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedDelete extends ParsedBase {
    public ParsedDelete clone() {
        return Cloner.standard().deepClone(this);
    }
}
