package com.ccnode.codegenerator.database.handler;

import com.ccnode.codegenerator.database.ClassValidateResult;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public interface GenerateFileHandler extends JTableRecommendHandler{
    @NotNull
    String generateSql(List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName);

    @NotNull
    ClassValidateResult validateCurrentClass(PsiClass psiClass);
}
