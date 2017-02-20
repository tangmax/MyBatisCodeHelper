package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.dialog.GenCodeDialog;
import com.ccnode.codegenerator.dialog.InsertDialogResult;
import com.ccnode.codegenerator.pojo.ClassInfo;
import com.ccnode.codegenerator.service.pojo.GenerateInsertCodeService;
import com.ccnode.codegenerator.util.valdiate.InvalidField;
import com.ccnode.codegenerator.util.valdiate.PsiClassValidateUtils;
import com.ccnode.codegenerator.util.valdiate.ValidateResult;
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
        ValidateResult validate =
                PsiClassValidateUtils.validate(currentClass);
        if (!validate.getValid()) {
            List<InvalidField> invalidFieldList =
                    validate.getInvalidFieldList();
            StringBuilder errorBuilder = new StringBuilder();
            switch (validate.getInvalidType()) {
                case FIELDERROR:
                    for (InvalidField field : invalidFieldList) {
                        errorBuilder.append("field name is:" + field.getFieldName() + " field Type is:" + field.getType() + " the reason is:" + field.getReason() + "\n");
                    }
                    break;

                case NOFIELD:
                    errorBuilder.append(" there is no available field in current class");
            }
            Messages.showErrorDialog(project, errorBuilder.toString(), "validate fail");
            return;
        }
        VirtualFileManager.getInstance().syncRefresh();
        ApplicationManager.getApplication().saveAll();
        GenCodeDialog genCodeDialog = new GenCodeDialog(project, currentClass);
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
                    List<String> errors = GenerateInsertCodeService.generateInsert(insertDialogResult);
                    if (errors.size() > 0) {
                        String result = "";
                        for (String error : errors) {
                            result += error + "\n";
                        }
                        Messages.showErrorDialog(result, "generate error");
                        return;
                    }
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
