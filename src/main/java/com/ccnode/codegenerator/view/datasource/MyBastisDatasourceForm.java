package com.ccnode.codegenerator.view.datasource;

import com.ccnode.codegenerator.datasourceToolWindow.MyBatisDatasourceConfigView;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
public class MyBastisDatasourceForm {
    private JPanel myDatasourcePanel;
    private JButton addButton;
    private JButton refreshButton;
    private JButton configButton;
    private JButton consoleButton;
    private JTree datasourceTree;
    private Project myProject;

    public MyBastisDatasourceForm(Project project) {
        myProject = project;
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyBatisDatasourceConfigView myBatisDatasourceConfigView = new MyBatisDatasourceConfigView(myProject, false);
                boolean b = myBatisDatasourceConfigView.showAndGet();
                System.out.println(b);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

    }


    public JPanel getMyDatasourcePanel() {
        return myDatasourcePanel;
    }
}
