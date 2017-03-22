package com.ccnode.codegenerator.view.inspection;

import com.ccnode.codegenerator.util.MyPsiXmlUtils;
import com.ccnode.codegenerator.util.PsiSearchUtils;
import com.intellij.codeInspection.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/3/22
 * @Description
 */
public class MybatisMethodNotFindInXmlInspection extends BaseJavaLocalInspectionTool {

    private LocalQuickFix fix = new MyBatisMethodNotFoundFix();

    @Nullable
    @Override
    public ProblemDescriptor[] checkMethod(@NotNull PsiMethod method, @NotNull InspectionManager manager, boolean isOnTheFly) {
        return new ProblemDescriptor[]{manager.createProblemDescriptor(method, "method not find in xml", fix, ProblemHighlightType.ERROR
                , true)};
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                PsiClass currentClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
                if (currentClass == null||!currentClass.isInterface()) {
                    return;
                }
                //check if current class is mybatis file.
                String qualifiedName = currentClass.getQualifiedName();
                //search it in xml file.
                Module moduleForPsiElement =
                        ModuleUtilCore.findModuleForPsiElement(method);
                List<XmlFile> xmlFiles = PsiSearchUtils.searchMapperXml(holder.getProject(), moduleForPsiElement, qualifiedName);
                if(xmlFiles.size()==0||xmlFiles.size()>1){
                    return;
                } else {
                    XmlFile xmlFile = xmlFiles.get(0);
                    PsiElement tagForMethodName = MyPsiXmlUtils.findTagForMethodName(xmlFile, method.getName());
                    if(tagForMethodName==null){
                        holder.registerProblem(method, "mybatis xml not exist", ProblemHighlightType.ERROR, fix);
                    }
                }
            }
        };
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "check mybatis xml";
    }
}
