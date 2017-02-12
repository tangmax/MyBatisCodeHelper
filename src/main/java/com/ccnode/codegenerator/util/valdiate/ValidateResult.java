package com.ccnode.codegenerator.util.valdiate;

import com.ccnode.codegenerator.dialog.datatype.ClassFieldInfo;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/12
 * @Description
 */
public class ValidateResult {
    private Boolean valid;

    private InvalidType invalidType;


    public InvalidType getInvalidType() {
        return invalidType;
    }

    public void setInvalidType(InvalidType invalidType) {
        this.invalidType = invalidType;
    }

    private List<InvalidField> invalidFieldList;

    private List<ClassFieldInfo> validFields;

    public List<ClassFieldInfo> getValidFields() {
        return validFields;
    }

    public void setValidFields(List<ClassFieldInfo> validFields) {
        this.validFields = validFields;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public List<InvalidField> getInvalidFieldList() {
        return invalidFieldList;
    }

    public void setInvalidFieldList(List<InvalidField> invalidFieldList) {
        this.invalidFieldList = invalidFieldList;
    }

    public enum InvalidType{
        NOFIELD,FIELDERROR
    }
}
