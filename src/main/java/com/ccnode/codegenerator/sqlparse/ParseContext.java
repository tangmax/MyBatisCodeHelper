package com.ccnode.codegenerator.sqlparse;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

/**
 * @Author bruce.ge
 * @Date 2017/3/18
 * @Description
 */
public class ParseContext {
    private String beforeText;

    private String afterText;

    private String allText;

    private String currentWordStart;

    private Project project;

    private int cursorOffSet;

    private XmlTag currentTag;

    private XmlFile currentXmlFile;

    public CompletionType completionType;

    public XmlTag getCurrentTag() {
        return currentTag;
    }

    public void setCurrentTag(XmlTag currentTag) {
        this.currentTag = currentTag;
    }

    public XmlFile getCurrentXmlFile() {
        return currentXmlFile;
    }

    public void setCurrentXmlFile(XmlFile currentXmlFile) {
        this.currentXmlFile = currentXmlFile;
    }

    public int getCursorOffSet() {
        return cursorOffSet;
    }

    public void setCursorOffSet(int cursorOffSet) {
        this.cursorOffSet = cursorOffSet;
    }

    public CompletionType getCompletionType() {
        return completionType;
    }

    public void setCompletionType(CompletionType completionType) {
        this.completionType = completionType;
    }

    public String getBeforeText() {
        return beforeText;
    }

    public void setBeforeText(String beforeText) {
        this.beforeText = beforeText;
    }

    public String getAfterText() {
        return afterText;
    }

    public void setAfterText(String afterText) {
        this.afterText = afterText;
    }

    public String getAllText() {
        return allText;
    }

    public void setAllText(String allText) {
        this.allText = allText;
    }

    public String getCurrentWordStart() {
        return currentWordStart;
    }

    public void setCurrentWordStart(String currentWordStart) {
        this.currentWordStart = currentWordStart;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
