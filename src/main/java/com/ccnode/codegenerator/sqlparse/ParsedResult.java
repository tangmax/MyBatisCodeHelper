package com.ccnode.codegenerator.sqlparse;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
public class ParsedResult {
    private String currentState;

    private List<String> recommedValues;

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public List<String> getRecommedValues() {
        return recommedValues;
    }

    public void setRecommedValues(List<String> recommedValues) {
        this.recommedValues = recommedValues;
    }
}
