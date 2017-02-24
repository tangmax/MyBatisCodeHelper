package com.ccnode.codegenerator.database;

import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.intellij.psi.PsiClass;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/23
 * @Description
 */
//shall make it easy to test.
public interface DatabaseHandler {

    ClassValidateResult validateCurrentClass(PsiClass psiClass);

    TypeProps getDefaultTypePropsOfFieldType(String fieldTypeName);

    TypeProps getTypePropsOfDatabaseType(String databaseType);

    List<String> getRecommendDatabaseTypeOfFieldType(String fieldType);

    String generateSql(List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName);
}
