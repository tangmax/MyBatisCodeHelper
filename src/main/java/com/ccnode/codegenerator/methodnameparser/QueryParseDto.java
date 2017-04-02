package com.ccnode.codegenerator.methodnameparser;

import com.ccnode.codegenerator.methodnameparser.buidler.QueryInfo;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryParseDto {
    private QueryInfo queryInfo;

    private Boolean hasMatched = false;

    private Boolean displayErrorMsg = true;

    private List<String> errorMsg;

    public Boolean getDisplayErrorMsg() {
        return displayErrorMsg;
    }

    public void setDisplayErrorMsg(Boolean displayErrorMsg) {
        this.displayErrorMsg = displayErrorMsg;
    }

    public QueryInfo getQueryInfo() {
        return queryInfo;
    }

    public void setQueryInfo(QueryInfo queryInfo) {
        this.queryInfo = queryInfo;
    }

    public Boolean getHasMatched() {
        return hasMatched;
    }

    public void setHasMatched(Boolean hasMatched) {
        this.hasMatched = hasMatched;
    }

    public List<String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(List<String> errorMsg) {
        this.errorMsg = errorMsg;
    }
}
