package com.ccnode.codegenerator.database;

import com.ccnode.codegenerator.dialog.dto.mybatis.ColumnAndField;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.FetchProp;
import com.ccnode.codegenerator.pojo.FieldToColumnRelation;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.PsiClassUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/3/6
 * @Description
 */
public class GenClassDialog extends DialogWrapper {

    private Project myProject;

    private List<FetchProp> fetchPropList;

    private Map<String, String> fieldMap;

    private String methodName;

    private String resultMapName;

    private String classPackageName;

    private List<FieldInfo> fieldInfoList;

    private List<ColumnAndField> columnAndFields;

    private FieldToColumnRelation relation;

    private JTable myJtable;

    private JTextField classFolderText;


    private String modulePath;


    private JTextField resultMapText;

    private JTextField classNameText;

    private String classQutifiedName;


    public GenClassDialog(Project project, List<FetchProp> props, Map<String, String> fieldMap, String methodName, FieldToColumnRelation relation, PsiClass srcClass) {
        super(project, true);
        //just need to know the module path.

        this.myProject = project;
        this.fetchPropList = props;
        this.fieldMap = fieldMap;
        this.methodName = methodName;
        this.relation = relation;
        this.resultMapName = methodName;
        this.fieldInfoList = buildClassInfo(props, fieldMap, relation);
        this.classFolderText = new JTextField(srcClass.getContainingFile().getVirtualFile().getParent().getPath());
        this.classNameText = new JTextField(methodName + "Result");
        this.resultMapText = new JTextField(methodName + "Result");
        this.modulePath = PsiClassUtil.getModuleSrcPathOfClass(srcClass);
        this.myJtable = new JTable(extractValue(fieldInfoList), new String[]{"columnName", "fieldName", "fieldType"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        myJtable.getTableHeader().setReorderingAllowed(false);
        setTitle("create new Class for the result");

        init();
        //make them to customized field names.
        //use with a jtable.
    }

    private static Object[][] extractValue(List<FieldInfo> fieldInfos) {
        Object[][] values = new Object[fieldInfos.size()][];
        for (int i = 0; i < fieldInfos.size(); i++) {
            values[i] = new String[3];
            values[i][0] = fieldInfos.get(i).getColumnName();
            values[i][1] = fieldInfos.get(i).getFieldName();
            values[i][2] = fieldInfos.get(i).getFieldType();
        }
        return values;
    }

    private List<FieldInfo> buildClassInfo(List<FetchProp> props, Map<String, String> fieldMap, FieldToColumnRelation relation) {
        ArrayList<FieldInfo> fieldInfos = new ArrayList<>();
        for (FetchProp prop : props) {
            if (prop.getFetchFunction() == null) {
                FieldInfo fieldInfo = new FieldInfo();
                String columnName = relation.getPropColumn(prop.getFetchProp());
                fieldInfo.setFieldName(GenCodeUtil.getLowerCamel(columnName));
                fieldInfo.setFieldType(fieldMap.get(prop.getFetchProp()));
                fieldInfo.setColumnName(columnName);
                fieldInfos.add(fieldInfo);
            } else {
                String column = DbUtils.buildSelectFunctionVal(prop);
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setColumnName(column);
                fieldInfo.setFieldName(column);
                fieldInfo.setFieldType(DbUtils.getReturnClassFromFunction(fieldMap, prop.getFetchFunction(), prop.getFetchProp()));
                fieldInfos.add(fieldInfo);
            }
        }
        return fieldInfos;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        GridBagConstraints bag = new GridBagConstraints();
        int mygridy = 0;
        bag.anchor = GridBagConstraints.NORTHWEST;
        bag.fill = GridBagConstraints.BOTH;
        bag.gridwidth = 1;
        bag.weightx = 1;
        bag.weighty = 1;
        bag.gridy = mygridy++;
        bag.gridx = 0;
        jPanel.add(new JLabel("className"), bag);
        bag.gridx = 1;
        jPanel.add(classNameText, bag);
        bag.gridy = 1;
        bag.gridx = 0;
        jPanel.add(new JLabel("resultMapId:"), bag);

        bag.gridx = 1;
        jPanel.add(resultMapText, bag);

        bag.gridy = 3;
        bag.gridx = 0;
        jPanel.add(new JLabel(" class location:"), bag);
        bag.gridx = 1;
        jPanel.add(classFolderText, bag);

        bag.gridy = 4;
        bag.gridx = 1;
        JScrollPane jScrollPane = new JScrollPane(myJtable);
        jPanel.add(jScrollPane, bag);
        return jPanel;
    }

    @Override
    protected void doOKAction() {
        //generate the info for it.
        //gonna save the result.
        String className = classNameText.getText();
        String resultMapId = resultMapText.getText();

        StringBuilder classFileBuilder = new StringBuilder();

        String folder = classFolderText.getText();
        String packageToModule = PsiClassUtil.getPackageToModule(folder, modulePath);

        int rowCount = myJtable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String columnName = (String) myJtable.getValueAt(i, 0);
            String fieldName = (String) myJtable.getValueAt(i, 1);
            String fieldType = (String) myJtable.getValueAt(i, 2);
            ColumnAndField columnAndField = new ColumnAndField();
            columnAndField.setColumn(columnName);
            columnAndField.setField(fieldName);
            this.columnAndFields.add(columnAndField);
        }
    }
}
