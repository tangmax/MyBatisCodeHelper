package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.database.DatabaseComponenent;
import com.ccnode.codegenerator.genmethodxml.GenMethodResult;
import com.ccnode.codegenerator.genmethodxml.GenMethodXmlInvoker;
import com.ccnode.codegenerator.log.Log;
import com.ccnode.codegenerator.log.LogFactory;
import com.ccnode.codegenerator.util.IconUtils;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author bruce.ge
 * @Date 2017/3/5
 * @Description
 */
public class MultipleMethodGenerateAction extends AnAction {

    private static Log log = LogFactory.getLogger(MultipleMethodGenerateAction.class);

    public MultipleMethodGenerateAction() {
        super(null, null, IconUtils.useMyBatisIcon());
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Stopwatch started = Stopwatch.createStarted();
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Caret currentCaret =
                editor.getCaretModel().getCurrentCaret();
        if (currentCaret.getSelectionStart() == currentCaret.getSelectionEnd()) {
            return;
        }
        Document document =
                editor.getDocument();
        //go remove them.

        String selectedText = currentCaret.getSelectedText();
        List<String> methodNames = extractMethodNames(selectedText);
        if (methodNames.isEmpty()) {
            return;
        }
        PsiFile data = e.getData(PlatformDataKeys.PSI_FILE);
        GenMethodXmlInvoker genMethodXmlInvoker = new GenMethodXmlInvoker(methodNames, editor.getProject(), new TextRange(currentCaret.getSelectionStart(), currentCaret.getSelectionEnd()), data);
        GenMethodResult result =
                genMethodXmlInvoker.invoke();
        if (result == null) {
            return;
        }
        if (result.isHasCursor()) {
            CodeInsightUtil.positionCursor(editor.getProject(), result.getCursorFile(), result.getCursorElement());
        }
        StringBuilder methodNameBuilder = new StringBuilder();
        for (String s : methodNames) {
            methodNameBuilder.append(s + " ,");
        }
        methodNameBuilder.deleteCharAt(methodNameBuilder.length() - 1);
        long elapsed = started.elapsed(TimeUnit.MILLISECONDS);
        log.info("generate dao multiple xml use with time in mill second is:" + elapsed + " and the method name is:" + methodNameBuilder.toString()
                + " used database is:" + DatabaseComponenent.currentDatabase());
    }

    @NotNull
    private static List<String> extractMethodNames(String selectedText) {
        List<String> methodList = Lists.newArrayList();
        String mm = "";
        for (int i = 0; i < selectedText.length(); i++) {
            char c = selectedText.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                mm += c;
            } else {
                if (mm.length() > 0) {
                    methodList.add(mm);
                }
                mm = "";
            }
        }
        if (StringUtils.isNotBlank(mm)) {
            methodList.add(mm);
        }
        return methodList;
    }

    //only in mybatis interface it can work.
    @Override
    public void update(AnActionEvent e) {
        PsiFile data = e.getData(PlatformDataKeys.PSI_FILE);
        if (data == null || !data.isWritable() || !(data instanceof PsiJavaFile)) {
            e.getPresentation().setVisible(false);
            return;
        }
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        String selectedText = editor.getCaretModel().getCurrentCaret().getSelectedText();
        if (StringUtils.isBlank(selectedText)) {
            e.getPresentation().setVisible(false);
        }
    }
}
