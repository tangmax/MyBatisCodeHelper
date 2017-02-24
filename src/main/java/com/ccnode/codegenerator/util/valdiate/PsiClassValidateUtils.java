package com.ccnode.codegenerator.util.valdiate;

import com.ccnode.codegenerator.dialog.datatype.ClassFieldInfo;
import com.ccnode.codegenerator.dialog.datatype.MySqlTypeUtil;
import com.ccnode.codegenerator.util.PsiClassUtil;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.ArrayList;
import java.util.List;

import static com.ccnode.codegenerator.util.PsiClassUtil.convertToObjectText;

/**
 * @Author bruce.ge
 * @Date 2017/2/12
 * @Description
 */
public class PsiClassValidateUtils {
    //check the if the class is valid and extract the field values;
    public static ValidateResult validate(PsiClass psiClass) {
        ValidateResult result = new ValidateResult();
        result.setValid(true);
        List<InvalidField> invalidFields = Lists.newArrayList();
        List<ClassFieldInfo> lists = new ArrayList<>();
        PsiField[] allFields = psiClass.getAllFields();
        if (allFields.length == 0) {
            result.setValid(false);
            result.setInvalidType(ValidateResult.InvalidType.NOFIELD);
            return result;
        }
        for (PsiField psiField : allFields) {
            if (PsiClassUtil.isSupprtedModifier(psiField)) {
                String fieldType = psiField.getType().getCanonicalText();
                if (!MySqlTypeUtil.isSupportedType(psiField.getType())) {
                    InvalidField field = new InvalidField();
                    field.setFieldName(psiField.getName());
                    field.setType(fieldType);
                    if (PsiClassUtil.isPrimitiveType(fieldType)) {
                        field.setReason("please use object type instead");
                    } else {
                        field.setReason("not support type");
                    }
                    invalidFields.add(field);
                } else {
                    ClassFieldInfo info = new ClassFieldInfo();
                    info.setFieldName(psiField.getName());
                    info.setFieldType(convertToObjectText(fieldType));
                    lists.add(info);
                }
            } else {
                InvalidField field = new InvalidField();
                field.setFieldName(psiField.getName());
                field.setType(psiField.getType().getCanonicalText());
                field.setReason("field type not support, the field type shall be private and not static");
                invalidFields.add(field);
            }
        }

        if (invalidFields.size() > 0) {
            result.setInvalidFieldList(invalidFields);
            result.setInvalidType(ValidateResult.InvalidType.FIELDERROR);
            result.setValid(false);
        }
        result.setValidFields(lists);
        return result;
    }
}
