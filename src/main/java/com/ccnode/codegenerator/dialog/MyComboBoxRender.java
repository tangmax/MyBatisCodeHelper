package com.ccnode.codegenerator.dialog;

import com.ccnode.codegenerator.dialog.datatype.TypeProps;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce.ge on 2016/12/27.
 */
class MyComboBoxRender implements TableCellRenderer {

    private Map<Integer, JComboBox> jComboBoxMap = new HashMap<>();
    private Map<String, List<TypeProps>> fieldTypeMap;

    public MyComboBoxRender(Map<String, List<TypeProps>> fieldTypeMap) {
        this.fieldTypeMap = fieldTypeMap;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (jComboBoxMap.get(row) == null) {
            JComboBox jComboBox = new JComboBox();
            Object fieldName = table.getValueAt(row, 0);
            List<TypeProps> typePropsList = fieldTypeMap.get(fieldName);
            for (TypeProps getType : typePropsList) {
                jComboBox.addItem(getType.getDefaultType());
            }
            jComboBoxMap.put(row, jComboBox);
        }
        JComboBox jComboBox = jComboBoxMap.get(row);
        if (value != null) {
            jComboBox.setSelectedItem(value);
            //
        }
        return jComboBox;
        //find the filedType.

    }
}
