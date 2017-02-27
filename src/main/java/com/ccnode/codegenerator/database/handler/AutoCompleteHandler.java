package com.ccnode.codegenerator.database.handler;

import com.intellij.psi.PsiParameter;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public interface AutoCompleteHandler {

    boolean isSupportedParam(PsiParameter psiParameter);
}
