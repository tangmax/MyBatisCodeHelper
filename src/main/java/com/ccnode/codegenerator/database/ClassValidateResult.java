package com.ccnode.codegenerator.database;

import com.intellij.psi.PsiField;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/23
 * @Description
 */
public class ClassValidateResult {
    private List<PsiField> validFields;

    private Boolean valid;

    /*the messages display to user when there are invalid fields*/
    private String invalidMessages;


    public String getInvalidMessages() {
        return invalidMessages;
    }

    public void setInvalidMessages(String invalidMessages) {
        this.invalidMessages = invalidMessages;
    }

    public List<PsiField> getValidFields() {
        return validFields;
    }

    public void setValidFields(List<PsiField> validFields) {
        this.validFields = validFields;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
