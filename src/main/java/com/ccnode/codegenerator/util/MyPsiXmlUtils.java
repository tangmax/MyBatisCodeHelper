package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.constants.MyBatisXmlConstants;
import com.ccnode.codegenerator.database.DatabaseComponenent;
import com.ccnode.codegenerator.dialog.MapperUtil;
import com.ccnode.codegenerator.methodnameparser.buidler.QueryInfo;
import com.ccnode.codegenerator.methodnameparser.tag.XmlTagAndInfo;
import com.ccnode.codegenerator.pojo.ExistXmlTagInfo;
import com.ccnode.codegenerator.pojo.FieldToColumnRelation;
import com.google.common.collect.Lists;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @Author bruce.ge
 * @Date 2017/2/16
 * @Description
 */
public class MyPsiXmlUtils {
    @NotNull
    public static List<XmlTag> getXmlAttributeOfType(XmlDocument xmlDocument, Set<String> tagNames) {
        List<XmlTag> values = Lists.newArrayList();
        XmlTag rootTag = xmlDocument.getRootTag();
        if (rootTag == null) {
            return values;
        }
        if (tagNames.contains(rootTag.getName())) {
            values.add(rootTag);
        }
        addTagsToList(rootTag, tagNames, values);
        return values;
    }

    @NotNull
    public static List<XmlTag> getXmlAttributeOfType(XmlDocument xmlDocument, String tagName) {
        List<XmlTag> values = Lists.newArrayList();
        XmlTag rootTag = xmlDocument.getRootTag();
        if (rootTag == null) {
            return values;
        }
        if (tagName.equals(rootTag.getName())) {
            values.add(rootTag);
        }
        addTagsToList(rootTag, tagName, values);
        return values;
    }

    private static void addTagsToList(XmlTag rootTag, Set<String> tagNames, List<XmlTag> values) {
        XmlTag[] subTags = rootTag.getSubTags();
        if (subTags.length == 0) {
            return;
        }
        for (XmlTag subTag : subTags) {
            if (tagNames.contains(subTag.getName())) {
                values.add(subTag);
            }
            addTagsToList(subTag, tagNames, values);
        }
    }


    private static void addTagsToList(XmlTag rootTag, String tagName, List<XmlTag> values) {
        XmlTag[] subTags = rootTag.getSubTags();
        if (subTags.length == 0) {
            return;
        }
        for (XmlTag subTag : subTags) {
            if (tagName.equals(subTag.getName())) {
                values.add(subTag);
            }
            addTagsToList(subTag, tagName, values);
        }
    }

    @Nullable
    public static String findCurrentElementIntefaceMethodName(PsiElement positionElement) {
        PsiElement parent = positionElement.getParent();
        while (parent != null) {
            if (parent instanceof XmlTag) {
                String name = ((XmlTag) parent).getName();
                if (MyBatisXmlConstants.mapperMethodSet.contains(name)) {
                    return ((XmlTag) parent).getAttributeValue(MyBatisXmlConstants.ID);
                }
            }
            parent = parent.getParent();
        }
        return null;
    }


    @Nullable
    public static XmlTag findCurrentElementXmlTag(PsiElement positionElement) {
        PsiElement parent = positionElement.getParent();
        while (parent != null) {
            if (parent instanceof XmlTag) {
                String name = ((XmlTag) parent).getName();
                if (MyBatisXmlConstants.mapperMethodSet.contains(name)) {
                    return (XmlTag) parent;
                }
            }
            parent = parent.getParent();
        }
        return null;
    }

    @Nullable
    public static String findCurrentXmlFileNameSpace(@NotNull XmlFile xmlFile) {
        XmlTag rootTag = xmlFile.getRootTag();
        if (rootTag == null || !(rootTag.getName().equals(MyBatisXmlConstants.MAPPER))) {
            return null;
        }
        return rootTag.getAttributeValue(MyBatisXmlConstants.NAMESPACE);
    }

    @NotNull
    public static ExistXmlTagInfo extractExistXmlInfo(List<String> props, XmlTag rootTag, String qualifiedName) {
        ExistXmlTagInfo existXmlTagInfo = new ExistXmlTagInfo();
        XmlTag[] subTags = rootTag.getSubTags();
        for (XmlTag tag : subTags) {
            if (tag.getName().equalsIgnoreCase(MyBatisXmlConstants.INSERT)) {
                if (existXmlTagInfo.getTableName() != null) {
                    continue;
                }
                String insertText = tag.getValue().getText();
                //go format it.
                String tableName = MapperUtil.extractTable(insertText);
                if (tableName != null && tableName.length() < 30) {
                    existXmlTagInfo.setTableName(tableName);
                }
            } else if (existXmlTagInfo.getFieldToColumnRelation() == null && tag.getName().equalsIgnoreCase(MyBatisXmlConstants.RESULTMAP)) {
                String resultMapId;
                XmlAttribute id = tag.getAttribute(MyBatisXmlConstants.ID);
                if (id != null && id.getValue() != null) {
                    resultMapId = id.getValue();
                    XmlAttribute typeAttribute = tag.getAttribute(MyBatisXmlConstants.TYPE);
                    if (typeAttribute != null && typeAttribute.getValue() != null && typeAttribute.getValue().trim().equals(qualifiedName)) {
                        //mean we find the corresponding prop.
                        existXmlTagInfo.setHasResultMap(true);
                        existXmlTagInfo.setFieldToColumnRelation(extractFieldAndColumnRelation(tag, props, resultMapId));
                    }
                }
            } else if (!existXmlTagInfo.isHasAllColumn() && tag.getName().equalsIgnoreCase(MyBatisXmlConstants.SQL)) {
                XmlAttribute id = tag.getAttribute(MyBatisXmlConstants.ID);
                if (id != null && id.getValue().equals(MapperConstants.ALL_COLUMN)) {
                    existXmlTagInfo.setHasAllColumn(true);
                }
            }
            //then go next shall be the same.
            //deal with it.
        }
        return existXmlTagInfo;
    }

    public static FieldToColumnRelation extractFieldAndColumnRelation(XmlTag tag, List<String> props, String resultMapId) {
        Set<String> propSet = new HashSet<>(props);
        XmlTag[] subTags = tag.getSubTags();
        if (subTags == null || subTags.length == 0) {
            return null;
        }
        Map<String, String> fieldAndColumnMap = new LinkedHashMap<>();
        for (XmlTag propTag : subTags) {
            XmlAttribute column = propTag.getAttribute("column");
            XmlAttribute property = propTag.getAttribute("property");
            if (column == null || column.getValue() == null || property == null || property.getValue() == null) {
                continue;
            }
            String columnString = column.getValue().trim();
            String propertyString = property.getValue().trim();
            if (!propSet.contains(propertyString)) {
                continue;
            }
            fieldAndColumnMap.put(propertyString.toLowerCase(), columnString);
            propSet.remove(propertyString);
        }
        //mean there are not all property in the resultMap.
        if (propSet.size() != 0) {
            return null;
        }
        FieldToColumnRelation relation = new FieldToColumnRelation();
        relation.setFiledToColumnMap(fieldAndColumnMap);
        relation.setResultMapId(resultMapId);
        return relation;
    }

    public static String buildAllColumn(Map<String, String> filedToColumnMap) {
        StringBuilder bu = new StringBuilder();
        int i = 0;
        for (String s : filedToColumnMap.keySet()) {
            i++;
            bu.append("\n" + GenCodeUtil.ONE_RETRACT).append(DatabaseComponenent.formatColumn(filedToColumnMap.get(s)));
            if (i != filedToColumnMap.size()) {
                bu.append(",");
            }
        }
        bu.append("\n");
        return bu.toString();
    }

    public static String buildAllCoumnMap(Map<String, String> fieldToColumnMap) {
        StringBuilder builder = new StringBuilder();
        for (String prop : fieldToColumnMap.keySet()) {
            builder.append("\n" + GenCodeUtil.ONE_RETRACT).append("<result column=\"").append(fieldToColumnMap.get(prop)).append("\"")
                    .append(" property=\"").append(prop).append("\"/>");
        }
        builder.append("\n");
        return builder.toString();
    }

    public static XmlTag methodAlreadyExist(PsiFile psixml, String methodName) {
        XmlTag rootTag = ((XmlFileImpl) psixml).getRootTag();
        XmlTag[] subTags = rootTag.getSubTags();
        for (XmlTag subTag : subTags) {
            XmlAttribute id = subTag.getAttribute(MyBatisXmlConstants.ID);
            if (id != null && id.getValue() != null && id.getValue().equalsIgnoreCase(methodName)) {
                return subTag;
            }
        }
        return null;
    }

    public static XmlTagAndInfo generateTag(XmlTag rootTag, QueryInfo info, String methodName) {
        XmlTagAndInfo xmlTagAndInfo = new XmlTagAndInfo();
        xmlTagAndInfo.setInfo(info);
        XmlTag select = rootTag.createChildTag(info.getType(), "", info.getSql(), false);
        select.setAttribute("id", methodName);
        if (info.getReturnMap() != null) {
            select.setAttribute(MyBatisXmlConstants.RESULTMAP, info.getReturnMap());
        } else if (info.getReturnClass() != null) {
            select.setAttribute(MyBatisXmlConstants.RESULT_TYPE, info.getReturnClass());
        }
        xmlTagAndInfo.setXmlTag(select);
        return xmlTagAndInfo;

    }

    public static FieldToColumnRelation convertToRelation(FieldToColumnRelation relation1) {
        FieldToColumnRelation relation = new FieldToColumnRelation();
        relation.setResultMapId(relation1.getResultMapId());
        Map<String, String> fieldToColumnLower = new LinkedHashMap<>();
        for (String prop : relation1.getFiledToColumnMap().keySet()) {
            fieldToColumnLower.put(prop.toLowerCase(), relation1.getFiledToColumnMap().get(prop));
        }
        relation.setFiledToColumnMap(fieldToColumnLower);
        return relation;
    }

    public static void buildAllColumnMap(Project myProject, Document document, XmlTag rootTag, PsiDocumentManager psiDocumentManager, FieldToColumnRelation relation1, String qualifiedName) {
        String allColumnMap = buildAllCoumnMap(relation1.getFiledToColumnMap());
        XmlTag resultMap = rootTag.createChildTag(MyBatisXmlConstants.RESULTMAP, "", allColumnMap, false);
        resultMap.setAttribute(MyBatisXmlConstants.ID, relation1.getResultMapId());
        resultMap.setAttribute(MyBatisXmlConstants.TYPE, qualifiedName);
        WriteCommandAction.runWriteCommandAction(myProject,()->{
            rootTag.addSubTag(resultMap, true);
            Document xmlDocument = document;
            PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, xmlDocument);
        });
    }
}
