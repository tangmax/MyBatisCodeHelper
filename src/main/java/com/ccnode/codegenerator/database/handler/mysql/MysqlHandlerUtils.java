package com.ccnode.codegenerator.database.handler.mysql;

import com.ccnode.codegenerator.database.JavaTypeConstant;
import com.ccnode.codegenerator.database.handler.utils.TypePropUtils;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiTypesUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class MysqlHandlerUtils {
    private static Map<String, List<TypeProps>> mysqlTypeProps = Maps.newHashMap();

    static {
        //display type prop for data.
        TypeProps integerSqlTypeProps = new TypeProps(MysqlTypeConstants.INTEGER, "11", "-1");
        TypeProps unsignedIntegerTypeProps = new TypeProps(unsigned(MysqlTypeConstants.INTEGER), "11", "0");
        mysqlTypeProps.put(JavaTypeConstant.INTEGER, newArrayListWithOrder(integerSqlTypeProps, unsignedIntegerTypeProps));

        TypeProps bigIntType = new TypeProps(MysqlTypeConstants.BIGINT, "15", "-1");
        TypeProps unsignedBigIntType = new TypeProps(unsigned(MysqlTypeConstants.BIGINT), "15", "0");
        mysqlTypeProps.put(JavaTypeConstant.LONG, newArrayListWithOrder(bigIntType, unsignedBigIntType));

        TypeProps FLOAT = new TypeProps(MysqlTypeConstants.FLOAT, "10,2", "-1.0");
        mysqlTypeProps.put(JavaTypeConstant.FLOAT, newArrayListWithOrder(FLOAT));

        TypeProps DOUBLE = new TypeProps(MysqlTypeConstants.DOUBLE, "16,4", "-1.0");
        mysqlTypeProps.put(JavaTypeConstant.DOUBLE, newArrayListWithOrder(DOUBLE));

        TypeProps TINYINT = new TypeProps(MysqlTypeConstants.TINYINT, "3", "0");

        TypeProps UNSIGNED_TINYINT = new TypeProps(unsigned(MysqlTypeConstants.TINYINT), "3", "0");
        TypeProps BIT = new TypeProps(MysqlTypeConstants.BIT, "1", "0");
        mysqlTypeProps.put(JavaTypeConstant.BOOLEAN, newArrayListWithOrder(TINYINT, UNSIGNED_TINYINT, BIT));

        TypeProps DECIMAL = new TypeProps(MysqlTypeConstants.DECIMAL, "13,4", "-1");
        mysqlTypeProps.put(JavaTypeConstant.BIGDECIMAL, newArrayListWithOrder(DECIMAL));

        TypeProps MEDIUMINT = new TypeProps(MysqlTypeConstants.MEDIUMINT, "7", "-1");
        TypeProps unsignedMediumInt = new TypeProps(unsigned(MysqlTypeConstants.MEDIUMINT), "7", "0");

        TypeProps SMALLINT = new TypeProps(MysqlTypeConstants.SMALLINT, "5", "-1");
        TypeProps unsignedSMALLINT = new TypeProps(unsigned(MysqlTypeConstants.SMALLINT), "5", "0");
        mysqlTypeProps.put(JavaTypeConstant.SHORT, newArrayListWithOrder(SMALLINT, unsignedSMALLINT, MEDIUMINT, unsignedMediumInt));

/*-------------------------------------------date time  */
        TypeProps DATETIME = new TypeProps(MysqlTypeConstants.DATETIME, "", "'1000-01-01 00:00:00'");

        TypeProps DATE = new TypeProps(MysqlTypeConstants.DATE, "", "'1000-01-01'");

        TypeProps TIMESTAMP = new TypeProps(MysqlTypeConstants.TIMESTAMP, null, "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");


        TypeProps TIME = new TypeProps(MysqlTypeConstants.TIME, null, "'12:00'");

        mysqlTypeProps.put(JavaTypeConstant.DATE, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        mysqlTypeProps.put(JavaTypeConstant.LOCALDATE, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        mysqlTypeProps.put(JavaTypeConstant.LOCALDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));

        mysqlTypeProps.put(JavaTypeConstant.LOCALTIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        mysqlTypeProps.put(JavaTypeConstant.SQLDATE, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        mysqlTypeProps.put(JavaTypeConstant.SQLTIMESTAMP, newArrayListWithOrder(TIMESTAMP, DATE, DATETIME, TIME));
        mysqlTypeProps.put(JavaTypeConstant.SQLTIME, newArrayListWithOrder(TIME, DATE, TIMESTAMP, DATETIME));
        mysqlTypeProps.put(JavaTypeConstant.LOCALDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        mysqlTypeProps.put(JavaTypeConstant.LOCALTIME, newArrayListWithOrder(TIME, DATETIME, DATE, TIMESTAMP));
        mysqlTypeProps.put(JavaTypeConstant.LOCALDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        mysqlTypeProps.put(JavaTypeConstant.OFFSETDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        mysqlTypeProps.put(JavaTypeConstant.OFFSETTIME, newArrayListWithOrder(TIME, DATETIME, TIMESTAMP, DATETIME));
        mysqlTypeProps.put(JavaTypeConstant.ZONEDDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));

/*------------------------------------------string ect */
        TypeProps VARCHAR = new TypeProps(MysqlTypeConstants.VARCHAR, "50", "\'\'");
        TypeProps TEXT = new TypeProps(MysqlTypeConstants.TEXT, null, null);
        TypeProps MEDIUMTEXT = new TypeProps(MysqlTypeConstants.MEDIUMTEXT, null, "\'\'");
        TypeProps LONGTEXT = new TypeProps(MysqlTypeConstants.LONGTEXT, null, "\'\'");
        TypeProps TINYTEXT = new TypeProps(MysqlTypeConstants.TINYTEXT, null, "\'\'");
        TypeProps CHAR = new TypeProps(MysqlTypeConstants.CHAR, "10", null);
        mysqlTypeProps.put(JavaTypeConstant.STRING, newArrayListWithOrder(VARCHAR, TEXT, CHAR, MEDIUMTEXT, LONGTEXT, TINYTEXT));


        TypeProps BLOB = new TypeProps(MysqlTypeConstants.BLOB, "", "\'\'");
        TypeProps TINYBLOB = new TypeProps(MysqlTypeConstants.TINYBLOB, "", "\'\'");
        TypeProps MEDIUMBLOB = new TypeProps(MysqlTypeConstants.MEDIUMBLOB, "", "\'\'");
        TypeProps LONGBLOB = new TypeProps(MysqlTypeConstants.LONGBLOB, "", "\'\'");
        mysqlTypeProps.put(JavaTypeConstant.BYTE, newArrayListWithOrder(BLOB, MEDIUMBLOB, LONGBLOB, TINYBLOB));
    }

    public static List<TypeProps> newArrayListWithOrder(TypeProps... typePropArray) {
        List<TypeProps> typePropslist = Lists.newArrayList();
        for (int i = 0; i < typePropArray.length; i++) {
            typePropArray[i].setOrder(i);
            typePropslist.add(typePropArray[i]);
        }
        return typePropslist;
    }

    private static String unsigned(String type) {
        return type + "_" + MysqlTypeConstants.UNSIGNED;
    }


    @NotNull
    public static List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField psiField) {
        String canonicalText = psiField.getType().getCanonicalText();
        List<TypeProps> fromMapTypes = mysqlTypeProps.get(canonicalText);
        List<TypeProps> typePropss = TypePropUtils.generateFromDefaultMap(fromMapTypes);
        if (typePropss != null) {
            if (psiField.getName().equals("id")) {
                typePropss.get(0).setPrimary(true);
                typePropss.get(0).setHasDefaultValue(false);
            } else if (psiField.getName().equalsIgnoreCase("updatetime")) {
                for (TypeProps props : typePropss) {
                    if (props.getDefaultType().equals(MysqlTypeConstants.TIMESTAMP)) {
                        props.setOrder(-1);
                        break;
                    }
                }
                return typePropss;
            }
            return typePropss;
        }
        PsiClass psiClass = PsiTypesUtil.getPsiClass(psiField.getType());
        if (psiClass == null || !psiClass.isEnum()) {
            throw new RuntimeException("field type not support, the field is:" + psiField.getName() + " the type is:" + psiField.getType().getCanonicalText());
        }
        //mean this is enum type.
        return newArrayListWithOrder(new TypeProps(MysqlTypeConstants.VARCHAR, "50", "''"), new TypeProps(MysqlTypeConstants.TEXT, null, null));
    }

    public static UnsignedCheckResult checkUnsigned(String chooseType) {
        UnsignedCheckResult result = new UnsignedCheckResult();
        String[] split = chooseType.split("_");
        result.setType(split[0]);
        if (split.length == 2 && split[1].equals(MysqlTypeConstants.UNSIGNED)) {
            result.setUnsigned(true);
            return result;
        } else {
            result.setUnsigned(false);
            return result;
        }
    }

    public static List<TypeProps> getTypePropsByQulifiType(String type){
        return mysqlTypeProps.get(type);
    }
}
