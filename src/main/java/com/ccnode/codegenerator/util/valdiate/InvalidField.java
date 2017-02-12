package com.ccnode.codegenerator.util.valdiate;

/**
 * @Author bruce.ge
 * @Date 2017/2/12
 * @Description
 */
public class InvalidField {
    private String fieldName;

    private String type;

    private String reason;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
