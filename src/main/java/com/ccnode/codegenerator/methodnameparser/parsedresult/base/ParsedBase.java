package com.ccnode.codegenerator.methodnameparser.parsedresult.base;

import com.ccnode.codegenerator.dialog.ParseTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedBase {
    protected List<QueryRule> queryRules;


    protected String parsedResult;

    public String getParsedResult() {
        return parsedResult;
    }

    public void setParsedResult(String parsedResult) {
        this.parsedResult = parsedResult;
    }

    public void addParsePart(ParseTypeEnum parseTypeEnum,String value){
        if(parsedResult==null){
            parsedResult = "| parsedType:"+parseTypeEnum.name()+" parse value:"+value;
        } else{
            parsedResult = parsedResult+"| parsedType:"+parseTypeEnum.name()+" parse value:"+value;
        }
    }

    public void addQueryProp(String queryProp) {
        if (this.queryRules == null) {
            this.queryRules = new ArrayList<>();
        }
        QueryRule rule = new QueryRule();
        rule.setProp(queryProp);
        this.queryRules.add(rule);
    }

    public void addQueryOperator(String operator) {
        QueryRule rule = this.queryRules.get(this.queryRules.size() - 1);
        rule.setOperator(operator);
    }

    public void addConnector(String connector) {
        QueryRule rule = this.queryRules.get(this.queryRules.size() - 1);
        rule.setConnector(connector);
    }

    public List<QueryRule> getQueryRules() {
        return queryRules;
    }
}
