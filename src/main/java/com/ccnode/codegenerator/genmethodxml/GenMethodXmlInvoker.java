package com.ccnode.codegenerator.genmethodxml;

import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.constants.MyBatisXmlConstants;
import com.ccnode.codegenerator.dialog.ChooseXmlToUseDialog;
import com.ccnode.codegenerator.dialog.GenerateResultMapDialog;
import com.ccnode.codegenerator.dialog.MethodExistDialog;
import com.ccnode.codegenerator.methodnameparser.QueryParseDto;
import com.ccnode.codegenerator.methodnameparser.QueryParser;
import com.ccnode.codegenerator.methodnameparser.buidler.ParamInfo;
import com.ccnode.codegenerator.methodnameparser.buidler.QueryInfo;
import com.ccnode.codegenerator.methodnameparser.tag.XmlTagAndInfo;
import com.ccnode.codegenerator.pojo.*;
import com.ccnode.codegenerator.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author bruce.ge
 * @Date 2017/3/5
 * @Description
 */
public class GenMethodXmlInvoker {
    private List<String> methodNameList;

    private Project myProject;

    private PsiElement element;

    private TextRange myTextRange;

    public GenMethodXmlInvoker(List<String> methodName, Project project, TextRange textRange, PsiElement element) {
        this.methodNameList = methodName;
        this.element = element;
        this.myProject = project;
        this.myTextRange = textRange;
    }

    @Nullable
    public GenMethodResult invoke() {
        Module srcModule = ModuleUtilCore.findModuleForPsiElement(element);
        if (srcModule == null) {
            return null;
        }
        PsiClass srcClass = PsiElementUtil.getContainingClass(element);
        PsiIdentifier nameIdentifier = srcClass.getNameIdentifier();
        if (srcClass == null) return null;
        //go to check if the pojo class exist.
        DomainClassInfo domainClassInfo = PsiClassUtil.getDomainClassInfo(srcClass);
        PsiClass pojoClass = domainClassInfo.getDomainClass();
        String srcClassName = srcClass.getName();
        //ask user to provide a class name for it.
        if (pojoClass == null) {
            Messages.showErrorDialog("please provide an insert method with corresponding database class as parameter in this class" +
                    "\n like 'insert(User user)'\n" +
                    "we need the 'User' class to parse your method", "can't find the class for the database table");
            return null;
        }

        XmlFile psixml = null;
        List<XmlFile> xmlFiles = PsiSearchUtils.searchMapperXml(myProject, ModuleUtilCore.findModuleForPsiElement(element), srcClass.getQualifiedName());
        if (xmlFiles.size() == 0) {
            Messages.showErrorDialog("can't find xml file for namespace " + srcClassName, "xml file not found error");
            return null;
        } else if (xmlFiles.size() == 1) {
            psixml = xmlFiles.get(0);
        }
        //extract field from pojoClass.
        List<String> props = PsiClassUtil.extractProps(pojoClass);
        //find the corresponding xml file.
        XmlTag rootTag = psixml.getRootTag();
        if (rootTag == null) {
            return null;
        }

        ExistXmlTagInfo existXmlTagInfo = MyPsiXmlUtils.extractExistXmlInfo(props, rootTag, pojoClass.getQualifiedName());
        if (existXmlTagInfo.getTableName() == null && domainClassInfo.getDomainClassSourceType() == DomainClassSourceType.MYBATISPLUS) {
            String tableName = MyBatisPlusUtils.extractTableNameForMybatisPlus(domainClassInfo);
            if (tableName != null) {
                existXmlTagInfo.setTableName(tableName);
            }
        }
        if (StringUtils.isEmpty(existXmlTagInfo.getTableName())) {

            Messages.showErrorDialog("can't find table name from your " + psixml.getName() + "" +
                    "\nplease add a correct insert method into the file\n" +
                    "like\n'<insert id=\"insert\">\n" +
                    "        INSERT INTO user ....\n</insert>\n" +
                    "so we can extract the table name 'user' from it", "can't extract table name");
            return null;
        }

        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(myProject);
        if (existXmlTagInfo.getFieldToColumnRelation() == null) {
            if (existXmlTagInfo.isHasResultMap()) {
                Messages.showErrorDialog("please check with your resultMap\n" +
                        "dose it contain all the property of " + pojoClass.getQualifiedName() + "? ", "proprety in resultMap is not complete");
                return null;
            } else {
                GenerateResultMapDialog generateResultMapDialog = new GenerateResultMapDialog(myProject, props, pojoClass.getQualifiedName());
                boolean b = generateResultMapDialog.showAndGet();
                if (!b) {
                    return null;
                }
//                Messages.showErrorDialog("please provide a resultMap the type is:" + pojoClass.getQualifiedName() + "\n" +
//                        "in xml path:" + psixml.getVirtualFile().getPath(), "can't find resultMap in your mapper xml");
                //create tag into the file.
                FieldToColumnRelation relation1 = generateResultMapDialog.getRelation();
                //use to generate resultMap
                String allColumnMap = MyPsiXmlUtils.buildAllCoumnMap(relation1.getFiledToColumnMap());
                XmlTag resultMap = rootTag.createChildTag(MyBatisXmlConstants.RESULTMAP, "", allColumnMap, false);
                resultMap.setAttribute(MyBatisXmlConstants.ID, relation1.getResultMapId());
                resultMap.setAttribute(MyBatisXmlConstants.TYPE, pojoClass.getQualifiedName());
                rootTag.addSubTag(resultMap, true);
                Document xmlDocument = psiDocumentManager.getDocument(psixml);
                PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, xmlDocument);

                existXmlTagInfo.setFieldToColumnRelation(MyPsiXmlUtils.convertToRelation(relation1));
            }
        }

        if (!existXmlTagInfo.isHasAllColumn()) {
            String allColumn = MyPsiXmlUtils.buildAllColumn(existXmlTagInfo.getFieldToColumnRelation().getFiledToColumnMap());
            XmlTag sql = rootTag.createChildTag("sql", "", allColumn, false);
            sql.setAttribute("id", MapperConstants.ALL_COLUMN);
            rootTag.addSubTag(sql, true);
        }

        StringBuilder newDaoTextBuider = new StringBuilder();
        List<XmlTag> newGeneratedTag = Lists.newArrayList();
        Set<String> allImportList = Sets.newHashSet();
        Document document = psiDocumentManager.getDocument(srcClass.getContainingFile());
        for (String methodName : this.methodNameList) {
            MethodXmlPsiInfo methodInfo = new MethodXmlPsiInfo();
            methodInfo.setMethodName(methodName);
            methodInfo.setRelation(existXmlTagInfo.getFieldToColumnRelation());

            XmlTag existTag
                    = MyPsiXmlUtils.methodAlreadyExist(psixml, methodInfo.getMethodName());

            if (existTag != null) {
                MethodExistDialog exist = new MethodExistDialog(myProject, existTag.getText());
                boolean b = exist.showAndGet();
                if (!b) {
                    return null;
                } else {
                    WriteCommandAction.runWriteCommandAction(myProject, () -> {
                        existTag.delete();
                    });
                    PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, document);
                }
            }
            rootTag = psixml.getRootTag();
            methodInfo.setTableName(existXmlTagInfo.getTableName());
            methodInfo.setPsiClassFullName(pojoClass.getQualifiedName());
            methodInfo.setPsiClassName(pojoClass.getName());
            methodInfo.setFieldMap(PsiClassUtil.buildFieldMapWithConvertPrimitiveType(pojoClass));
            QueryParseDto parseDto = QueryParser.parse(props, methodInfo);
            XmlTagAndInfo choosed = null;
            if (parseDto.getHasMatched()) {
                //dothings in it.
                List<QueryInfo> queryInfos = parseDto.getQueryInfos();
                //generate tag for them
                List<XmlTagAndInfo> tags = new ArrayList<>();
                for (QueryInfo info : queryInfos) {
                    XmlTagAndInfo tag = MyPsiXmlUtils.generateTag(rootTag, info, methodInfo.getMethodName());
                    tags.add(tag);
                }

                if (tags.size() > 1) {
                    //let user choose with one.
                    ChooseXmlToUseDialog chooseXmlToUseDialog = new ChooseXmlToUseDialog(myProject, tags);
                    boolean b = chooseXmlToUseDialog.showAndGet();
                    if (!b) {
                        return null;
                    } else {
                        choosed = tags.get(chooseXmlToUseDialog.getChoosedIndex());
                    }

                } else {
                    choosed = tags.get(0);
                }
            } else {
                //there is no match the current methodName display error msg for user.
                String content = "";
                List<String> errorMsg = parseDto.getErrorMsg();
                for (int i = 0; i < errorMsg.size(); i++) {
                    content += errorMsg.get(i) + "\n";
                }
                Messages.showErrorDialog(content, "can't parse the methodName");
                return null;
            }

            //means we need to insert the text into it.
            String insertBefore = choosed.getInfo().getMethodReturnType() + " ";
            String insertNext = "(";
            if (choosed.getInfo().getParamInfos() != null) {
                for (int i = 0; i < choosed.getInfo().getParamInfos().size(); i++) {
                    ParamInfo info = choosed.getInfo().getParamInfos().get(i);
                    insertNext += "@Param(\"" + info.getParamAnno() + "\")" + info.getParamType() + " " + info.getParamValue();
                    if (i != choosed.getInfo().getParamInfos().size() - 1) {
                        insertNext += ",";
                    }
                }
            }
            insertNext += ");";
            newDaoTextBuider.append(insertBefore + methodName + insertNext + "\n\n\t");
            newGeneratedTag.add(choosed.getXmlTag());
            Set<String> importList = choosed.getInfo().getImportList();
            allImportList.addAll(importList);
        }
        WriteCommandAction.runWriteCommandAction(myProject, () -> {
            document.replaceString(myTextRange.getStartOffset(), myTextRange.getEndOffset(), newDaoTextBuider.toString());
            PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, document);
        });
        final XmlFile finalPsiXml = psixml;
        Document xmlDocument = psiDocumentManager.getDocument(psixml);
        for (XmlTag tag : newGeneratedTag) {
            WriteCommandAction.runWriteCommandAction(myProject, () -> {
                PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, xmlDocument);
                finalPsiXml.getRootTag().addSubTag(tag, false);
                PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, xmlDocument);
                XmlTag[] newSubTags = finalPsiXml.getRootTag().getSubTags();
                XmlTag newSubTag = newSubTags[newSubTags.length - 1];
                xmlDocument.insertString(newSubTag.getTextOffset(), "\n<!--auto generated by codehelper on " + DateUtil.formatLong(new Date()) + "-->\n" + GenCodeUtil.ONE_RETRACT);
                PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, xmlDocument);
            });
        }
        PsiFile psiFile = PsiDocumentManager.getInstance(myProject).getPsiFile(document);
        if (psiFile != null && (psiFile instanceof PsiJavaFile)) {
            PsiDocumentUtils.addImportToFile(psiDocumentManager, (PsiJavaFile) psiFile, psiDocumentManager.getDocument(srcClass.getContainingFile()), allImportList);
        }
        XmlTag[] subTags = psixml.getRootTag().getSubTags();
        if (subTags.length > 0) {
            GenMethodResult result = new GenMethodResult();
            result.setHasCursor(true);
            result.setCursorFile(psixml);
            result.setCursorElement(subTags[subTags.length - 1]);
            return result;
        } else {
            return null;
        }
    }
}
