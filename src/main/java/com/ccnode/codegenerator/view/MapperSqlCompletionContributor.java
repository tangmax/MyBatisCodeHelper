package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.constants.MyBatisXmlConstants;
import com.ccnode.codegenerator.dialog.MapperUtil;
import com.ccnode.codegenerator.dialog.datatype.MySqlTypeUtil;
import com.ccnode.codegenerator.dialog.dto.mybatis.ColumnAndField;
import com.ccnode.codegenerator.util.PsiClassUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bruce.ge on 2017/1/3.
 */
public class MapperSqlCompletionContributor extends CompletionContributor {

    public static final String PARAM = "@Param(\"";
    private static Set<String> mapperMethodSet = new HashSet<String>() {{
        add("update");
        add("select");
        add("delete");
        add("insert");
    }};


    private static ImmutableListMultimap<String, String> multimap = ImmutableListMultimap.<String, String>builder()
            .put("s", "select")
            .put("S", "SELECT")
            .put("i", "insert into")
            .put("I", "INSERT INTO")
            .put("u", "update")
            .put("U", "UPDATE")
            .put("d", "delete")
            .put("D", "DELETE")
            .put("j", "join")
            .put("J", "JOIN")
            .put("i", "inner join")
            .put("I", "INNER JOIN")
            .put("l", "left join")
            .put("L", "LEFT JOIN")
            .put("o", "on")
            .put("O", "ON")
            .put("m", "max")
            .put("M", "MAX")
            .put("m", "min")
            .put("M", "MIN")
            .put("c", "count")
            .put("C", "COUNT")
            .put("d", "distinct")
            .put("D", "DISTINCT")
            .put("f", "from")
            .put("F", "FROM")
            .put("o", "order by")
            .put("O", "ORDER BY")
            .put("d", "desc")
            .put("d", "DESC")
            .put("w", "where")
            .put("W", "WHERE")
            .put("r", "right join")
            .put("R", "RIGHT JOIN")
            .put("l", "limit")
            .put("L", "LIMIT")
            .put("h", "having")
            .put("H", "HAVING")
            .put("g", "group by")
            .put("G", "GROUP BY")
            .put("v", "values")
            .put("V", "VALUES")
            .put("d", "duplicate")
            .put("D", "DUPLICATE")
            .put("f", "for update")
            .put("F", "FOR UPDATE")
            .put("a", "asc")
            .put("A", "ASC")
            .put("u", "union")
            .put("U", "UNION")
            .put("r", "replace")
            .put("R", "REPLACE")
            .build();


    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        if (parameters.getCompletionType() != CompletionType.BASIC) {
            return;
        }
        PsiElement positionElement = parameters.getOriginalPosition();
        if (positionElement == null) {
            return;
        }
        PsiElement parent = positionElement.getParent();
        if (parent == null || !(parent instanceof XmlText)) {
            return;
        }
        PsiElement position = parameters.getPosition();
        String positionText = position.getText();
        int endPosition = parameters.getEditor().getCaretModel().getCurrentCaret().getSelectionStart();
        int startOffset = parameters.getPosition().getTextRange().getStartOffset();
        if (endPosition - startOffset <= 0) {
            return;
        }
        //there are end text for it.
        String realStart = positionText.substring(0, endPosition - startOffset);
        PsiFile originalFile = parameters.getOriginalFile();
        if (!(originalFile instanceof XmlFile)) {
            return;
        }
        XmlFile xmlFile = (XmlFile) originalFile;
        if (!xmlFile.getRootTag().getName().equals("mapper")) {
            return;
        }

        int m = realStart.lastIndexOf("`");
        if (m != -1 && m > realStart.length() - 10) {
            String lastText = realStart.substring(m + 1);

            //get all the rootMap for it.
            XmlTag[] subTags =
                    xmlFile.getRootTag().getSubTags();
            List<ColumnAndField> columnAndFields = new ArrayList<>();
            for (XmlTag tag : subTags) {
                if (tag.getName().equals(MyBatisXmlConstants.RESULTMAP)) {
                    columnAndFields.addAll(generateColumnNames(tag));
                }
            }
            Set<String> columns = extractColumn(columnAndFields);
            int firstStart = findFindAlpha(realStart);
            columns.forEach((item) -> {
                boolean b = item.startsWith(lastText);
                if (b) {
                    result.addElement(LookupElementBuilder.create(realStart.substring(firstStart, m + 1) + item + "`"));
                }
            });
        }

        int findFieldIndex = realStart.lastIndexOf("#{");
        if (findFieldIndex != -1 && findFieldIndex > realStart.length() - 10) {
            //find all the prop for it.
            String namespace = xmlFile.getRootTag().getAttributeValue("namespace");
            PsiClass namespaceClass = findPsiClass(xmlFile, namespace);
            if (namespaceClass == null) {
                return;
            }

            //only for those four method to use.
            String methodName = findMethods(positionElement);
            //find the corresponding method.
            if (methodName == null) {
                return;
            }

            PsiMethod[] methods =
                    namespaceClass.getMethods();
            PsiMethod findMethod = null;
            for (PsiMethod method : methods) {
                if (method.getName().equals(methodName)) {
                    findMethod = method;
                    break;
                }
            }

            if (findMethod == null) {
                return;
            }

            PsiParameter[] parameters1 = findMethod.getParameterList().getParameters();
            List<String> lookUpResult = new ArrayList<>();
            for (PsiParameter parameter : parameters1) {
                String parameterText = parameter.getText();
                String param = extractParam(parameterText);
                String parameterType = parameter.getType().getCanonicalText();
                parameterType = PsiClassUtil.convertToObjectText(parameterType);
                if (MySqlTypeUtil.isSupportedType(parameterType)) {
                    if (param == null) {
                        continue;
                    } else {
                        lookUpResult.add(param);
                    }
                } else {
                    PsiClass psiClass = PsiTypesUtil.getPsiClass(parameter.getType());
                    List<String> props = PsiClassUtil.extractProps(psiClass);
                    if (param == null) {
                        lookUpResult.addAll(props);
                    } else {
                        for (String prop : props) {
                            lookUpResult.add(param + "." + prop);
                        }
                    }
                }
            }
            String remaining = realStart.substring(findFieldIndex + 2);
            int findAlpha = findFindAlpha(realStart);
            for (String s : lookUpResult) {
                if (s.startsWith(remaining)) {
                    result.addElement(LookupElementBuilder.create(realStart.substring(findAlpha, findFieldIndex + 2) + s + "}"));
                }
            }
        }

        if (realStart.length() == 1) {
            ImmutableList<String> recommends = multimap.get(realStart);
            for (String recommend : recommends) {
                result.addElement(LookupElementBuilder.create(recommend + " "));
            }
        }

    }

    private static String extractParam(String parameterText) {
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

    private String findMethods(PsiElement positionElement) {
        PsiElement parent = positionElement.getParent();
        while (parent != null) {
            if (parent instanceof XmlTag) {
                String name = ((XmlTag) parent).getName();
                if (mapperMethodSet.contains(name)) {
                    return ((XmlTag) parent).getAttributeValue("id");
                }
            }
            parent = parent.getParent();
        }
        return null;
    }

    private static PsiClass findPsiClass(PsiElement element, String namespace) {
        Module moduleForPsiElement = ModuleUtilCore.findModuleForPsiElement(element);
        if (moduleForPsiElement == null) {
            return null;
        }
        PsiClass[] classesByName = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(MapperUtil.extractClassShortName(namespace), GlobalSearchScope.moduleScope(moduleForPsiElement));
        for (PsiClass psiClass : classesByName) {
            if (psiClass.isInterface() && psiClass.getQualifiedName().equals(namespace)) {
                return psiClass;
            }
        }
        return null;
    }

    private static int findFindAlpha(String realStart) {
        for (int i = 0; i < realStart.length(); i++) {
            if (Character.isLetterOrDigit(realStart.charAt(i))) {
                return i;
            }
        }
        return 0;
    }

    private static Set<String> extractField(List<ColumnAndField> columnAndFields) {
        Set<String> fields = new HashSet<>();
        for (ColumnAndField columnAndField : columnAndFields) {
            if (StringUtils.isNotBlank(columnAndField.getField())) {
                fields.add(columnAndField.getField());
            }
        }
        return fields;
    }

    private static Set<String> extractColumn(List<ColumnAndField> columnAndFields) {
        Set<String> columns = new HashSet<>();
        for (ColumnAndField columnAndField : columnAndFields) {
            if (StringUtils.isNotBlank(columnAndField.getColumn())) {
                columns.add(columnAndField.getColumn());
            }
        }
        return columns;
    }

    private static List<ColumnAndField> generateColumnNames(XmlTag tag) {
        List<ColumnAndField> column = new ArrayList<>();
        if (tag.getSubTags() != null) {
            for (XmlTag subTag : tag.getSubTags()) {
                ColumnAndField columnAndField = new ColumnAndField();
                String columnName = subTag.getAttributeValue("column");
                String field = subTag.getAttributeValue("property");
                columnAndField.setField(field);
                columnAndField.setColumn(columnName);
                column.add(columnAndField);
            }
        }
        return column;
    }
}
