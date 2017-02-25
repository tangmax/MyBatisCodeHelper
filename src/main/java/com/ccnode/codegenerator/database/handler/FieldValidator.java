package com.ccnode.codegenerator.database.handler;

import com.intellij.psi.PsiField;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
public interface FieldValidator {
    boolean isValidField(PsiField psiField);
}
