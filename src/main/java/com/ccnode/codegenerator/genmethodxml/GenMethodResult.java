package com.ccnode.codegenerator.genmethodxml;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @Author bruce.ge
 * @Date 2017/3/5
 * @Description
 */
public class GenMethodResult {
    private PsiElement cursorElement;

    private boolean hasCursor;

    private PsiFile cursorFile;


    public boolean isHasCursor() {
        return hasCursor;
    }

    public void setHasCursor(boolean hasCursor) {
        this.hasCursor = hasCursor;
    }

    public PsiFile getCursorFile() {
        return cursorFile;
    }

    public void setCursorFile(PsiFile cursorFile) {
        this.cursorFile = cursorFile;
    }

    public PsiElement getCursorElement() {
        return cursorElement;
    }

    public void setCursorElement(PsiElement cursorElement) {
        this.cursorElement = cursorElement;
    }
}
