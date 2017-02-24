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

    private List<PsiField> invalidField;

    private Boolean valid;

    public List<PsiField> getValidFields() {
        return validFields;
    }

    public void setValidFields(List<PsiField> validFields) {
        this.validFields = validFields;
    }

    public List<PsiField> getInvalidField() {
        return invalidField;
    }

    public void setInvalidField(List<PsiField> invalidField) {
        this.invalidField = invalidField;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
