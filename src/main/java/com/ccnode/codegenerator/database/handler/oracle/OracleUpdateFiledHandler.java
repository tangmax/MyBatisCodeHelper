package com.ccnode.codegenerator.database.handler.oracle;

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
public class OracleUpdateFiledHandler implements UpdateFieldHandler {

    private static volatile OracleUpdateFiledHandler mInstance;

    private OracleUpdateFiledHandler() {
    }

    public static OracleUpdateFiledHandler getInstance() {
        if (mInstance == null) {
            synchronized (OracleUpdateFiledHandler.class) {
                if (mInstance == null) {
                    mInstance = new OracleUpdateFiledHandler();
                }
            }
        }
        return mInstance;
    }

    @Override
    public String generateUpdateSql(List<GenCodeProp> newAddedProps, String tableName, List<ColumnAndField> deletedFields) {
        //todo
        return null;
    }

    @NotNull
    @Override
    public List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField field) {
        return OracleHandlerUtils.getRecommendDatabaseTypeOfFieldType(field);
    }
}
