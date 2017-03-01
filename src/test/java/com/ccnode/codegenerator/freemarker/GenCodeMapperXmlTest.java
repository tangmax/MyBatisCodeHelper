package com.ccnode.codegenerator.freemarker;

import com.ccnode.codegenerator.dialog.dto.mybatis.ColumnAndFieldAndFormattedColumn;
import com.ccnode.codegenerator.myconfigurable.DataBaseConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/2/15
 * @Description
 */
public class GenCodeMapperXmlTest {
    @Test
    public void testGenerateMapperXml() {
        Map<String, Object> root = Maps.newHashMap();
        root.put(TemplateConstants.DAO_FULLTYPE, "com.bruce.ge");
        root.put(TemplateConstants.POJO_FULL_TYPE, "com.myUser");
        List<ColumnAndFieldAndFormattedColumn> columnAndFields = Lists.newArrayList(
                createFieldAndColumn("id","my_id"),
                createFieldAndColumn("userName","user_name"),
                createFieldAndColumn("password","password")
        );
        root.put(TemplateConstants.USE_GENERATED_KEYS, true);
        root.put(TemplateConstants.FIELD_AND_COLUMNS, columnAndFields);
        root.put(TemplateConstants.PRIMARY_COLUMN, "id");
        root.put(TemplateConstants.PRIMARY_FIELD, "id");
        root.put(TemplateConstants.TABLE_NAME, "my_user");
        root.put(TemplateConstants.CURRENTDATABASE, DataBaseConstants.ORACLE);
        root.put(TemplateConstants.PRIMAY_JDBC_TYPE, "NUMERIC");
        String generateMapperString = TemplateUtil.processToString(TemplateConstants.GENCODE_MAPPERXML, root);
        System.out.println(generateMapperString);
    }

    private ColumnAndFieldAndFormattedColumn createFieldAndColumn(String fieldName,String columnName) {
        ColumnAndFieldAndFormattedColumn columnAndFieldAndFormattedColumn = new ColumnAndFieldAndFormattedColumn();
        columnAndFieldAndFormattedColumn.setFormattedColumn(columnName);
        columnAndFieldAndFormattedColumn.setField(fieldName);
        columnAndFieldAndFormattedColumn.setColumn(columnName);
        return columnAndFieldAndFormattedColumn;
    }

}
