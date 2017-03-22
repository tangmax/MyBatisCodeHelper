package com.ccnode.codegenerator.view.inspection;

import com.ccnode.codegenerator.constants.MyBatisXmlConstants;
import com.ccnode.codegenerator.util.PsiClassUtil;
import com.ccnode.codegenerator.util.PsiDocumentUtils;
import com.ccnode.codegenerator.util.PsiSearchUtils;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/3/22
 * @Description
 */
public class MyBatisMethodNotFoundFix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "create mybatis xml";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiElement psiElement =
                descriptor.getPsiElement();
        if (!(psiElement instanceof PsiMethod)) {
            return;
        }
        PsiMethod method = (PsiMethod) psiElement;
        PsiClass currentClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        String qualifiedName = currentClass.getQualifiedName();
        Module moduleForPsiElement =
                ModuleUtilCore.findModuleForPsiElement(method);
        List<XmlFile> xmlFiles = PsiSearchUtils.searchMapperXml(project, moduleForPsiElement, qualifiedName);
        if (xmlFiles.size() != 1) {
            return;
        }
        XmlFile thexml = xmlFiles.get(0);
        XmlTag rootTag = thexml.getRootTag();
        if (rootTag == null) {
            return;
        }
        //get the insert Text place.
        //generate the inserted text.
        rootTag.addSubTag(generateTag(rootTag, method), false);
        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        Document document = manager.getDocument(thexml);
        PsiDocumentUtils.commitAndSaveDocument(manager, document);
        XmlTag[] subTags = rootTag.getSubTags();
        XmlTag subTag = subTags[subTags.length - 1];
        OpenFileDescriptor fileDescriptor = new OpenFileDescriptor(project, thexml.getVirtualFile(), subTag.getValue().getTextRange().getStartOffset()+1);
        FileEditorManager.getInstance(project).openTextEditor(fileDescriptor, true);
    }

    private XmlTag generateTag(XmlTag rootTag, PsiMethod method) {
        XmlTag childTag = rootTag.createChildTag("select", "", "\n\n", false);
        childTag.setAttribute("id", method.getName());
        String value = PsiClassUtil.extractRealType(method.getReturnType());
        if (value != null) {
            childTag.setAttribute(MyBatisXmlConstants.RESULT_TYPE,
                    value);
        }
        return childTag;
    }
}
