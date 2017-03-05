package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.pojo.DomainClassInfo;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTypesUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @Author bruce.ge
 * @Date 2017/3/5
 * @Description
 */
public class MyBatisPlusUtils {

    @Nullable
    public static String extractTableNameForMybatisPlus(DomainClassInfo domainClassInfo) {
        String tableName = null;
        PsiModifierList modifierList = domainClassInfo.getDomainClass().getModifierList();
        if (modifierList != null) {
            PsiAnnotation[] annotations = modifierList.getApplicableAnnotations();
            if (annotations.length > 0) {
                for (PsiAnnotation annotation : annotations) {
                    if (annotation.getQualifiedName().equals("com.baomidou.mybatisplus.annotations.TableName")) {
                        PsiNameValuePair[] attributes = annotation.getParameterList().getAttributes();
                        for (PsiNameValuePair attribute : attributes) {
                            if (attribute.getName() == null || attribute.getName().equals("value")) {
                                PsiAnnotationMemberValue value = attribute.getValue();
                                if (value != null && StringUtils.isNotBlank(value.getText())) {
                                    if (value.getText().startsWith("\"") && value.getText().endsWith("\""))
                                        tableName = value.getText().substring(1, value.getText().length() - 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        return tableName;
    }

    @Nullable
    static PsiClass getClassFromMyBatisPlus(PsiClass srcClass) {
        PsiClassType[] extendsListTypes = srcClass.getExtendsListTypes();
        for (PsiClassType extendsListType : extendsListTypes) {
            if (!Objects.equals(extendsListType.getClassName(), PsiClassUtil.MYBATISPLUS_BASEMAPPER) && extendsListType.getParameterCount() != 1) {
                continue;
            }
            PsiClass psiClass = PsiTypesUtil.getPsiClass(extendsListType.getParameters()[0]);
            if (PsiClassUtil.canBeDomainClass(psiClass)) {
                return psiClass;
            }
        }
        return null;
    }
}
