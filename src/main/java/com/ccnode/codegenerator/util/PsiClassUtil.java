package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.database.DatabaseComponenent;
import com.ccnode.codegenerator.dialog.MapperUtil;
import com.ccnode.codegenerator.dialog.datatype.ClassFieldInfo;
import com.ccnode.codegenerator.pojo.DomainClassInfo;
import com.ccnode.codegenerator.pojo.DomainClassSourceType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTypesUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by bruce.ge on 2016/12/9.
 */
public class PsiClassUtil {


    public static final String PARAM = "@Param(\"";
    public static final String MYBATISPLUS_BASEMAPPER = "BaseMapper";

    private static Map<String, String> primitiveToObjectMap = new HashMap<String, String>() {
        {
            put("int", "java.lang.Integer");
            put("short", "java.lang.Short");
            put("long", "java.lang.Long");
            put("double", "java.lang.Double");
            put("float", "java.lang.FLoat");
            put("boolean", "java.lang.Boolean");
            put("byte[]", "java.lang.Byte");
        }
    };

    @NotNull
    public static List<String> extractProps(PsiClass pojoClass) {
        PsiField[] allFields = pojoClass.getAllFields();
        List<String> props = new ArrayList<String>();
        for (PsiField psiField : allFields) {
            if (isSupprtedModifier(psiField)) {
                props.add(psiField.getName());
            }
        }
        return props;
    }

    @NotNull
    public static String getModuleSrcPathOfClass(PsiClass srcClass) {
        String qualifiedName = srcClass.getQualifiedName();
        String[] split = qualifiedName.split("\\.");
        int splitLength = split.length;

        VirtualFile psiFile = srcClass.getContainingFile().getVirtualFile();
        while (splitLength > 0) {
            psiFile = psiFile.getParent();
            splitLength--;
        }
        return psiFile.getPath();
    }


    @NotNull
    public static String getPackageToModule(String path, String modulePath) {
        Path moduleSrc = Paths.get(modulePath);
        Path relativeToSouce = moduleSrc.relativize(Paths.get(path));
        String relate = relativeToSouce.toString();
        relate = relate.replace("\\", ".");
        relate = relate.replace("/", ".");
        return relate;
    }


    public static PsiMethod getAddMethod(PsiClass srcClass) {
        PsiMethod[] methods = srcClass.getMethods();
        List<PsiMethod> methodsList = new ArrayList<PsiMethod>();
        for (PsiMethod classMethod : methods) {
            String name = classMethod.getName().toLowerCase();
            if (name.startsWith("insert") || name.startsWith("save") || name.startsWith("add") || name.startsWith("create")) {
                if (classMethod.getParameterList().getParameters().length == 1) {
                    PsiParameter parameter = classMethod.getParameterList().getParameters()[0];
                    PsiClass psiClass = PsiTypesUtil.getPsiClass(parameter.getType());
                    if (canBeDomainClass(psiClass)) {
                        methodsList.add(classMethod);
                    }
                }
            }
        }
        if (methodsList.size() == 0) {
            return null;
        } else {
            PsiMethod miniMethod = methodsList.stream().min(Comparator.comparingInt(o -> o.getName().length())).get();
            return miniMethod;
        }
    }

    public static boolean canBeDomainClass(PsiClass psiClass) {
        if (psiClass != null) {
            if (!psiClass.getQualifiedName().startsWith("java.")) {
                PsiField[] allFields = psiClass.getAllFields();
                for (PsiField allField : allFields) {
                    if (isSupprtedModifier(allField)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    public static DomainClassInfo getDomainClassInfo(PsiClass srcClass) {
        PsiMethod addMethod = getAddMethod(srcClass);
        if (addMethod != null) {
            PsiClass psiClass = PsiTypesUtil.getPsiClass(addMethod.getParameterList().getParameters()[0].getType());
            DomainClassInfo info = new DomainClassInfo();
            info.setDomainClass(psiClass);
            info.setDomainClassSourceType(DomainClassSourceType.FROMMETHOD);
            return info;
        } else {
            //get from super type.
            PsiClass psiClass = MyBatisPlusUtils.getClassFromMyBatisPlus(srcClass);
            if (psiClass != null) {
                DomainClassInfo info = new DomainClassInfo();
                info.setDomainClass(psiClass);
                info.setDomainClassSourceType(DomainClassSourceType.MYBATISPLUS);
                return info;
            }
        }
        return null;
    }

    public static List<ClassFieldInfo> buildPropFieldInfo(List<PsiField> validFields) {
        List<ClassFieldInfo> lists = new ArrayList<>();
        for (PsiField psiField : validFields) {
            ClassFieldInfo info = new ClassFieldInfo();
            info.setFieldName(psiField.getName());
            info.setFieldType(convertToObjectText(psiField.getType().getCanonicalText()));
            info.setPsiField(psiField);
            lists.add(info);
        }
        return lists;
    }


    public static boolean isSupprtedModifier(PsiField psiField) {
        return (psiField.hasModifierProperty("private") || psiField.hasModifierProperty("protected")) && !psiField.hasModifierProperty("static");
    }


    public static List<String> buildPropFields(PsiClass psiClass) {
        List<String> lists = new ArrayList<>();
        PsiField[] allFields = psiClass.getAllFields();
        for (PsiField psiField : allFields) {
            if (isSupprtedModifier(psiField)) {
                lists.add(psiField.getName());
            }
        }
        return lists;
    }

    @NotNull
    public static Map<String, String> buildFieldMapWithConvertPrimitiveType(PsiClass pojoClass) {
        Map<String, String> fieldMap = new HashMap<>();
        PsiField[] allFields = pojoClass.getAllFields();
        for (PsiField f : allFields) {
            if (isSupprtedModifier(f)) {
                String canonicalText = f.getType().getCanonicalText();
                String objectTypeText = convertToObjectText(canonicalText);
                fieldMap.put(f.getName(), objectTypeText);
            }
        }
        return fieldMap;
    }


    @NotNull
    public static Map<String, String> buildFieldMap(PsiClass pojoClass) {
        Map<String, String> fieldMap = new HashMap<>();
        PsiField[] allFields = pojoClass.getAllFields();
        for (PsiField f : allFields) {
            if (isSupprtedModifier(f)) {
                String canonicalText = f.getType().getCanonicalText();
                String objectTypeText = canonicalText;
                fieldMap.put(f.getName(), objectTypeText);
            }
        }
        return fieldMap;
    }

    public static String convertToObjectText(String canonicalText) {
        String s = primitiveToObjectMap.get(canonicalText);
        if (s != null) {
            return s;
        } else {
            return canonicalText;
        }
    }

    public static boolean isPrimitiveType(String type) {
        return primitiveToObjectMap.containsKey(type);
    }

    @Nullable
    public static PsiClass findClassOfQuatifiedType(@NotNull PsiElement element, @NotNull String resultTypeValue) {
        Module moduleForPsiElement =
                ModuleUtilCore.findModuleForPsiElement(element);
        if (moduleForPsiElement == null) {
            return null;
        }
        PsiClass[] classesByName = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(MapperUtil.extractClassShortName(resultTypeValue)
                , GlobalSearchScope.moduleScope(moduleForPsiElement));
        if (classesByName.length == 0) {
            return null;
        }

        for (PsiClass psiClass : classesByName) {
            if (psiClass.getQualifiedName().equals(resultTypeValue)) {
                return psiClass;
            }
        }
        return null;
    }


    @Nullable
    public static PsiClass findClassOfQuatifiedTypeUsingType(@NotNull PsiElement element, @NotNull String resultTypeValue) {

        Module moduleForPsiElement =
                ModuleUtilCore.findModuleForPsiElement(element);
        if (moduleForPsiElement == null) {
            return null;
        }
        PsiClassType typeByName = PsiType.getTypeByName(resultTypeValue, element.getProject(), GlobalSearchScope.moduleScope(moduleForPsiElement));
        PsiClass psiClass = PsiTypesUtil.getPsiClass(typeByName);
        return psiClass;
    }

    @Nullable
    public static PsiClass findClassOfQuatifiedType(@Nullable Module module, @NotNull Project project, @NotNull String resultTypeValue) {
        if (module == null) {
            return null;
        }
        PsiClass[] classesByName = PsiShortNamesCache.getInstance(project).getClassesByName(MapperUtil.extractClassShortName(resultTypeValue)
                , GlobalSearchScope.moduleScope(module));
        if (classesByName.length == 0) {
            return null;
        }

        for (PsiClass psiClass : classesByName) {
            if (psiClass.getQualifiedName().equals(resultTypeValue)) {
                return psiClass;
            }
        }
        return null;
    }

    @NotNull
    public static List<String> extractMyBatisParam(PsiMethod findMethod) {
        List<String> lookUpResult = new ArrayList<>();
        PsiParameter[] parameters1 = findMethod.getParameterList().getParameters();
        for (PsiParameter parameter : parameters1) {
            String parameterText = parameter.getText();
            String param = extractParam(parameterText);
            String parameterType = parameter.getType().getCanonicalText();
            parameterType = convertToObjectText(parameterType);
            //if it's basic type, just add it to the param.
            if (DatabaseComponenent.currentHandler().getAutoCompleteHandler().isSupportedParam(parameter)) {
                if (param == null) {
                    continue;
                } else {
                    lookUpResult.add(param);
                }
            } else {
                PsiClass psiClass = PsiTypesUtil.getPsiClass(parameter.getType());
                if (psiClass == null) {
                    continue;
                }
                List<String> props = extractProps(psiClass);
                //means there is no @Param for this, just add it.
                if (param == null) {
                    lookUpResult.addAll(props);
                } else {
                    for (String prop : props) {
                        lookUpResult.add(param + "." + prop);
                    }
                }
            }
        }
        return lookUpResult;
    }

    public static String extractParam(String parameterText) {
        int i = parameterText.indexOf(PARAM);
        if (i == -1) {
            return null;
        }
        int u = i + PARAM.length();
        String m = "";

        char c;
        while (u < parameterText.length() && (c = parameterText.charAt(u)) != '"') {
            m += c;
            u++;
        }
        if (m.length() > 0) {
            return m;
        }
        return null;
    }

    @Nullable
    public static PsiMethod getClassMethodByMethodName(PsiClass namespaceClass, String methodName) {
        PsiMethod[] methods =
                namespaceClass.getMethods();
        PsiMethod findMethod = null;
        for (PsiMethod method : methods) {
            if (method.getName().equals(methodName)) {
                findMethod = method;
                break;
            }
        }
        return findMethod;
    }
}
