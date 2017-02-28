package com.ccnode.codegenerator.dialog;

import com.ccnode.codegenerator.dialog.datatype.TypeProps;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce.ge on 2016/12/27.
 */
class MyComboBoxEditor extends DefaultCellEditor {

    private Map<Integer, String[]> itemMap = new HashMap<>();
    private Map<String, List<TypeProps>> fieldTypeMap;

    public MyComboBoxEditor(JComboBox comboBox, Map<String, List<TypeProps>> fieldTypeMap) {
        super(comboBox);
        this.fieldTypeMap = fieldTypeMap;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox editorComponent
                = (JComboBox) this.editorComponent;
        editorComponent.removeAllItems();
        if (itemMap.get(row) == null) {
            Object fieldName = table.getValueAt(row, 0);
            List<TypeProps> typePropsList = fieldTypeMap.get(fieldName);
            for (TypeProps recommend : typePropsList) {
                editorComponent.addItem(recommend.getDefaultType());
            }
            itemMap.put(row, typePropsList.stream().map(item -> item.getDefaultType()).toArray(size -> new String[size]));
        } else {
            for (String recommend : itemMap.get(row)) {
                editorComponent.addItem(recommend);
            }
        }

        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }


}
