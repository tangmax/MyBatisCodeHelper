package com.ccnode.codegenerator.datasourceToolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
public class MyBatisDatasourceConfigView extends DialogWrapper{
    private JPanel jpanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTextField urlField;
    private JTextField usernamefield;
    private JTextField passwordField;

    public MyBatisDatasourceConfigView(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        setTitle("add database config");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.jpanel;
    }
}
