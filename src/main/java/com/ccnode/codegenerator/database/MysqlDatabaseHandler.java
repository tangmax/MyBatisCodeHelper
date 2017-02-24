package com.ccnode.codegenerator.database;

import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.datatype.MySqlTypeUtil;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.ccnode.codegenerator.util.PsiClassUtil;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/23
 * @Description
 */
public class MysqlDatabaseHandler implements DatabaseHandler {

    @Override
    @NotNull
    public ClassValidateResult validateCurrentClass(PsiClass psiClass) {
        ClassValidateResult result = new ClassValidateResult();
        PsiField[] allFields = psiClass.getAllFields();
        if (allFields.length == 0) {
            result.setValid(false);
            result.setInvalidMessages("there is no available field in current class");
            return result;
        }
        List<PsiField> validFields = Lists.newArrayList();
        List<String> errorMessages = Lists.newArrayList();
        for (PsiField psiField : allFields) {
            if (PsiClassUtil.isSupprtedModifier(psiField)) {
                String fieldType = psiField.getType().getCanonicalText();
                if (!MySqlTypeUtil.isSupportedType(psiField.getType())) {
                    if (PsiClassUtil.isPrimitiveType(fieldType)) {
                        errorMessages.add(buildErrorMessage(psiField, " please use with object type"));
                    } else {
                        errorMessages.add(buildErrorMessage(psiField, " unsupported field type"));
                    }
                } else {
                    validFields.add(psiField);
                }
            } else {
                errorMessages.add(buildErrorMessage(psiField, " please use with private and not static"));
            }
        }

        result.setValidFields(validFields);
        if (errorMessages.size() > 0) {
            result.setValid(false);
            StringBuilder builder = new StringBuilder();
            for (String errorMessage : errorMessages) {
                builder.append(errorMessage + "\n");
            }
            builder.deleteCharAt(builder.length() - 1);
            result.setInvalidMessages(builder.toString());
            return result;
        }
        result.setValid(true);
        return result;
    }

    private static String buildErrorMessage(PsiField psiField, String reason) {
        return "field name is:" + psiField.getName() + "  field type is:" + psiField.getType().getCanonicalText() + "  invalid reason is:" + reason;
    }

    @Override
    public List<TypeProps> getRecommendDatabaseTypeOfFieldType(String fieldType) {
        return null;
    }

    @Override
    public String generateSql(List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName) {
        return null;
    }
}
