package com.ccnode.codegenerator.dialog.datatype;

import com.intellij.psi.PsiField;

/**
 * Created by bruce.ge on 2016/12/25.
 */
public class ClassFieldInfo {
    private String fieldName;

//    the field type is Object type will convert int -> java.lang.Integer
    private String fieldType;

    private PsiField psiField;


    public PsiField getPsiField() {
        return psiField;
    }

    public void setPsiField(PsiField psiField) {
        this.psiField = psiField;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
