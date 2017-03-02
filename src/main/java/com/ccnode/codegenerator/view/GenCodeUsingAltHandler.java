package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.database.ClassValidateResult;
import com.ccnode.codegenerator.database.DatabaseComponenent;
import com.ccnode.codegenerator.dialog.GenCodeDialog;
import com.ccnode.codegenerator.dialog.InsertDialogResult;
import com.ccnode.codegenerator.pojo.ClassInfo;
import com.ccnode.codegenerator.service.pojo.GenerateInsertCodeService;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.generation.OverrideImplementUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/9.
 */
public class GenCodeUsingAltHandler implements CodeInsightActionHandler {

    @Override
    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (project == null) {
            return;
        }
        if (!CodeInsightUtilBase.prepareEditorForWrite(editor)) return;
        if (!FileDocumentManager.getInstance().requestWriting(editor.getDocument(), project)) {
            return;
        }
        final PsiClass currentClass = OverrideImplementUtil.getContextClass(project, editor, file, false);
        ClassValidateResult classValidateResult = DatabaseComponenent.currentHandler().getGenerateFileHandler().validateCurrentClass(currentClass);
        if (!classValidateResult.getValid()) {
            if (classValidateResult.getValid() == null || classValidateResult.getValidFields().size() == 0) {
                Messages.showErrorDialog(project, classValidateResult.getInvalidMessages(), "validate fail");
            } else {
                List<PsiField> validFields1 = classValidateResult.getValidFields();
                StringBuilder validBuilder = new StringBuilder();
                validBuilder.append("\n the following are valid fields: ");
                for (PsiField psiField : validFields1) {
                    validBuilder.append(psiField.getName()+",");
                }
                int i = Messages.showOkCancelDialog(project, classValidateResult.getInvalidMessages() + validBuilder.toString() + "\n\n do you want just use with valid fields?", "some field not valid", null);
                if (i == Messages.CANCEL) {
                    return;
                }
            }
        }
        VirtualFileManager.getInstance().syncRefresh();
        ApplicationManager.getApplication().saveAll();
        GenCodeDialog genCodeDialog = new GenCodeDialog(project, currentClass,classValidateResult.getValidFields());
        boolean b = genCodeDialog.showAndGet();
        if (!b) {
            return;
        } else {
            ClassInfo info = new ClassInfo();
            info.setQualifiedName(currentClass.getQualifiedName());
            info.setName(currentClass.getName());
            switch (genCodeDialog.getType()) {
                case INSERT: {
                    InsertDialogResult insertDialogResult = genCodeDialog.getInsertDialogResult();
                    //build everything on it.
                    insertDialogResult.setSrcClass(info);
                    GenerateInsertCodeService.generateInsert(insertDialogResult);
                    // TODO: 2016/12/26 need to make sure message is call after file refresh
                    VirtualFileManager.getInstance().syncRefresh();
                    Messages.showMessageDialog(project, "generate files success", "success", Messages.getInformationIcon());
                    return;
                }
                case UPDATE: {
                    //do for update.
                }
            }

        }
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }


}
