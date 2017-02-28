package com.ccnode.codegenerator.database.handler.mysql;

import com.ccnode.codegenerator.database.handler.AutoCompleteHandler;
import com.intellij.psi.PsiParameter;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class MysqlAutoCompleteHandler implements AutoCompleteHandler {

    private static volatile MysqlAutoCompleteHandler mInstance;

    private MysqlAutoCompleteHandler() {
    }

    public static MysqlAutoCompleteHandler getInstance() {
        if (mInstance == null) {
            synchronized (MysqlAutoCompleteHandler.class) {
                if (mInstance == null) {
                    mInstance = new MysqlAutoCompleteHandler();
                }
            }
        }
        return mInstance;
    }

    @Override
    public boolean isSupportedParam(PsiParameter psiParameter) {
        return MysqlHandlerUtils.getTypePropsByQulifiType(psiParameter.getType().getCanonicalText()) != null;
    }
}
