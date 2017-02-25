package com.ccnode.codegenerator.database.handler;

import com.ccnode.codegenerator.database.ClassValidateResult;
import com.ccnode.codegenerator.database.JavaTypeConstant;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.datatype.MysqlTypeConstants;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTypesUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
public class OracleDatabaseHandler implements DatabaseHandler {

    private OracleFieldValidator validator = new OracleFieldValidator();


    private static Map<String, List<TypeProps>> oracleTypeProps = Maps.newHashMap();

    static {
        //display type prop for data.
        TypeProps integerSqlTypeProps = new TypeProps(MysqlTypeConstants.INTEGER, "11", "-1");
        oracleTypeProps.put(JavaTypeConstant.INTEGER, newArrayListWithOrder(integerSqlTypeProps));

        TypeProps bigIntType = new TypeProps(MysqlTypeConstants.BIGINT, "15", "-1");
        oracleTypeProps.put(JavaTypeConstant.LONG, newArrayListWithOrder(bigIntType));

        TypeProps FLOAT = new TypeProps(MysqlTypeConstants.FLOAT, "10,2", "-1.0");
        oracleTypeProps.put(JavaTypeConstant.FLOAT, newArrayListWithOrder(FLOAT));

        TypeProps DOUBLE = new TypeProps(MysqlTypeConstants.DOUBLE, "16,4", "-1.0");
        oracleTypeProps.put(JavaTypeConstant.DOUBLE, newArrayListWithOrder(DOUBLE));

        TypeProps TINYINT = new TypeProps(MysqlTypeConstants.TINYINT, "3", "0");

        TypeProps BIT = new TypeProps(MysqlTypeConstants.BIT, "1", "0");
        oracleTypeProps.put(JavaTypeConstant.BOOLEAN, newArrayListWithOrder(TINYINT, BIT));

        TypeProps DECIMAL = new TypeProps(MysqlTypeConstants.DECIMAL, "13,4", "-1");
        oracleTypeProps.put(JavaTypeConstant.BIGDECIMAL, newArrayListWithOrder(DECIMAL));

        TypeProps MEDIUMINT = new TypeProps(MysqlTypeConstants.MEDIUMINT, "7", "-1");

        TypeProps SMALLINT = new TypeProps(MysqlTypeConstants.SMALLINT, "5", "-1");
        oracleTypeProps.put(JavaTypeConstant.SHORT, newArrayListWithOrder(SMALLINT, MEDIUMINT));

/*-------------------------------------------date time  */
        TypeProps DATETIME = new TypeProps(MysqlTypeConstants.DATETIME, "", "'1000-01-01 00:00:00'");

        TypeProps DATE = new TypeProps(MysqlTypeConstants.DATE, "", "'1000-01-01'");

        TypeProps TIMESTAMP = new TypeProps(MysqlTypeConstants.TIMESTAMP, null, "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");


        TypeProps TIME = new TypeProps(MysqlTypeConstants.TIME, null, "'12:00'");

        oracleTypeProps.put(JavaTypeConstant.DATE, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        oracleTypeProps.put(JavaTypeConstant.LOCALDATE, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        oracleTypeProps.put(JavaTypeConstant.LOCALDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));

        oracleTypeProps.put(JavaTypeConstant.LOCALTIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        oracleTypeProps.put(JavaTypeConstant.SQLDATE, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        oracleTypeProps.put(JavaTypeConstant.SQLTIMESTAMP, newArrayListWithOrder(TIMESTAMP, DATE, DATETIME, TIME));
        oracleTypeProps.put(JavaTypeConstant.SQLTIME, newArrayListWithOrder(TIME, DATE, TIMESTAMP, DATETIME));
        oracleTypeProps.put(JavaTypeConstant.LOCALDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        oracleTypeProps.put(JavaTypeConstant.LOCALTIME, newArrayListWithOrder(TIME, DATETIME, DATE, TIMESTAMP));
        oracleTypeProps.put(JavaTypeConstant.LOCALDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        oracleTypeProps.put(JavaTypeConstant.OFFSETDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));
        oracleTypeProps.put(JavaTypeConstant.OFFSETTIME, newArrayListWithOrder(TIME, DATETIME, TIMESTAMP, DATETIME));
        oracleTypeProps.put(JavaTypeConstant.ZONEDDATETIME, newArrayListWithOrder(DATETIME, DATE, TIMESTAMP, TIME));

/*------------------------------------------string ect */
        TypeProps VARCHAR = new TypeProps(MysqlTypeConstants.VARCHAR, "50", "\'\'");
        TypeProps TEXT = new TypeProps(MysqlTypeConstants.TEXT, null, null);
        TypeProps MEDIUMTEXT = new TypeProps(MysqlTypeConstants.MEDIUMTEXT, null, "\'\'");
        TypeProps LONGTEXT = new TypeProps(MysqlTypeConstants.LONGTEXT, null, "\'\'");
        TypeProps TINYTEXT = new TypeProps(MysqlTypeConstants.TINYTEXT, null, "\'\'");
        TypeProps CHAR = new TypeProps(MysqlTypeConstants.CHAR, "10", null);
        oracleTypeProps.put(JavaTypeConstant.STRING, newArrayListWithOrder(VARCHAR, TEXT, CHAR, MEDIUMTEXT, LONGTEXT, TINYTEXT));


        TypeProps BLOB = new TypeProps(MysqlTypeConstants.BLOB, "", "\'\'");
        TypeProps TINYBLOB = new TypeProps(MysqlTypeConstants.TINYBLOB, "", "\'\'");
        TypeProps MEDIUMBLOB = new TypeProps(MysqlTypeConstants.MEDIUMBLOB, "", "\'\'");
        TypeProps LONGBLOB = new TypeProps(MysqlTypeConstants.LONGBLOB, "", "\'\'");
        oracleTypeProps.put(JavaTypeConstant.BYTE, newArrayListWithOrder(BLOB, MEDIUMBLOB, LONGBLOB, TINYBLOB));
    }

    @NotNull
    @Override
    public ClassValidateResult validateCurrentClass(PsiClass psiClass) {
        return new HandlerValidator(validator).validateResult(psiClass);
    }


    @NotNull
    @Override
    public List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField field) {
        return null;
    }

    @NotNull
    @Override
    public String generateSql(List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName) {
        return null;
    }

    @Override
    public boolean isSupportedParam(PsiParameter psiParameter) {
        return oracleTypeProps.get(psiParameter.getType().getCanonicalText()) != null;
    }

    class OracleFieldValidator implements FieldValidator {
        @Override
        public boolean isValidField(PsiField psiField) {
            String canonicalText = psiField.getType().getCanonicalText();
            List<TypeProps> typePropss = oracleTypeProps.get(canonicalText);
            if (typePropss != null) {
                return true;
            }
            PsiClass psiClass = PsiTypesUtil.getPsiClass(psiField.getType());
            if (psiClass != null && psiClass.isEnum()) {
                return true;
            }
            return false;
        }
    }


    public static List<TypeProps> newArrayListWithOrder(TypeProps... typePropArray) {
        List<TypeProps> typePropslist = Lists.newArrayList();
        for (int i = 0; i < typePropArray.length; i++) {
            typePropArray[i].setOrder(i);
            typePropslist.add(typePropArray[i]);
        }
        return typePropslist;
    }
}
