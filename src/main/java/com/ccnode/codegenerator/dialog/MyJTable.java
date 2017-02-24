package com.ccnode.codegenerator.dialog;

import com.ccnode.codegenerator.dialog.datatype.*;
import com.ccnode.codegenerator.dialog.exception.NotStringException;
import com.ccnode.codegenerator.util.GenCodeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce.ge on 2016/12/27.
 */
class MyJTable extends JTable {
    public static final String COLUMNUNIQUE = "unique";
    public static final String FILEDCOLUMN = "filed";
    public static final String COLUMN_NAMECOLUMN = "columnName";
    public static final String TYPECOLUMN = "type";
    public static final String PRIMARYCOLUMN = "primary";
    public static final String LENGTHCOLUMN = "length";
    public static final String CANBENULLCOLUMN = "canBeNull";
    public static final String HAS_DEFAULTVALUE_COLUMN = "hasDefaultValue";
    public static final String DEFAULT_VALUE_COLUMN = "defaultValue";
    public static final String INDEX_COLUMN = "index";
    public static final int FIELDCOLUMNINDEX = 0;
    public static final int COLUMN_NAMECOLUMNINDEX = 1;
    public static final int TYPECOLUMNINDEX = 2;
    public static final int LENGTHCOLUMNINDEX = 3;
    public static final int UNIQUECOLUMNINDEX = 4;
    public static final int PRIMARYCOLUMNINDEX = 5;
    public static final int CANBENULLCOLUMNINDEX = 6;
    public static final int HAS_DEFAULTVALUE_COLUMNINDEX = 7;
    public static final int DEFAULT_VALUECOLUMNINDEX = 8;
    public static final int INDEXCOLUMNINDEX = 9;
    public static String[] columnNames = {FILEDCOLUMN, COLUMN_NAMECOLUMN, TYPECOLUMN, LENGTHCOLUMN, COLUMNUNIQUE, PRIMARYCOLUMN, CANBENULLCOLUMN, HAS_DEFAULTVALUE_COLUMN, DEFAULT_VALUE_COLUMN, INDEX_COLUMN};

    public MyJTable(Object[][] propData, Map<String, List<TypeProps>> fieldTypeMap) {
        super(propData, columnNames);
        this.getTableHeader().setReorderingAllowed(false);

        this.getColumn(MyJTable.COLUMNUNIQUE).setCellRenderer(new CheckButtonRender());
        this.getColumn(MyJTable.COLUMNUNIQUE).setCellEditor(new CheckButtonEditor(new JCheckBox()));


        this.getColumn(MyJTable.PRIMARYCOLUMN).setCellRenderer(new CheckButtonRender());
        this.getColumn(MyJTable.PRIMARYCOLUMN).setCellEditor(new CheckButtonEditor(new JCheckBox()));

        this.getColumn(MyJTable.CANBENULLCOLUMN).setCellRenderer(new CheckButtonRender());
        this.getColumn(MyJTable.CANBENULLCOLUMN).setCellEditor(new CheckButtonEditor(new JCheckBox()));


        this.getColumn(MyJTable.HAS_DEFAULTVALUE_COLUMN).setCellRenderer(new CheckButtonRender());
        this.getColumn(MyJTable.HAS_DEFAULTVALUE_COLUMN).setCellEditor(new CheckButtonEditor(new JCheckBox()));


        this.getColumn(MyJTable.INDEX_COLUMN).setCellRenderer(new CheckButtonRender());
        this.getColumn(MyJTable.INDEX_COLUMN).setCellEditor(new CheckButtonEditor(new JCheckBox()));


        this.getColumn(MyJTable.TYPECOLUMN).setCellRenderer(new MyComboBoxRender(fieldTypeMap));

        this.getColumn(MyJTable.TYPECOLUMN).setCellEditor(new MyComboBoxEditor(new JComboBox(), fieldTypeMap));
        this.setRowHeight(25);

        this.setFillsViewportHeight(true);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    static Object[][] getDatas(java.util.List<ClassFieldInfo> propFields) {
        List<Object[]> ssList = new ArrayList<>();
        for (int i = 0; i < propFields.size(); i++) {
            Object[] mm = new Object[columnNames.length];
            ClassFieldInfo info = propFields.get(i);
            mm[FIELDCOLUMNINDEX] = info.getFieldName();
            mm[COLUMN_NAMECOLUMNINDEX] = GenCodeUtil.getUnderScore(info.getFieldName());
            TypeProps typeProp = MySqlTypeUtil.getType(info.getFieldType());
            if (typeProp == null) {
                // TODO: 2016/12/25 ask user if ignore.
                continue;
            }
            customTypeProp(info, typeProp);
            mm[TYPECOLUMNINDEX] = typeProp.getDefaultType();
            mm[LENGTHCOLUMNINDEX] = typeProp.getSize();
            mm[UNIQUECOLUMNINDEX] = typeProp.getUnique();
            mm[PRIMARYCOLUMNINDEX] = typeProp.getPrimary();
            mm[CANBENULLCOLUMNINDEX] = typeProp.getCanBeNull();
            mm[DEFAULT_VALUECOLUMNINDEX] = typeProp.getDefaultValue();
            mm[HAS_DEFAULTVALUE_COLUMNINDEX] = typeProp.getHasDefaultValue();
            mm[INDEXCOLUMNINDEX] = typeProp.getIndex();
            ssList.add(mm);
        }
        Object[][] ss = new Object[ssList.size()][];
        ssList.toArray(ss);
        return ss;
    }

    private static void customTypeProp(ClassFieldInfo info, TypeProps typeProp) {
        if (info.getFieldName().equals("id")) {
            typeProp.setPrimary(true);
            typeProp.setHasDefaultValue(false);
        } else if (info.getFieldName().toLowerCase().equals("updatetime")) {
            TypeDefault typeDefault = MySqlTypeUtil.getTypeDefault(MysqlTypeConstants.TIMESTAMP);
            typeProp.setDefaultType(MysqlTypeConstants.TIMESTAMP);
            typeProp.setDefaultValue(typeDefault.getDefaultValue());
            typeProp.setSize(typeDefault.getSize());
        }
    }

    static Boolean formatBoolean(Object unique) {
        if (unique == null) {
            return false;
        }

        if (!(unique instanceof Boolean)) {
            return false;
        }
        return (Boolean) unique;
    }

    static String formatString(Object value) {
        if (value == null) {
            return "";
        }
        if (!(value instanceof String)) {
            // TODO: 2016/12/28 need handle it.
            throw new NotStringException();
        }
        return ((String) value).trim();
    }


    @NotNull
    static GenCodeProp getGenCodeProp(int i, JTable propTable) {
        GenCodeProp prop = new GenCodeProp();
        Object value = propTable.getValueAt(i, FIELDCOLUMNINDEX);
        prop.setFieldName(formatString(value));

        Object column = propTable.getValueAt(i, COLUMN_NAMECOLUMNINDEX);
        prop.setColumnName(formatString(column));

        Object type = propTable.getValueAt(i, TYPECOLUMNINDEX);
        prop.setFiledType(formatString(type));

        Object length = propTable.getValueAt(i, LENGTHCOLUMNINDEX);
        prop.setSize(formatString(length));

        Object unique = propTable.getValueAt(i, UNIQUECOLUMNINDEX);
        prop.setUnique(formatBoolean(unique));

        Object primary = propTable.getValueAt(i, PRIMARYCOLUMNINDEX);
        prop.setPrimaryKey(formatBoolean(primary));

        Object canbenull = propTable.getValueAt(i, CANBENULLCOLUMNINDEX);
        prop.setCanBeNull(formatBoolean(canbenull));

        Object hasDefualtValue = propTable.getValueAt(i, HAS_DEFAULTVALUE_COLUMNINDEX);
        prop.setHasDefaultValue(formatBoolean(hasDefualtValue));

        Object isIndex = propTable.getValueAt(i, INDEXCOLUMNINDEX);
        prop.setIndex(formatBoolean(isIndex));

        Object defaultValue = propTable.getValueAt(i, DEFAULT_VALUECOLUMNINDEX);
        prop.setDefaultValue(formatString(defaultValue));
        return prop;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        int headerHeight = this.getTableHeader().getPreferredSize().height;
        int height = headerHeight + (10 * getRowHeight());
        int width = getPreferredSize().width;
        return new Dimension(width, height);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        super.setValueAt(aValue, row, column);
        if (column == TYPECOLUMNINDEX) {
            TypeDefault typeDefault = MySqlTypeUtil.getTypeDefault((String) aValue);
            if (typeDefault == null) {
                super.setValueAt(null, row, LENGTHCOLUMNINDEX);
                super.setValueAt(null, row, DEFAULT_VALUECOLUMNINDEX);
            } else {
                super.setValueAt(typeDefault.getSize(), row, LENGTHCOLUMNINDEX);
                super.setValueAt(typeDefault.getDefaultValue(), row, DEFAULT_VALUECOLUMNINDEX);
            }
        }

        if (column == PRIMARYCOLUMNINDEX) {
            if (aValue instanceof Boolean) {
                Boolean s = (Boolean) aValue;
                if (s) {
                    for (int i = 0; i < this.getRowCount(); i++) {
                        if (i != row) {
                            super.setValueAt(false, i, column);
                        }
                    }
                    super.setValueAt(false, row, HAS_DEFAULTVALUE_COLUMNINDEX);
                    super.setValueAt(false, row, INDEXCOLUMNINDEX);
                } else {
                    super.setValueAt(true, row, HAS_DEFAULTVALUE_COLUMNINDEX);
                }
            }
        }
    }
}
