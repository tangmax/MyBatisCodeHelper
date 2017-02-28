package com.ccnode.codegenerator.database.handler;

import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.dto.mybatis.ColumnAndField;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public interface UpdateFieldHandler extends JTableRecommendHandler {
    String generateUpdateSql(List<GenCodeProp> newAddedProps, String tableName, List<ColumnAndField> deletedFields);
}
