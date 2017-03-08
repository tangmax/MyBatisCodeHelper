package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.database.DatabaseComponenent;
import com.ccnode.codegenerator.database.handler.oracle.OracleHandlerUtils;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.InsertFileProp;
import com.ccnode.codegenerator.dialog.dto.mybatis.ColumnAndFieldAndFormattedColumn;
import com.ccnode.codegenerator.freemarker.TemplateConstants;
import com.ccnode.codegenerator.freemarker.TemplateUtil;
import com.ccnode.codegenerator.myconfigurable.DataBaseConstants;
import com.ccnode.codegenerator.myconfigurable.MyBatisCodeHelperApplicationComponent;
import com.ccnode.codegenerator.pojo.ClassInfo;
import com.ccnode.codegenerator.util.FileUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
            columnAndField.setFormattedColumn(DatabaseComponenent.formatColumn(prop.getColumnName()));
            return columnAndField;
        }).collect(Collectors.toList());
        root.put(TemplateConstants.USE_GENERATED_KEYS, MyBatisCodeHelperApplicationComponent.getInstance().getState().getProfile().getUseGeneratedKeys());
        root.put(TemplateConstants.FIELD_AND_COLUMNS, columnAndFields);
        root.put(TemplateConstants.PRIMARY_COLUMN, DatabaseComponenent.formatColumn(primaryProp.getColumnName()));
        root.put(TemplateConstants.PRIMARY_FIELD, primaryProp.getFieldName());
        root.put(TemplateConstants.TABLE_NAME, tableName);
        root.put(TemplateConstants.CURRENTDATABASE, DatabaseComponenent.currentDatabase());
        if (DatabaseComponenent.currentDatabase().equals(DataBaseConstants.ORACLE)) {
            String primaryKeyJdbcType = OracleHandlerUtils.extractJdbcType(primaryProp);
            root.put(TemplateConstants.PRIMAY_JDBC_TYPE, primaryKeyJdbcType);
        }
        String generateMapperString = TemplateUtil.processToString(TemplateConstants.GENCODE_MAPPERXML, root);
        retList.add(generateMapperString);
        FileUtils.writeFiles(fileProp,retList);
    }



}
