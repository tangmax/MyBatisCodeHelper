package com.ccnode.codegenerator.datasourceToolWindow;

import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.DatabaseConnector;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JButton testConnectionButton;
    private JComboBox datasourceCombox;
    private JLabel testConnectionText;
    private JTextField databaseText;
    private JLabel databaseLabel;

    private NewDatabaseInfo newDatabaseInfo;

    private Project myProject;


    public MyBatisDatasourceConfigView(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        myProject = project;
        setTitle("add database config");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        testConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //connect to db.
                boolean b = DatabaseConnector.checkConnection((String) datasourceCombox.getSelectedItem()
                        , urlField.getText(), usernamefield.getText(), passwordField.getText(),databaseText.getText());
                if(!b){
                    testConnectionText.setText("failed");
                }else{
                    testConnectionText.setText("success");
                }
                Timer t = new Timer(4000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        testConnectionText.setText(null);
                    }
                });
                t.setRepeats(false);
                t.start();
            }
        });
        return this.jpanel;
    }


    @Override
    protected void doOKAction() {
        boolean b = DatabaseConnector.checkConnection((String) datasourceCombox.getSelectedItem()
                , urlField.getText(), usernamefield.getText(), passwordField.getText(),databaseText.getText());
        if(b){
            newDatabaseInfo  = new NewDatabaseInfo();
            newDatabaseInfo.setDatabaseType((String) datasourceCombox.getSelectedItem());
            newDatabaseInfo.setUrl(urlField.getText());
            newDatabaseInfo.setUserName(usernamefield.getText());
            newDatabaseInfo.setPassword(passwordField.getText());
            newDatabaseInfo.setDatabase(databaseText.getText());
            super.doOKAction();
        } else{
            Messages.showErrorDialog(myProject,"make sure you can connect to the database","database connect fail");
        }
    }

    public NewDatabaseInfo getNewDatabaseInfo() {
        return newDatabaseInfo;
    }
}
