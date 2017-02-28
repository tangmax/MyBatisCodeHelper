package com.ccnode.codegenerator.methodnameparser.buidler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryInfo {
    private String returnClass;

    private List<ParamInfo> paramInfos;

    private String methodReturnType;

    private String sql;

    private String id;

    private String returnMap;

    private String type;

    private Set<String> importList;

    public Set<String> getImportList() {
        return importList;
    }

    public void setImportList(Set<String> importList) {
        this.importList = importList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
    }

    public String getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass;
    }

    public List<ParamInfo> getParamInfos() {
        return paramInfos;
    }

    public void setParamInfos(List<ParamInfo> paramInfos) {
        this.paramInfos = paramInfos;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReturnMap() {
        return returnMap;
    }

    public void setReturnMap(String returnMap) {
        this.returnMap = returnMap;
    }

    public void addParams(ParamInfo info) {
        if (paramInfos == null) {
            paramInfos = new ArrayList<>();
        }
        paramInfos.add(info);
    }
}
