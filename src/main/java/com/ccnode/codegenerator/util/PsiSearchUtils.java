package com.ccnode.codegenerator.util;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiNonJavaFileReferenceProcessor;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/11
 * @Description
 */
public class PsiSearchUtils {
    @NotNull
    public static List<XmlFile> searchMapperXml(@NotNull Project project, Module moduleForPsiElement, final String srcClassQualifiedName) {
        if(moduleForPsiElement==null){
            return new ArrayList<>();
        }
        PsiSearchHelper searchService = ServiceManager.getService(project, PsiSearchHelper.class);
        List<XmlFile> xmlFiles = new ArrayList<XmlFile>();
        searchService.processUsagesInNonJavaFiles("mapper", new PsiNonJavaFileReferenceProcessor() {
            @Override
            public boolean process(PsiFile file, int startOffset, int endOffset) {
                if (file instanceof XmlFile) {
                    XmlFile xmlFile = (XmlFile) file;
                    if (xmlFile.getRootTag() != null) {
                        XmlAttribute namespace = xmlFile.getRootTag().getAttribute("namespace");
                        if (namespace != null && namespace.getValue().equals(srcClassQualifiedName)) {
                            xmlFiles.add(xmlFile);
                            return false;
                        }
                    }
                }
                return true;
            }
        }, GlobalSearchScope.moduleScope(moduleForPsiElement));
        return xmlFiles;
    }
}
