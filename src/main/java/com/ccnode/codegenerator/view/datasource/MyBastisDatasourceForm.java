package com.ccnode.codegenerator.view.datasource;

import com.ccnode.codegenerator.datasourceToolWindow.DatasourceState;
import com.ccnode.codegenerator.datasourceToolWindow.MyBatisDatasourceConfigView;
import com.ccnode.codegenerator.datasourceToolWindow.NewDatabaseInfo;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.DatabaseConnector;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.DatabaseInfo;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.TableInfo;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private DefaultMutableTreeNode selectedNode;

    private Map<DefaultMutableTreeNode,NewDatabaseInfo> nodeDatabaseMap = new HashMap<>();

    private List<NewDatabaseInfo> myNewDatabaseInfos = new ArrayList<>();

    public MyBastisDatasourceForm(Project project) {
        myProject = project;
        MyBatisDatasourceComponent component = myProject.getComponent(MyBatisDatasourceComponent.class);
        DatasourceState state = component.getState();
        myNewDatabaseInfos = state.getDatabaseInfos();
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyBatisDatasourceConfigView myBatisDatasourceConfigView = new MyBatisDatasourceConfigView(myProject, false,myNewDatabaseInfos);
                boolean b = myBatisDatasourceConfigView.showAndGet();
                if(!b){
                    return;
                } else{
                    //gonna refresh the view.
                    NewDatabaseInfo newDatabaseInfo =
                            myBatisDatasourceConfigView.getNewDatabaseInfo();

                    DatabaseInfo dataBaseInfoFromConnection = DatabaseConnector.getDataBaseInfoFromConnection(newDatabaseInfo.getDatabaseType(),
                            newDatabaseInfo.getUrl(), newDatabaseInfo.getUserName(), newDatabaseInfo.getPassword(), newDatabaseInfo.getDatabase());
                    DefaultMutableTreeNode top =
                            new DefaultMutableTreeNode(dataBaseInfoFromConnection.getDatabaseName());
                    createNodes(top,dataBaseInfoFromConnection.getTableInfoList(),newDatabaseInfo);
                    DefaultTreeModel model = (DefaultTreeModel) datasourceTree.getModel();
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                    model.insertNodeInto(top,root,root.getChildCount());
                    //shoud load state of the database.
                    DatasourceState state = myProject.getComponent(MyBatisDatasourceComponent.class).getState();
                    state.getDatabaseInfos().add(newDatabaseInfo);
                    myProject.getComponent(MyBatisDatasourceComponent.class).loadState(state);
                }
            }
        });
    }

    private void createNodes(DefaultMutableTreeNode top, List<TableInfo> tableInfos,NewDatabaseInfo info) {
        for (TableInfo tableInfo : tableInfos) {
            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(tableInfo.getTableName());
            nodeDatabaseMap.put(defaultMutableTreeNode,info);
            top.add(defaultMutableTreeNode);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("Data sources");
        datasourceTree = new JTree(top);

    }

    public JPanel getMyDatasourcePanel() {
        DefaultTreeModel model = (DefaultTreeModel) datasourceTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        for (NewDatabaseInfo databaseInfo : myNewDatabaseInfos) {
            DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(databaseInfo.getDatabase());
            nodeDatabaseMap.put(newChild,databaseInfo);
            root.add(newChild);
        }
        datasourceTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    TreePath pathForLocation = datasourceTree.getPathForLocation(e.getX(), e.getY());
                    if(pathForLocation!=null){
                        selectedNode = (DefaultMutableTreeNode)pathForLocation.getLastPathComponent();
                        datasourceTree.getSelectionModel().setSelectionPath(pathForLocation);
                        JPopupMenu popupMenu = new JPopupMenu();
                        JMenuItem menuItem = new JMenuItem("mybatis generator");
                        JMenuItem refresh = new JMenuItem("refresh");
                        menuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.out.println(nodeDatabaseMap.get(selectedNode));;
                            }
                        });
                        popupMenu.add(menuItem);
                        popupMenu.add(refresh);
                        popupMenu.show(datasourceTree,e.getX(),e.getY());
                    }
                    System.out.println("right mouse");
                }
            }
        });
        return myDatasourcePanel;
    }
}
