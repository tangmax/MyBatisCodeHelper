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
public class MysqlDatabaseHandler implements DatabaseHandler {
    @Override
    public ClassValidateResult validateCurrentClass(PsiClass psiClass) {
        return null;
    }

    @Override
    public TypeProps getDefaultTypePropsOfFieldType(String fieldTypeName) {
        return null;
    }

    @Override
    public TypeProps getTypePropsOfDatabaseType(String databaseType) {
        return null;
    }

    @Override
    public List<String> getRecommendDatabaseTypeOfFieldType(String fieldType) {
        return null;
    }

    @Override
    public String generateSql(List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName) {
        return null;
    }
}
