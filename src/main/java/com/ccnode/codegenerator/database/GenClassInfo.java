package com.ccnode.codegenerator.database;

import java.util.List;
import java.util.Set;

/**
 * @Author bruce.ge
 * @Date 2017/3/7
 * @Description
 */
public class GenClassInfo {
    private String classPackageName;

    private String className;

    private Set<String> importList;

    private List<NewClassFieldInfo> classFieldInfos;

    private String classFullPath;


    public String getClassPackageName() {
        return classPackageName;
    }

    public String getClassFullPath() {
        return classFullPath;
    }

    public void setClassFullPath(String classFullPath) {
        this.classFullPath = classFullPath;
    }

    public void setClassPackageName(String classPackageName) {
        this.classPackageName = classPackageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Set<String> getImportList() {
        return importList;
    }

    public void setImportList(Set<String> importList) {
        this.importList = importList;
    }

    public List<NewClassFieldInfo> getClassFieldInfos() {
        return classFieldInfos;
    }

    public void setClassFieldInfos(List<NewClassFieldInfo> classFieldInfos) {
        this.classFieldInfos = classFieldInfos;
    }
}
