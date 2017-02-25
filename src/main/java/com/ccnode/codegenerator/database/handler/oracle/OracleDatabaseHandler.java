package com.ccnode.codegenerator.database.handler.oracle;

import com.ccnode.codegenerator.database.ClassValidateResult;
import com.ccnode.codegenerator.database.JavaTypeConstant;
import com.ccnode.codegenerator.database.handler.BaseQueryBuilder;
import com.ccnode.codegenerator.database.handler.DatabaseHandler;
import com.ccnode.codegenerator.database.handler.FieldValidator;
import com.ccnode.codegenerator.database.handler.HandlerValidator;
import com.ccnode.codegenerator.database.handler.mysql.MysqlTypeConstants;
import com.ccnode.codegenerator.database.handler.utils.TypePropUtils;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.ccnode.codegenerator.nextgenerationparser.buidler.MethodNameParsedResult;
import com.ccnode.codegenerator.nextgenerationparser.buidler.QueryInfo;
import com.ccnode.codegenerator.util.DateUtil;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTypesUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
public class OracleDatabaseHandler implements DatabaseHandler {

    private OracleFieldValidator validator = new OracleFieldValidator();


    private BaseQueryBuilder baseQueryBuilder = new BaseQueryBuilder(new OracleQueryBuilderHandler());


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

    @NotNull
    @Override
    public ClassValidateResult validateCurrentClass(PsiClass psiClass) {
        return new HandlerValidator(validator).validateResult(psiClass);
    }


    @NotNull
    @Override
    public List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField field) {
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

    @NotNull
    @Override
    public String generateSql(List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName) {
        List<String> retList = Lists.newArrayList();
        retList.add(String.format("-- auto Generated on %s ", DateUtil.formatLong(new Date())));
        retList.add("-- DROP TABLE IF EXISTS " + tableName + "; ");
        retList.add("CREATE TABLE " + tableName + "(");
        List<String> indexText = Lists.newArrayList();
        List<String> uniques = Lists.newArrayList();
        for (GenCodeProp field : propList) {
            String fieldSql = genfieldSql(field);
            retList.add(fieldSql);
            if (field.getIndex()) {
                indexText.add("CREATE INDEX " + field.getColumnName() + "_idx ON " + tableName + " (" + field.getColumnName() + ");");
            }
            if (field.getUnique()) {
                uniques.add(GenCodeUtil.ONE_RETRACT + "CONSTRAINT " + field.getColumnName() + "_unique UNIQUE(" + field.getColumnName() + "),");
            }
        }
        retList.addAll(uniques);
        retList.add(GenCodeUtil.ONE_RETRACT + "CONSTRAINT " + tableName + "_pk PRIMARY KEY (" + primaryKey.getColumnName() + "));");
        retList.add(generateAutoIncrementAndTrigger(tableName, primaryKey.getColumnName()));
        retList.addAll(indexText);
        return Joiner.on("\n").join(retList);
    }

    private static String generateAutoIncrementAndTrigger(String tableName, String primaryColumnName) {
        return "create sequence " + tableName + "_seq start with 1 increment by 1 nomaxvalue;\n" +
                "create trigger " + tableName + "_trigger\n" +
                "before insert on " + tableName + "\n" +
                "for each row\n" +
                "   begin\n" +
                "     select " + tableName + "_seq.nextval into :new." + primaryColumnName + " from dual;\n" +
                "   end;";
    }

    private String genfieldSql(GenCodeProp field) {
        StringBuilder ret = new StringBuilder();
        ret.append(GenCodeUtil.ONE_RETRACT).append(field.getColumnName())
                .append(" ").append(field.getFiledType());
        if (org.apache.commons.lang.StringUtils.isNotBlank(field.getSize())) {
            ret.append("(" + field.getSize() + ")");
        }
        if (!field.getPrimaryKey() && field.getHasDefaultValue() && org.apache.commons.lang.StringUtils.isNotBlank(field.getDefaultValue())) {
            ret.append(" DEFAULT " + field.getDefaultValue());
        }
        if (!field.getCanBeNull()) {
            ret.append(" NOT NULL");
        }
        ret.append(",");
        return ret.toString();
    }

    @Override
    public boolean isSupportedParam(PsiParameter psiParameter) {
        return oracleTypeProps.get(psiParameter.getType().getCanonicalText()) != null;
    }

    @Override
    public QueryInfo buildQueryInfoByMethodNameParsedResult(MethodNameParsedResult result) {
        return baseQueryBuilder.buildQueryInfoByMethodNameParsedResult(result);
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
