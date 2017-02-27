package com.ccnode.codegenerator.database.handler.mysql;

import com.ccnode.codegenerator.database.handler.UpdateFieldHandler;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.ccnode.codegenerator.dialog.dto.mybatis.ColumnAndField;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class MysqlUpdateFieldHandler implements UpdateFieldHandler {


    private static volatile MysqlUpdateFieldHandler mInstance;

    private MysqlUpdateFieldHandler() {
    }

    public static MysqlUpdateFieldHandler getInstance() {
        if (mInstance == null) {
            synchronized (MysqlUpdateFieldHandler.class) {
                if (mInstance == null) {
                    mInstance = new MysqlUpdateFieldHandler();
                }
            }
        }
        return mInstance;
    }

    @Override
    public String generateUpdateSql(List<GenCodeProp> newAddedProps, String tableName, List<ColumnAndField> deletedFields) {
        StringBuilder ret = new StringBuilder();
        for (GenCodeProp field : newAddedProps) {
            ret.append("ALTER TABLE " + tableName + "\n\tADD " + field.getColumnName());
            //todo need remove to mysql handler.
            UnsignedCheckResult result = MysqlHandlerUtils.checkUnsigned(field.getFiledType());
            ret.append(" " + result.getType());
            if (org.apache.commons.lang.StringUtils.isNotBlank(field.getSize())) {
                ret.append("(" + field.getSize() + ")");
            }
            if (result.isUnsigned()) {
                ret.append(" UNSIGNED");
            }
            if (field.getUnique()) {
                ret.append(" UNIQUE");
            }
            if (!field.getCanBeNull()) {
                ret.append(" NOT NULL");
            }

            if (field.getHasDefaultValue() && org.apache.commons.lang.StringUtils.isNotBlank(field.getDefaultValue())) {
                ret.append(" DEFAULT " + field.getDefaultValue());
            }
            if (field.getPrimaryKey()) {
                ret.append(" AUTO_INCREMENT");
            }
            ret.append(" COMMENT '" + field.getFieldName() + "'");
            if (field.getIndex()) {
                ret.append(",\n\tADD INDEX (" + field.getColumnName() + ")");
            }
            ret.append(";");
        }

        for (ColumnAndField deletedField : deletedFields) {
            ret.append("ALTER TABLE " + tableName + " DROP COLUMN ");
            ret.append(deletedField.getColumn() + ";");
        }
        return ret.toString();
    }

    @NotNull
    @Override
    public List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField field) {
        return MysqlHandlerUtils.getRecommendDatabaseTypeOfFieldType(field);
    }
}
