package com.ccnode.codegenerator.pojo;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlFile;

import java.util.Map;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class MethodXmlPsiInfo {

    private String methodName;

    private FieldToColumnRelation relation;

    private String tableName;

    private Map<String, String> fieldMap;

    private String psiClassName;

    private XmlFile mybatisXmlFile;


    private String psiClassFullName;

    private Project project;

    private PsiClass srcClass;

    public XmlFile getMybatisXmlFile() {
        return mybatisXmlFile;
    }

    public void setMybatisXmlFile(XmlFile mybatisXmlFile) {
        this.mybatisXmlFile = mybatisXmlFile;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    //get the interface class of project.
    public PsiClass getSrcClass() {
        return srcClass;
    }

    public void setSrcClass(PsiClass srcClass) {
        this.srcClass = srcClass;
    }

    public String getPsiClassFullName() {
        return psiClassFullName;
    }

    public void setPsiClassFullName(String psiClassFullName) {
        this.psiClassFullName = psiClassFullName;
    }

    public Map<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public String getPsiClassName() {
        return psiClassName;
    }

    public void setPsiClassName(String psiClassName) {
        this.psiClassName = psiClassName;
    }

    public FieldToColumnRelation getRelation() {
        return relation;
    }

    public void setRelation(FieldToColumnRelation relation) {
        this.relation = relation;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
