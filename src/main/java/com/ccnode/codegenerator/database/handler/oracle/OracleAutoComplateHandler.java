package com.ccnode.codegenerator.database.handler.oracle;

import com.ccnode.codegenerator.database.handler.AutoCompleteHandler;
import com.ccnode.codegenerator.util.PsiClassUtil;
import com.intellij.psi.PsiParameter;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class OracleAutoComplateHandler implements AutoCompleteHandler {
    private static volatile OracleAutoComplateHandler mInstance;

    private OracleAutoComplateHandler() {
    }

    public static OracleAutoComplateHandler getInstance() {
        if (mInstance == null) {
            synchronized (OracleAutoComplateHandler.class) {
                if (mInstance == null) {
                    mInstance = new OracleAutoComplateHandler();
                }
            }
        }
        return mInstance;
    }

    @Override
    public boolean isSupportedParam(PsiParameter psiParameter) {
        return OracleHandlerUtils.getTypePropByQulitifiedName(PsiClassUtil.convertToObjectText(psiParameter.getType().getCanonicalText()))!=null;
    }

}
