package com.ccnode.codegenerator.view.rename;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Author bruce.ge
 * @Date 2017/2/17
 * @Description
 */
public class RenameResultMapSqlProcessor extends RenamePsiElementProcessor {
    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        if (element instanceof XmlAttributeValue) {
            return true;
        }
        return false;
    }

    @Override
    public void renameElement(PsiElement element, String newName, UsageInfo[] usages, @Nullable RefactoringElementListener listener) throws IncorrectOperationException {
        System.out.println("hello");
    }
}
