package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.dialog.datatype.ClassFieldInfo;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTypesUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce.ge on 2016/12/9.
 */
public class PsiClassUtil {
    public static List<String> extractProps(PsiClass pojoClass) {
        PsiField[] allFields = pojoClass.getAllFields();
        List<String> props = new ArrayList<String>();
        for (PsiField psiField : allFields) {
            if (psiField.hasModifierProperty("private") && !psiField.hasModifierProperty("static")) {
                props.add(psiField.getName());
            }
        }
        return props;
    }

    public static PsiMethod getAddMethod(PsiClass srcClass) {
        PsiMethod[] methods = srcClass.getMethods();
        List<PsiMethod> methodsList = new ArrayList<PsiMethod>();
        for (PsiMethod classMethod : methods) {
            String name = classMethod.getName().toLowerCase();
            if (name.startsWith("insert") || name.startsWith("save") || name.startsWith("add")||name.startsWith("create")) {
                if(classMethod.getParameterList().getParameters().length==1){
                    PsiParameter parameter = classMethod.getParameterList().getParameters()[0];
                    //mean is not system class like collection class ect.
                    if(!parameter.getType().getCanonicalText().startsWith("java.")){
                        methodsList.add(classMethod);
                    }
                }
            }
        }
        if (methodsList.size() == 0) {
            return null;
        } else {
            PsiMethod miniMethod = methodsList.get(0);
            for (int i = 1; i < methodsList.size(); i++) {
                if (methodsList.get(i).getName().length() < miniMethod.getName().length()) {
                    miniMethod = methodsList.get(i);
                }
            }
            return miniMethod;
        }
    }

    public static PsiClass getPojoClass(PsiClass srcClass) {
        PsiMethod addMethod = getAddMethod(srcClass);
        if (addMethod != null) {
            PsiParameterList parameterList = addMethod.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            if (parameters.length == 1) {
                PsiType type = parameters[0].getType();
                PsiClass psiClass = PsiTypesUtil.getPsiClass(type);
                PsiField[] allFields = psiClass.getAllFields();
                //// TODO: 2016/12/15 maybe need check if exist id property.
                if (allFields != null && allFields.length > 0) {
                    return psiClass;
                }
                //try to get it from the class.
            }
        }
        return null;
    }

    public static List<ClassFieldInfo> buildPropFieldInfo(PsiClass psiClass) {
        List<ClassFieldInfo> lists = new ArrayList<>();
        PsiField[] allFields = psiClass.getAllFields();
        for (PsiField psiField : allFields) {
            if (psiField.hasModifierProperty("private") && !psiField.hasModifierProperty("static")) {
                ClassFieldInfo info = new ClassFieldInfo();
                info.setFieldName(psiField.getName());
                info.setFieldType(psiField.getType().getCanonicalText());
                lists.add(info);
            }
        }
        return lists;
    }


    public static List<String> buildPropFields(PsiClass psiClass) {
        List<String> lists = new ArrayList<>();
        PsiField[] allFields = psiClass.getAllFields();
        for (PsiField psiField : allFields) {
            if (psiField.hasModifierProperty("private") && !psiField.hasModifierProperty("static")) {
                lists.add(psiField.getName());
            }
        }
        return lists;
    }

    @NotNull
    public static Map<String, String> buildFieldMap(PsiClass pojoClass) {
        Map<String, String> fieldMap = new HashMap<>();
        PsiField[] allFields = pojoClass.getAllFields();
        for (PsiField f : allFields) {
            if (f.hasModifierProperty("private") && !f.hasModifierProperty("static")) {
                String canonicalText = f.getType().getCanonicalText();
                String objectTypeText = convertToObjectText(canonicalText);
                fieldMap.put(f.getName(), objectTypeText);
            }
        }
        return fieldMap;
    }

    private static String convertToObjectText(String canonicalText) {
        if (canonicalText.equals("int")) {
            return "java.lang.Integer";
        } else if (canonicalText.equals("short")) {
            return "java.lang.Short";
        } else if (canonicalText.equals("long")) {
            return "java.lang.Long";
        } else if (canonicalText.equals("double")) {
            return "java.lang.Double";
        } else {
            return canonicalText;
        }
    }
}
