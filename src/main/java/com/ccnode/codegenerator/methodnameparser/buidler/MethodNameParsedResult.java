package com.ccnode.codegenerator.methodnameparser.buidler;

import com.ccnode.codegenerator.methodnameparser.parsedresult.count.ParsedCount;
import com.ccnode.codegenerator.methodnameparser.parsedresult.delete.ParsedDelete;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFind;
import com.ccnode.codegenerator.methodnameparser.parsedresult.update.ParsedUpdate;
import com.ccnode.codegenerator.pojo.FieldToColumnRelation;

import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
/*the medial result to get the final xml info*/
public class MethodNameParsedResult {
    private ParsedTypeEnum parsedType;

    private String methodName;

    private FieldToColumnRelation relation;

    private String tableName;

    private Map<String, String> fieldMap;

    private String psiClassName;

    private ParsedFind parsedFind;

    private ParsedUpdate parsedUpdate;

    private ParsedDelete parsedDelete;

    private ParsedCount parsedCount;

    public ParsedTypeEnum getParsedType() {
        return parsedType;
    }

    public void setParsedType(ParsedTypeEnum parsedType) {
        this.parsedType = parsedType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public FieldToColumnRelation getRelation() {
        return relation;
    }

    public void setRelation(FieldToColumnRelation relation) {
        this.relation = relation;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public String getPsiClassName() {
        return psiClassName;
    }

    public void setPsiClassName(String psiClassName) {
        this.psiClassName = psiClassName;
    }

    public ParsedFind getParsedFind() {
        return parsedFind;
    }

    public void setParsedFind(ParsedFind parsedFind) {
        this.parsedFind = parsedFind;
    }

    public ParsedUpdate getParsedUpdate() {
        return parsedUpdate;
    }

    public void setParsedUpdate(ParsedUpdate parsedUpdate) {
        this.parsedUpdate = parsedUpdate;
    }

    public ParsedDelete getParsedDelete() {
        return parsedDelete;
    }

    public void setParsedDelete(ParsedDelete parsedDelete) {
        this.parsedDelete = parsedDelete;
    }

    public ParsedCount getParsedCount() {
        return parsedCount;
    }

    public void setParsedCount(ParsedCount parsedCount) {
        this.parsedCount = parsedCount;
    }
}
