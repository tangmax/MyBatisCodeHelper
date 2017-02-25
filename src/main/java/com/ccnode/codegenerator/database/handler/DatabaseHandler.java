package com.ccnode.codegenerator.database.handler;

import com.ccnode.codegenerator.database.ClassValidateResult;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/23
 * @Description
 */
//shall make it easy to test.
public interface DatabaseHandler {

    @NotNull
    ClassValidateResult validateCurrentClass(PsiClass psiClass);

    @NotNull
    List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField field);

    @NotNull
    String generateSql(List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName);

    boolean isSupportedParam(PsiParameter psiParameter);
}
