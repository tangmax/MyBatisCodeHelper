package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.database.DataBaseHandlerFactory;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.InsertFileProp;
import com.ccnode.codegenerator.dialog.dto.mybatis.ColumnAndFieldAndFormattedColumn;
import com.ccnode.codegenerator.freemarker.TemplateConstants;
import com.ccnode.codegenerator.freemarker.TemplateUtil;
import com.ccnode.codegenerator.pojo.ClassInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/28 21:14
 */
public class GenMapperService {


    public static void generateMapperXmlUsingFtl(InsertFileProp fileProp, List<GenCodeProp> props, ClassInfo srcClass, InsertFileProp daoProp, String tableName, GenCodeProp primaryProp) {
        List<String> retList = Lists.newArrayList();
        Map<String, Object> root = Maps.newHashMap();
        root.put(TemplateConstants.DAO_FULLTYPE, daoProp.getQutifiedName());
        root.put(TemplateConstants.POJO_FULL_TYPE, srcClass.getQualifiedName());
        List<ColumnAndFieldAndFormattedColumn> columnAndFields = props.stream().map((prop) -> {
            ColumnAndFieldAndFormattedColumn columnAndField = new ColumnAndFieldAndFormattedColumn();
            columnAndField.setColumn(prop.getColumnName());
            columnAndField.setField(prop.getFieldName());
            columnAndField.setFormattedColumn(DataBaseHandlerFactory.formatColumn(prop.getColumnName()));
            return columnAndField;
        }).collect(Collectors.toList());
        root.put(TemplateConstants.FIELD_AND_COLUMNS, columnAndFields);
        root.put(TemplateConstants.PRIMARY_COLUMN, primaryProp.getColumnName());
        root.put(TemplateConstants.PRIMARY_FIELD, primaryProp.getFieldName());
        root.put(TemplateConstants.TABLE_NAME, tableName);
        String generateMapperString = TemplateUtil.processToString(TemplateConstants.GENCODE_MAPPERXML, root);
        retList.add(generateMapperString);
        try {
            String filePath = fileProp.getFullPath();
            Files.write(Paths.get(filePath), retList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("can't write file " + fileProp.getName() + " to path " + fileProp.getFullPath(), e);
        }
    }

}
