package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.database.DatabaseComponenent;
import com.ccnode.codegenerator.genmethodxml.GenMethodResult;
import com.ccnode.codegenerator.genmethodxml.GenMethodXmlInvoker;
import com.ccnode.codegenerator.log.Log;
import com.ccnode.codegenerator.log.LogFactory;
import com.ccnode.codegenerator.util.MethodNameUtil;
import com.ccnode.codegenerator.util.PsiElementUtil;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by bruce.ge on 2016/12/5.
 */
public class GenerateMethodXmlAction extends PsiElementBaseIntentionAction {

    public static final String GENERATE_DAOXML = "generate daoxml";
    public static final String INSERT_INTO = "insert into";

    private static Log log = LogFactory.getLogger(GenerateMethodXmlAction.class);

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        Stopwatch started = Stopwatch.createStarted();


//        PsiDirectory srcDir = element.getContainingFile().getContainingDirectory();
//        PsiPackage srcPackage = JavaDirectoryService.getInstance().getPackage(srcDir);
        PsiElement parent = element.getParent();

        TextRange textRange = null;
        List<String> methodName = Lists.newArrayList();
        if (parent instanceof PsiMethod) {
            return;
        } else if (parent instanceof PsiJavaCodeReferenceElement) {
            String text = parent.getText();
            methodName.add(text);
            textRange = parent.getTextRange();
//            return;
        } else if (element instanceof PsiWhiteSpace) {
            PsiElement lastMatchedElement = findLastMatchedElement(element);
            String text = lastMatchedElement.getText();
            textRange = lastMatchedElement.getTextRange();
            methodName.add(text);
        }

        GenMethodResult result = new GenMethodXmlInvoker(methodName, project, textRange, element).invoke();
        if (result == null) {
            return;
        }

        if (result.isHasCursor()) {
            CodeInsightUtil.positionCursor(project, result.getCursorFile(), result.getCursorElement());
        }
        StringBuilder methodNameBuilder = new StringBuilder();
        for (String s : methodName) {
            methodNameBuilder.append(s + " ,");
        }
        methodNameBuilder.deleteCharAt(methodNameBuilder.length() - 1);
        long elapsed = started.elapsed(TimeUnit.MILLISECONDS);
        log.info("generate dao xml use with time in mill second is:" + elapsed + " and the method name is:" + methodNameBuilder.toString()
                + " used database is:" + DatabaseComponenent.currentDatabase());
    }


    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        if (!isAvailbleForElement(element)) return false;
        PsiClass containingClass = PsiElementUtil.getContainingClass(element);
        assert containingClass != null;
        PsiElement leftBrace = containingClass.getLBrace();
        if (leftBrace == null) {
            return false;
        }
        if (element instanceof PsiMethod) {
            return false;
        }
        PsiElement parent = element.getParent();
        if (parent instanceof PsiMethod) {
            return false;
        }
        if (element instanceof PsiWhiteSpace) {
            PsiElement element1 = findLastMatchedElement(element);
            if (element1 == null) {
                return false;
            }
            return true;
        }
        if (parent instanceof PsiJavaCodeReferenceElement) {
            PsiJavaCodeReferenceElement referenceElement = (PsiJavaCodeReferenceElement) parent;
            String text = referenceElement.getText().toLowerCase();
            if (MethodNameUtil.checkValidTextStarter(text)) {
                return true;
            }
        }
        return false;
    }

    private PsiElement findLastMatchedElement(PsiElement element) {
        PsiElement prevSibling = element.getPrevSibling();
        while (prevSibling != null && isIgnoreText(prevSibling.getText())) {
            prevSibling = prevSibling.getPrevSibling();
        }
        if (prevSibling != null) {
            String lowerCase = prevSibling.getText().toLowerCase();
            if (MethodNameUtil.checkValidTextStarter(lowerCase)) {
                return prevSibling;
            }
        }
        return null;
    }

    private boolean isIgnoreText(String text) {
        return (text.equals("")) || (text.equals("\n")) || text.equals(" ");
    }

    @NotNull
    @Override
    public String getText() {
        return GENERATE_DAOXML;
    }

    public static boolean isAvailbleForElement(PsiElement psiElement) {
        if (psiElement == null) {
            return false;
        }

        PsiClass containingClass = PsiElementUtil.getContainingClass(psiElement);
        if (containingClass == null) return false;
        Module srcMoudle = ModuleUtilCore.findModuleForPsiElement(containingClass);
        if (srcMoudle == null) return false;
        if (containingClass.isAnnotationType() || containingClass instanceof PsiAnonymousClass || !containingClass.isInterface()) {
            return false;
        }
        return true;
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return GENERATE_DAOXML;
    }
}
