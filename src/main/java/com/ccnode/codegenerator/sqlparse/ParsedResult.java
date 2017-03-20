package com.ccnode.codegenerator.sqlparse;

import com.intellij.codeInsight.lookup.LookupElement;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
public class ParsedResult {
    private String currentState;

    private List<LookupElement> recommedValues;

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public List<LookupElement> getRecommedValues() {
        return recommedValues;
    }

    public void setRecommedValues(List<LookupElement> recommedValues) {
        this.recommedValues = recommedValues;
    }
}
