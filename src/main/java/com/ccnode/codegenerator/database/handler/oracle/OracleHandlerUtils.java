package com.ccnode.codegenerator.database.handler.oracle;

import com.ccnode.codegenerator.database.JavaTypeConstant;
import com.ccnode.codegenerator.database.handler.JdbcTypeConstatns;
import com.ccnode.codegenerator.database.handler.mysql.MysqlTypeConstants;
import com.ccnode.codegenerator.database.handler.utils.TypePropUtils;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class OracleHandlerUtils {
    private static Map<String, List<TypeProps>> oracleTypeProps = Maps.newHashMap();

    static {
        //display type prop for data.
        TypeProps integerSqlTypeProps = new TypeProps(OracleTypeConstants.NUMBER, "12", "-1");
        oracleTypeProps.put(JavaTypeConstant.INTEGER, newArrayListWithOrder(integerSqlTypeProps));

        TypeProps LONGNUMBER = new TypeProps(OracleTypeConstants.NUMBER, "24", "-1");
        oracleTypeProps.put(JavaTypeConstant.LONG, newArrayListWithOrder(LONGNUMBER));

        TypeProps FLOATNUMBER = new TypeProps(OracleTypeConstants.NUMBER, "10,2", "-1");
        oracleTypeProps.put(JavaTypeConstant.FLOAT, newArrayListWithOrder(FLOATNUMBER));

        TypeProps DOUBLENUMBER = new TypeProps(OracleTypeConstants.NUMBER, "16.4", "-1");
        oracleTypeProps.put(JavaTypeConstant.DOUBLE, newArrayListWithOrder(DOUBLENUMBER));

        TypeProps BOOLEAN = new TypeProps(OracleTypeConstants.BOOLEAN, null, "-1");
        oracleTypeProps.put(JavaTypeConstant.BOOLEAN, newArrayListWithOrder(BOOLEAN));

        TypeProps DECIMAL = new TypeProps(OracleTypeConstants.NUMBER, "13,4", "-1");
        oracleTypeProps.put(JavaTypeConstant.BIGDECIMAL, newArrayListWithOrder(DECIMAL));

        TypeProps MEDIUMINT = new TypeProps(OracleTypeConstants.NUMBER, "12", "-1");

        TypeProps SMALLINT = new TypeProps(OracleTypeConstants.NUMBER, "5", "-1");
        oracleTypeProps.put(JavaTypeConstant.SHORT, newArrayListWithOrder(SMALLINT, MEDIUMINT));

/*-------------------------------------------date time  */
        TypeProps DATE = new TypeProps(OracleTypeConstants.DATE, "", "SYSDATE");

        TypeProps TIMESTAMP = new TypeProps(MysqlTypeConstants.TIMESTAMP, null, "SYSDATE");

        oracleTypeProps.put(JavaTypeConstant.DATE, newArrayListWithOrder(DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.LOCALDATE, newArrayListWithOrder(DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.LOCALDATETIME, newArrayListWithOrder(DATE, TIMESTAMP));

        oracleTypeProps.put(JavaTypeConstant.LOCALTIME, newArrayListWithOrder(DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.SQLDATE, newArrayListWithOrder(DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.SQLTIMESTAMP, newArrayListWithOrder(TIMESTAMP, DATE));
        oracleTypeProps.put(JavaTypeConstant.SQLTIME, newArrayListWithOrder(DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.LOCALDATETIME, newArrayListWithOrder(DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.LOCALTIME, newArrayListWithOrder(DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.LOCALDATETIME, newArrayListWithOrder(DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.OFFSETDATETIME, newArrayListWithOrder(DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.OFFSETTIME, newArrayListWithOrder(TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.ZONEDDATETIME, newArrayListWithOrder(DATE, TIMESTAMP));

/*------------------------------------------string ect */
        TypeProps VARCHAR2 = new TypeProps(OracleTypeConstants.VARCHAR2, "50", "\'\'");
        TypeProps CHAR = new TypeProps(OracleTypeConstants.CHAR, "50", null);
        oracleTypeProps.put(JavaTypeConstant.STRING, newArrayListWithOrder(VARCHAR2, CHAR));

        TypeProps BLOB = new TypeProps(OracleTypeConstants.BLOB, "", "\'\'");
        oracleTypeProps.put(JavaTypeConstant.BYTE, newArrayListWithOrder(BLOB));
    }


    public static List<TypeProps> newArrayListWithOrder(TypeProps... typePropArray) {
        List<TypeProps> typePropslist = Lists.newArrayList();
        for (int i = 0; i < typePropArray.length; i++) {
            typePropArray[i].setOrder(i);
            typePropslist.add(typePropArray[i]);
        }
        return typePropslist;
    }


    public static List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField field) {
        String canonicalText = field.getType().getCanonicalText();
        List<TypeProps> fromMapTypes = oracleTypeProps.get(canonicalText);
        List<TypeProps> typePropss = TypePropUtils.generateFromDefaultMap(fromMapTypes);
        if (typePropss != null) {
            if (field.getName().equals("id")) {
                typePropss.get(0).setPrimary(true);
                typePropss.get(0).setHasDefaultValue(false);
            }
            return typePropss;
        }
        PsiClass psiClass = PsiTypesUtil.getPsiClass(field.getType());
        if (psiClass == null || !psiClass.isEnum()) {
            throw new RuntimeException("field type not support, the field is:" + field.getName() + " the type is:" + field.getType().getCanonicalText());
        }
        //mean this is enum type.
        return newArrayListWithOrder(new TypeProps(OracleTypeConstants.VARCHAR2, "50", "''"), new TypeProps(OracleTypeConstants.CHAR, null, null));
    }


    public static List<TypeProps> getTypePropByQulitifiedName(String name) {
        return oracleTypeProps.get(name);
    }

    public static String extractJdbcType(GenCodeProp primaryProp) {
        switch (primaryProp.getFiledType()) {
            case OracleTypeConstants.NUMBER: {
                return JdbcTypeConstatns.NUMERIC;
            }
            case OracleTypeConstants.DATE: {
                return JdbcTypeConstatns.DATE;
            }
            case OracleTypeConstants.CHAR: {
                return JdbcTypeConstatns.CHAR;
            }
            case OracleTypeConstants.VARCHAR2: {
                return JdbcTypeConstatns.VARCHAR;
            }
        }
        throw new RuntimeException("the primary key must be string or number");
    }
}
