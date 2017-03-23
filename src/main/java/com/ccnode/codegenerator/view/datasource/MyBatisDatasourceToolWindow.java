package com.ccnode.codegenerator.view.datasource;

import com.ccnode.codegenerator.util.IconUtils;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
public class MyBatisDatasourceToolWindow implements ToolWindowFactory,DumbAware {

    private JPanel myToolWindowContent;

    private ToolWindow myToolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        myToolWindowContent = new MyBastisDatasourceForm(project).getMyDatasourcePanel();
        myToolWindow = toolWindow;
        ContentFactory instance = ContentFactory.SERVICE.getInstance();
        Content content = instance.createContent(myToolWindowContent, "", false);
        content.setIcon(IconUtils.useMyBatisIcon());
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public void init(ToolWindow window) {

    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return false;
    }
}
