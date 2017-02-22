package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.InsertFileProp;
import com.ccnode.codegenerator.dialog.datatype.MySqlTypeUtil;
import com.ccnode.codegenerator.dialog.datatype.UnsignedCheckResult;
import com.ccnode.codegenerator.util.DateUtil;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/17 20:12
 */
public class GenSqlService {

    public static void generateSqlFile(InsertFileProp prop, List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName) {
        List<String> retList = Lists.newArrayList();
        String newTableName = GenCodeUtil.wrapComma(tableName);
        retList.add(String.format("-- auto Generated on %s ", DateUtil.formatLong(new Date())));
        retList.add("-- DROP TABLE IF EXISTS " + newTableName + "; ");
        retList.add("CREATE TABLE " + newTableName + "(");
        List<String> indexText = Lists.newArrayList();
        for (GenCodeProp field : propList) {
            String fieldSql = genfieldSql(field);
            retList.add(fieldSql);
            if (field.getIndex()) {
                indexText.add(GenCodeUtil.ONE_RETRACT + "INDEX(" + field.getColumnName() + "),");
            }
        }
        retList.addAll(indexText);
        // TODO: 2016/12/26 InnoDb and utf8 can be later configured
        retList.add(GenCodeUtil.ONE_RETRACT + "PRIMARY KEY (" + GenCodeUtil.wrapComma(primaryKey.getColumnName()) + ")");
        retList.add(String.format(")ENGINE=%s DEFAULT CHARSET=%s COMMENT '%s';", "InnoDB",
                "utf8", newTableName));

        try {
            String filePath = prop.getFolderPath() + "/" + prop.getName();
            Files.write(Paths.get(filePath), retList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("can't write file " + prop.getName() + " to path " + prop.getFolderPath() + "/" + prop.getName(),e);
        }
        //then go write to the file.
    }

    private static String genfieldSql(GenCodeProp field) {
        StringBuilder ret = new StringBuilder();
        UnsignedCheckResult result = MySqlTypeUtil.checkUnsigned(field.getFiledType());
        ret.append(GenCodeUtil.ONE_RETRACT).append(GenCodeUtil.wrapComma(field.getColumnName()))
                .append(" ").append(result.getType());
        if (org.apache.commons.lang.StringUtils.isNotBlank(field.getSize())) {
            ret.append(" (" + field.getSize() + ")");
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

        if (!field.getPrimaryKey() && field.getHasDefaultValue() && org.apache.commons.lang.StringUtils.isNotBlank(field.getDefaultValue())) {
            ret.append(" DEFAULT " + field.getDefaultValue());
        }

        if (field.getPrimaryKey()) {
            ret.append(" AUTO_INCREMENT");
        }

        ret.append(" COMMENT '" + field.getFieldName() + "',");

        return ret.toString();
    }
}


